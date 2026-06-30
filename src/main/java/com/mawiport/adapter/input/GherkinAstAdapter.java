package com.mawiport.adapter.input;

import com.mawiport.config.AppConfig;
import com.mawiport.config.ConfigLoader;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adapter Input: Reads a .feature file using Cucumber Gherkin v28.0.0
 * and flattens it into a Map<String, Object> for the Core Engine.
 */
public class GherkinAstAdapter implements FeatureFileReader {

    private static final Logger LOG = LoggerFactory.getLogger(GherkinAstAdapter.class);
    private final AppConfig config;

    public GherkinAstAdapter(AppConfig config) {
        this.config = config;
    }

    public GherkinAstAdapter() {
        this(new ConfigLoader().createDefaultConfig());
    }

    @Override
    public List<Map<String, Object>> readAndFlatten(Path path) throws IOException {
        AtomicInteger idCounter = new AtomicInteger(0);
        IdGenerator idGenerator = () -> String.valueOf(idCounter.incrementAndGet());

        GherkinParser parser = GherkinParser.builder()
                .includeSource(false)
                .includePickles(false)
                .includeGherkinDocument(true)
                .idGenerator(idGenerator)
                .build();

        try (Stream<Envelope> envelopes = parser.parse(path)) {
            GherkinDocument document = envelopes
                    .map(Envelope::getGherkinDocument)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElseThrow(() -> new IOException("Failed to parse Gherkin document from " + path));

            return flattenGherkinDocument(document);
        }
    }

    private List<Map<String, Object>> flattenGherkinDocument(GherkinDocument document) {
        List<Map<String, Object>> flattenedData = new ArrayList<>();
        Feature feature = document.getFeature().orElse(null);
        if (feature == null) {
            LOG.warn("No feature found in Gherkin document");
            return flattenedData;
        }

        String featureName = feature.getName();
        List<Tag> featureTags = feature.getTags();
        AtomicInteger tcCounter = new AtomicInteger(1);

        feature.getChildren().forEach(child -> {
            if (child.getScenario().isPresent()) {
                flattenedData.add(flattenScenario(child.getScenario().get(), featureName, "", featureTags, tcCounter));
            } else if (child.getRule().isPresent()) {
                Rule rule = child.getRule().get();
                String ruleName = rule.getName();
                rule.getChildren().forEach(ruleChild -> {
                    if (ruleChild.getScenario().isPresent()) {
                        flattenedData.add(flattenScenario(ruleChild.getScenario().get(), featureName, ruleName, featureTags, tcCounter));
                    }
                });
            }
        });

        return flattenedData;
    }

    private Map<String, Object> flattenScenario(Scenario scenario, String featureName, String ruleName, List<Tag> featureTags, AtomicInteger counter) {
        Map<String, Object> map = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

        map.put("TC_ID", String.format("TC_%03d", counter.getAndIncrement()));
        map.put("FEATURE_NAME", featureName);
        map.put("RULE_NAME", ruleName);
        map.put("SCENARIO_NAME", scenario.getName());

        // Categorize Steps and Extract Test Data
        processSteps(scenario.getSteps(), map);

        // Extract and process all tags dynamically
        processTags(scenario.getTags(), featureTags, map);

        return map;
    }

    private void processSteps(List<Step> steps, Map<String, Object> map) {
        List<String> givens = new ArrayList<>();
        List<String> whens = new ArrayList<>();
        List<String> thens = new ArrayList<>();
        List<String> testData = new ArrayList<>();
        final String[] currentContext = {"GIVEN"}; // Use an array to be effectively final
        
        // Track step numbering for each context
        int[] stepCounters = {1, 1, 1}; // [GIVEN, WHEN, THEN]
        boolean[] needsNumbering = {false, false, false}; // Track if numbering is needed

        steps.forEach(step -> {
            String keyword = step.getKeyword().trim();
            String text = step.getText();
            String formattedText = text;

            // Determine if this step needs numbering
            boolean shouldNumber = keyword.equalsIgnoreCase("Given") || 
                                  keyword.equalsIgnoreCase("When") || 
                                  keyword.equalsIgnoreCase("Then") ||
                                  keyword.equalsIgnoreCase("And") || 
                                  keyword.equalsIgnoreCase("But") ||
                                  keyword.equalsIgnoreCase("*");

            if (keyword.equalsIgnoreCase("Given")) {
                currentContext[0] = "GIVEN";
                stepCounters[0] = 1; // Reset counter for new GIVEN section
                needsNumbering[0] = true;
            } else if (keyword.equalsIgnoreCase("When")) {
                currentContext[0] = "WHEN";
                stepCounters[1] = 1; // Reset counter for new WHEN section
                needsNumbering[1] = true;
            } else if (keyword.equalsIgnoreCase("Then")) {
                currentContext[0] = "THEN";
                stepCounters[2] = 1; // Reset counter for new THEN section
                needsNumbering[2] = true;
            } else if (keyword.equalsIgnoreCase("And") || 
                      keyword.equalsIgnoreCase("But") ||
                      keyword.equalsIgnoreCase("*")) {
                // Continue numbering from previous step
                shouldNumber = true;
            }

            // Add numbering if needed
            if (shouldNumber) {
                int counterIndex = 0;
                if (currentContext[0].equals("WHEN")) counterIndex = 1;
                else if (currentContext[0].equals("THEN")) counterIndex = 2;
                
                formattedText = stepCounters[counterIndex] + ". " + text;
                stepCounters[counterIndex]++;
            }

            switch (currentContext[0]) {
                case "GIVEN" -> givens.add(formattedText);
                case "WHEN" -> whens.add(formattedText);
                case "THEN" -> thens.add(formattedText);
            }

            // Extract Test Data (DocString, DataTable, Quotes)
            step.getDocString().ifPresent(ds -> testData.add(ds.getContent()));
            step.getDataTable().ifPresent(dt -> dt.getRows().stream()
                    .map(row -> row.getCells().stream().map(TableCell::getValue).collect(Collectors.joining(" | ")))
                    .forEach(testData::add));

            if (text.contains("\"")) {
                String[] parts = text.split("\"");
                for (int i = 1; i < parts.length; i += 2) {
                    testData.add(parts[i]);
                }
            }
        });

        map.put("GIVEN_STEPS", String.join("\n", givens));
        map.put("WHEN_STEPS", String.join("\n", whens));
        map.put("THEN_STEPS", String.join("\n", thens));
        
        // Add numbering to test data if there are multiple items
        if (testData.size() > 1) {
            List<String> numberedTestData = new ArrayList<>();
            for (int i = 0; i < testData.size(); i++) {
                numberedTestData.add((i + 1) + ". " + testData.get(i));
            }
            map.put("TEST_DATA", String.join("\n", numberedTestData));
        } else {
            map.put("TEST_DATA", testData.isEmpty() ? "" : testData.get(0));
        }
    }

    private void processTags(List<Tag> scenarioTags, List<Tag> featureTags, Map<String, Object> map) {
        AppConfig.TagRouting tagRouting = config.tagRouting();
        Set<String> usedScenarioTags = new HashSet<>();
        Set<String> usedFeatureTags = new HashSet<>();
        Map<String, String> customTags = new HashMap<>();

        // Define the mapping from config method to map key
        Map<Function<AppConfig.TagRouting, List<String>>, String> tagConfigMap = new LinkedHashMap<>();
        tagConfigMap.put(AppConfig.TagRouting::platformKeywords, "TAG_PLATFORM");
        tagConfigMap.put(AppConfig.TagRouting::priorityKeywords, "TAG_PRIORITY");
        tagConfigMap.put(AppConfig.TagRouting::severityKeywords, "TAG_SEVERITY");
        tagConfigMap.put(AppConfig.TagRouting::typeKeywords, "TAG_TYPE");
        tagConfigMap.put(AppConfig.TagRouting::phaseKeywords, "TAG_PHASE");
        tagConfigMap.put(AppConfig.TagRouting::environmentKeywords, "TAG_ENVIRONMENT");
        tagConfigMap.put(AppConfig.TagRouting::executeKeywords, "TAG_EXECUTE");
        tagConfigMap.put(AppConfig.TagRouting::validCaseKeywords, "TAG_VALID");

        // Process configured tags with scenario-over-feature logic
        tagConfigMap.forEach((keywordsGetter, mapKey) -> {
            List<String> keywords = keywordsGetter.apply(tagRouting);
            extractTag(scenarioTags, keywords, usedScenarioTags)
                    .or(() -> extractTag(featureTags, keywords, usedFeatureTags))
                    .ifPresent(tagValue -> map.put(mapKey, tagValue));
        });

        // Proses remaining "other" tags
        List<String> otherTags = new ArrayList<>();
        otherTags.addAll(getUnusedTags(scenarioTags, usedScenarioTags));
        otherTags.addAll(getUnusedTags(featureTags, usedFeatureTags));

        // Store custom tags in the map for dynamic access
        for (int i = 0; i < otherTags.size(); i++) {
            customTags.put("TAG_" + (i + 1), otherTags.get(i));
        }
        
        map.put("CUSTOM_TAGS", customTags);
    }

    private Optional<String> extractTag(List<Tag> tags, List<String> keywords, Set<String> usedTags) {
        for (Tag tag : tags) {
            String tagName = tag.getName().replace("@", "");
            if (usedTags.contains(tag.getName())) {
                continue; // Skip already processed tags
            }
            for (String kw : keywords) {
                if (tagName.equalsIgnoreCase(kw)) {
                    usedTags.add(tag.getName());
                    return Optional.of(tagName);
                }
            }
        }
        return Optional.empty();
    }

    private List<String> getUnusedTags(List<Tag> tags, Set<String> usedTags) {
        return tags.stream()
                .filter(tag -> !usedTags.contains(tag.getName()))
                .map(tag -> tag.getName().replace("@", ""))
                .collect(Collectors.toList());
    }
}