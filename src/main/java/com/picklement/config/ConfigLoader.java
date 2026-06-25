package com.picklement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String CONFIG_FILE_NAME = "picklement.config.json";
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

    private AppConfig createDefaultConfig() {
        // Fallback jika user menghapus file JSON-nya
        return new AppConfig(
                new AppConfig.ExcelMetadata("QA Team", "Picklement Report"),
                new AppConfig.TcIdStrategy("TC", 3),
                new AppConfig.TagRouting(
                        java.util.List.of("API", "WEB", "MOBILE"),
                        java.util.List.of("P0", "P1", "HIGH", "MEDIUM")
                ),
                new AppConfig.StepKeywords(
                        java.util.List.of("Given"),
                        java.util.List.of("When"),
                        java.util.List.of("Then")
                )
        );
    }
}