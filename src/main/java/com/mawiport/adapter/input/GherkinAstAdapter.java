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

        // 6. Extract Tags
        List<Tag> allTags = new ArrayList<>(featureTags);
        allTags.addAll(scenario.getTags());

        map.put("TAG_TYPE", extractTag(allTags, "API", "WEB", "MOBILE"));
        map.put("TAG_PRIORITY", extractTag(allTags, "P0", "P1", "HIGH", "MEDIUM", "LOW"));

        List<String> otherTags = allTags.stream()
                .map(t -> t.getName().replace("@", ""))
                .filter(t -> !t.equalsIgnoreCase("API") && !t.equalsIgnoreCase("WEB") && !t.equalsIgnoreCase("P0") && !t.equalsIgnoreCase("P1"))
                .toList();

        if (!otherTags.isEmpty()) map.put("TAG_1", otherTags.get(0));
        if (otherTags.size() > 1) map.put("TAG_2", otherTags.get(1));

        return map;
    }

    private String extractTag(List<Tag> tags, String... keywords) {
        for (Tag tag : tags) {
            String name = tag.getName().replace("@", "").toUpperCase();
            for (String kw : keywords) {
                if (name.contains(kw)) return name;
            }
        }
        return "";
    }
}