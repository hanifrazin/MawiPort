package com.mawiport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String CONFIG_FILE_NAME = "mawiport.config.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public AppConfig load() throws IOException {
        Path configPath = Paths.get(CONFIG_FILE_NAME);

        if (!Files.exists(configPath)) {
            LOG.warn("⚠️ {} not found in current directory. Using fallback defaults.", CONFIG_FILE_NAME);
            return createDefaultConfig();
        }

        LOG.info("⚙️ Loading configuration from: {}", configPath.toAbsolutePath());
        return mapper.readValue(configPath.toFile(), AppConfig.class);
    }

    public AppConfig createDefaultConfig() {
        return new AppConfig(
            new AppConfig.ExcelMetadata("QA Team", "Picklement Report"),
            new AppConfig.TcIdStrategy("TC", 3),
            new AppConfig.TagRouting(
                java.util.List.of("API", "web", "android", "ios", "desktop", "table", "phone"),
                java.util.List.of("P0", "P1", "P2", "P3"),
                java.util.List.of("S1", "S2", "S3", "S4"),
                java.util.List.of("smoke", "regression", "sanity", "integration", "security", "performance"),
                java.util.List.of("UAT", "SIT"),
                java.util.List.of("dev", "staging", "prod", "pre-prod"),
                java.util.List.of("manual", "automation", "combine"),
                java.util.List.of("positive", "negative")
            ),
            new AppConfig.StepKeywords(
                java.util.List.of("Given"),
                java.util.List.of("When"),
                java.util.List.of("Then")
            ),
            new AppConfig.ExcelConfig(
                java.util.List.of(
                    new AppConfig.ExcelColumnGroup("Identitas", "(TC_ID|FEATURE_NAME|SCENARIO_NAME)", "center"),
                    new AppConfig.ExcelColumnGroup("Tag Prioritas", "(TAG_PLATFORM|TAG_PRIORITY|TAG_SEVERITY|TAG_TYPE)", "right"),
                    new AppConfig.ExcelColumnGroup("Steps", "(GIVEN_STEPS|WHEN_STEPS|THEN_STEPS)", "right"),
                    new AppConfig.ExcelColumnGroup("Actual Result", "ACTUAL_RESULT", "right")
                )
            )
        );
    }
}