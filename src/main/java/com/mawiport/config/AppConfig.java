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
        @JsonProperty("stepKeywords") StepKeywords stepKeywords,
        @JsonProperty("excelConfig") ExcelConfig excelConfig
) {
    public record ExcelMetadata(String author, String defaultSheetName) {}
    public record TcIdStrategy(String prefix, int padding) {}
    public record TagRouting(
            @JsonProperty("platformKeywords") List<String> platformKeywords,
            @JsonProperty("priorityKeywords") List<String> priorityKeywords,
            @JsonProperty("severityKeywords") List<String> severityKeywords,
            @JsonProperty("typeKeywords") List<String> typeKeywords,
            @JsonProperty("phaseKeywords") List<String> phaseKeywords,
            @JsonProperty("environmentKeywords") List<String> environmentKeywords,
            @JsonProperty("executeKeywords") List<String> executeKeywords,
            @JsonProperty("validCaseKeywords") List<String> validCaseKeywords
    ) {}
    public record StepKeywords(List<String> given, List<String> when, List<String> then) {}

    public record ExcelConfig(
        List<ExcelColumnGroup> columnGroups
    ) {}

    public record ExcelColumnGroup(
        String name,
        String pattern,
        String position
    ) {}
}