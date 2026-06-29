package com.mawiport.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Java 21 Record untuk memetakan picklement.config.json.
 * Immutable dan Thread-Safe.
 */
public record AppConfig(
        @JsonProperty("excelMetadata") ExcelMetadata excelMetadata,
        @JsonProperty("tcIdStrategy") TcIdStrategy tcIdStrategy,
        @JsonProperty("tagRouting") TagRouting tagRouting,
        @JsonProperty("stepKeywords") StepKeywords stepKeywords
) {
    public record ExcelMetadata(String author, String defaultSheetName) {}
    public record TcIdStrategy(String prefix, int padding) {}
    public record TagRouting(
            List<String> platformKeywords,
            List<String> priorityKeywords,
            List<String> severityKeywords,
            List<String> typeKeywords,
            List<String> phaseKeywords,
            List<String> environmentKeywords,
            List<String> executeKeywords,
            List<String> validCaseKeywords
    ) {}
    public record StepKeywords(List<String> given, List<String> when, List<String> then) {}
}