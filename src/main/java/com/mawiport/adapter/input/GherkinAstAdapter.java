package com.mawiport.adapter.input;

import com.mawiport.port.input.FeatureFileReader;
import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.IdGenerator;
import io.cucumber.messages.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Adapter Input: Membaca file .feature menggunakan Cucumber Gherkin v28.0.0
 * dan meratakannya (flatten) menjadi Map<String, Object> untuk Core Engine.
 */
public class GherkinAstAdapter implements FeatureFileReader {

    private static final Logger LOG = LoggerFactory.getLogger(GherkinAstAdapter.class);

    @Override
    public List<Map<String, Object>> readAndFlatten(Path path) throws IOException {
        // 1. Setup Cucumber Parser (API Resmi Gherkin v28.0.0)
        // Kita buat IdGenerator sederhana menggunakan AtomicInteger karena
        // class IdGenerator.Incrementing sudah dihapus di messages v25.0.1
        AtomicInteger idCounter = new AtomicInteger(0);
        IdGenerator idGenerator = () -> String.valueOf(idCounter.incrementAndGet());

        GherkinParser parser = GherkinParser.builder()
                .includeSource(false)
                .includePickles(false)
                .includeGherkinDocument(true)
                .idGenerator(idGenerator)
                .build();

        List<Map<String, Object>> flattenedData = new ArrayList<>();

        // 2. Parse file dan ambil GherkinDocument dari Stream Envelope
        try (Stream<Envelope> envelopes = parser.parse(path)) {
            GherkinDocument document = envelopes
                    .map(Envelope::getGherkinDocument)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElseThrow(() -> new IOException("Failed to parse Gherkin document from " + path));

            Feature feature = document.getFeature().orElse(null);
            if (feature == null) {
                LOG.warn("No feature found in file: {}", path);
                return flattenedData;
            }

            String featureName = feature.getName();
            List<Tag> featureTags = feature.getTags();

            // 3. Traverse AST: Feature -> Children (Rule / Scenario)
            int tcCounter = 1;
            for (FeatureChild child : feature.getChildren()) {
                if (child.getScenario().isPresent()) {
                    flattenedData.add(flattenScenario(child.getScenario().get(), featureName, "", featureTags, tcCounter++));
                } else if (child.getRule().isPresent()) {
                    Rule rule = child.getRule().get();
                    String ruleName = rule.getName();
                    for (RuleChild ruleChild : rule.getChildren()) {
                        if (ruleChild.getScenario().isPresent()) {
                            flattenedData.add(flattenScenario(ruleChild.getScenario().get(), featureName, ruleName, featureTags, tcCounter++));
                        }
                    }
                }
            }
        }
        return flattenedData;
    }

    private Map<String, Object> flattenScenario(Scenario scenario, String featureName, String ruleName, List<Tag> featureTags, int counter) {
        Map<String, Object> map = new HashMap<>();

        map.put("TC_ID", String.format("TC_%03d", counter));
        map.put("FEATURE_NAME", featureName);
        map.put("RULE_NAME", ruleName);
        map.put("SCENARIO_NAME", scenario.getName());

        // 4. Categorize Steps (Given, When, Then)
        List<String> givens = new ArrayList<>();
        List<String> whens = new ArrayList<>();
        List<String> thens = new ArrayList<>();
        List<String> testData = new ArrayList<>();

        String currentContext = "GIVEN";

        for (Step step : scenario.getSteps()) {
            String keyword = step.getKeyword().trim();
            String text = step.getText();

            if (keyword.equalsIgnoreCase("Given")) currentContext = "GIVEN";
            else if (keyword.equalsIgnoreCase("When")) currentContext = "WHEN";
            else if (keyword.equalsIgnoreCase("Then")) currentContext = "THEN";

            switch (currentContext) {
                case "GIVEN" -> givens.add(text);
                case "WHEN" -> whens.add(text);
                case "THEN" -> thens.add(text);
            }

            // 5. Extract Test Data (DocString, DataTable, Quotes)
            step.getDocString().ifPresent(ds -> testData.add(ds.getContent()));
            step.getDataTable().ifPresent(dt -> {
                for (TableRow row : dt.getRows()) {
                    List<String> cells = row.getCells().stream().map(TableCell::getValue).toList();
                    testData.add(String.join(" | ", cells));
                }
            });

            if (text.contains("\"")) {
                String[] parts = text.split("\"");
                for (int i = 1; i < parts.length; i += 2) {
                    testData.add(parts[i]);
                }
            }
        }

        map.put("GIVEN_STEPS", String.join("\n", givens));
        map.put("WHEN_STEPS", String.join("\n", whens));
        map.put("THEN_STEPS", String.join("\n", thens));
        map.put("TEST_DATA", String.join("\n", testData));

        // 6. Extract Tags with proper inheritance logic
        
        // PLATFORM: scenario overrides feature
        String finalPlatform = extractTag(scenario.getTags(), "API", "web", "android", "ios", "desktop", "table", "phone");
        if (finalPlatform == null) finalPlatform = extractTag(featureTags, "API", "web", "android", "ios", "desktop", "table", "phone");
        if (finalPlatform != null) map.put("TAG_PLATFORM", finalPlatform);
        
        // PRIORITY: scenario overrides feature
        String finalPriority = extractTag(scenario.getTags(), "P0", "P1", "P2", "P3", "HIGH", "MEDIUM", "LOW");
        if (finalPriority == null) finalPriority = extractTag(featureTags, "P0", "P1", "P2", "P3", "HIGH", "MEDIUM", "LOW");
        if (finalPriority != null) map.put("TAG_PRIORITY", finalPriority);
        
        // SEVERITY: scenario overrides feature
        String finalSeverity = extractTag(scenario.getTags(), "S1", "S2", "S3", "S4", "CRITICAL", "MAJOR", "MODERATE", "LOW");
        if (finalSeverity == null) finalSeverity = extractTag(featureTags, "S1", "S2", "S3", "S4", "CRITICAL", "MAJOR", "MODERATE", "LOW");
        if (finalSeverity != null) map.put("TAG_SEVERITY", finalSeverity);
        
        // TYPE: scenario overrides feature
        String finalType = extractTag(scenario.getTags(), "smoke", "regression", "sanity", "integration", "security", "performance");
        if (finalType == null) finalType = extractTag(featureTags, "smoke", "regression", "sanity", "integration", "security", "performance");
        if (finalType != null) map.put("TAG_TYPE", finalType);
        
        // PHASE: scenario overrides feature
        String finalPhase = extractTag(scenario.getTags(), "UAT", "SIT");
        if (finalPhase == null) finalPhase = extractTag(featureTags, "UAT", "SIT");
        if (finalPhase != null) map.put("TAG_PHASE", finalPhase);
        
        // ENVIRONMENT: scenario overrides feature
        String finalEnvironment = extractTag(scenario.getTags(), "dev", "staging", "prod", "pre-prod");
        if (finalEnvironment == null) finalEnvironment = extractTag(featureTags, "dev", "staging", "prod", "pre-prod");
        if (finalEnvironment != null) map.put("TAG_ENVIRONMENT", finalEnvironment);
        
        // EXECUTE: scenario overrides feature
        String finalExecute = extractTag(scenario.getTags(), "manual", "automation", "combine");
        if (finalExecute == null) finalExecute = extractTag(featureTags, "manual", "automation", "combine");
        if (finalExecute != null) map.put("TAG_EXECUTE", finalExecute);
        
        // VALID: scenario overrides feature
        String finalValid = extractTag(scenario.getTags(), "positive", "negative");
        if (finalValid == null) finalValid = extractTag(featureTags, "positive", "negative");
        if (finalValid != null) map.put("TAG_VALID", finalValid);

        // For TAG_1 and TAG_2, use scenario tags first, then fall back to feature tags
        // Exclude all categorized tags
        List<String> scenarioOtherTags = new ArrayList<>();
        for (Tag tag : scenario.getTags()) {
            String tagName = tag.getName().replace("@", "");
            String lowerTagName = tagName.toLowerCase();
            if (!lowerTagName.equals("api") && !lowerTagName.equals("web") && 
                !lowerTagName.equals("android") && !lowerTagName.equals("ios") && 
                !lowerTagName.equals("desktop") && !lowerTagName.equals("table") && 
                !lowerTagName.equals("phone") && 
                !lowerTagName.equals("p0") && !lowerTagName.equals("p1") && 
                !lowerTagName.equals("p2") && !lowerTagName.equals("p3") && 
                !lowerTagName.equals("s1") && !lowerTagName.equals("s2") && 
                !lowerTagName.equals("s3") && !lowerTagName.equals("s4") && 
                !lowerTagName.equals("smoke") && !lowerTagName.equals("regression") && 
                !lowerTagName.equals("sanity") && !lowerTagName.equals("integration") && 
                !lowerTagName.equals("security") && !lowerTagName.equals("performance") && 
                !lowerTagName.equals("uat") && !lowerTagName.equals("sit") && 
                !lowerTagName.equals("dev") && !lowerTagName.equals("staging") && 
                !lowerTagName.equals("prod") && !lowerTagName.equals("pre-prod") && 
                !lowerTagName.equals("manual") && !lowerTagName.equals("automation") && 
                !lowerTagName.equals("combine") && 
                !lowerTagName.equals("positive") && !lowerTagName.equals("negative")) {
                scenarioOtherTags.add(tagName);
            }
        }
        
        List<String> finalOtherTags = new ArrayList<>();
        if (!scenarioOtherTags.isEmpty()) {
            finalOtherTags.addAll(scenarioOtherTags);
        } else {
            for (Tag tag : featureTags) {
                String tagName = tag.getName().replace("@", "");
                String lowerTagName = tagName.toLowerCase();
                if (!lowerTagName.equals("api") && !lowerTagName.equals("web") && 
                    !lowerTagName.equals("android") && !lowerTagName.equals("ios") && 
                    !lowerTagName.equals("desktop") && !lowerTagName.equals("table") && 
                    !lowerTagName.equals("phone") && 
                    !lowerTagName.equals("p0") && !lowerTagName.equals("p1") && 
                    !lowerTagName.equals("p2") && !lowerTagName.equals("p3") && 
                    !lowerTagName.equals("s1") && !lowerTagName.equals("s2") && 
                    !lowerTagName.equals("s3") && !lowerTagName.equals("s4") && 
                    !lowerTagName.equals("smoke") && !lowerTagName.equals("regression") && 
                    !lowerTagName.equals("sanity") && !lowerTagName.equals("integration") && 
                    !lowerTagName.equals("security") && !lowerTagName.equals("performance") && 
                    !lowerTagName.equals("uat") && !lowerTagName.equals("sit") && 
                    !lowerTagName.equals("dev") && !lowerTagName.equals("staging") && 
                    !lowerTagName.equals("prod") && !lowerTagName.equals("pre-prod") && 
                    !lowerTagName.equals("manual") && !lowerTagName.equals("automation") && 
                    !lowerTagName.equals("combine") && 
                    !lowerTagName.equals("positive") && !lowerTagName.equals("negative")) {
                    finalOtherTags.add(tagName);
                }
            }
        }
        
        if (!finalOtherTags.isEmpty()) map.put("TAG_1", finalOtherTags.get(0));
        if (finalOtherTags.size() > 1) map.put("TAG_2", finalOtherTags.get(1));

        return map;
    }

    private String extractTag(List<Tag> tags, String... keywords) {
        for (Tag tag : tags) {
            String name = tag.getName().replace("@", "");
            for (String kw : keywords) {
                if (name.equalsIgnoreCase(kw)) return name;
            }
        }
        return null;
    }
}