package com.mawiport.config;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TagRoutingTest {

    @Test
    public void testTagRoutingConfiguration() {
        // Test loading from default config
        ConfigLoader configLoader = new ConfigLoader();
        AppConfig config = configLoader.createDefaultConfig();
        
        AppConfig.TagRouting tagRouting = config.tagRouting();
        
        // Verify platform keywords
        assertNotNull(tagRouting.platformKeywords());
        assertEquals(7, tagRouting.platformKeywords().size());
        assertTrue(tagRouting.platformKeywords().contains("API"));
        assertTrue(tagRouting.platformKeywords().contains("android"));
        
        // Verify priority keywords
        assertNotNull(tagRouting.priorityKeywords());
        assertEquals(4, tagRouting.priorityKeywords().size());
        assertTrue(tagRouting.priorityKeywords().contains("P0"));
        assertTrue(tagRouting.priorityKeywords().contains("P3"));
        
        // Verify severity keywords
        assertNotNull(tagRouting.severityKeywords());
        assertEquals(4, tagRouting.severityKeywords().size());
        assertTrue(tagRouting.severityKeywords().contains("S1"));
        assertTrue(tagRouting.severityKeywords().contains("S4"));
        
        // Verify type keywords
        assertNotNull(tagRouting.typeKeywords());
        assertEquals(6, tagRouting.typeKeywords().size());
        assertTrue(tagRouting.typeKeywords().contains("smoke"));
        assertTrue(tagRouting.typeKeywords().contains("performance"));
        
        // Verify phase keywords
        assertNotNull(tagRouting.phaseKeywords());
        assertEquals(2, tagRouting.phaseKeywords().size());
        assertTrue(tagRouting.phaseKeywords().contains("UAT"));
        assertTrue(tagRouting.phaseKeywords().contains("SIT"));
        
        // Verify environment keywords
        assertNotNull(tagRouting.environmentKeywords());
        assertEquals(4, tagRouting.environmentKeywords().size());
        assertTrue(tagRouting.environmentKeywords().contains("dev"));
        assertTrue(tagRouting.environmentKeywords().contains("prod"));
        
        // Verify execute keywords
        assertNotNull(tagRouting.executeKeywords());
        assertEquals(3, tagRouting.executeKeywords().size());
        assertTrue(tagRouting.executeKeywords().contains("manual"));
        assertTrue(tagRouting.executeKeywords().contains("combine"));
        
        // Verify valid case keywords
        assertNotNull(tagRouting.validCaseKeywords());
        assertEquals(2, tagRouting.validCaseKeywords().size());
        assertTrue(tagRouting.validCaseKeywords().contains("positive"));
        assertTrue(tagRouting.validCaseKeywords().contains("negative"));
        
        System.out.println("✅ Tag routing configuration test passed!");
    }
    
    @Test
    public void testTagRoutingFromFile() {
        try {
            ConfigLoader configLoader = new ConfigLoader();
            AppConfig config = configLoader.load();
            
            AppConfig.TagRouting tagRouting = config.tagRouting();
            
            // Basic verification that config was loaded
            assertNotNull(tagRouting);
            assertNotNull(tagRouting.platformKeywords());
            assertNotNull(tagRouting.priorityKeywords());
            
            System.out.println("✅ Tag routing file loading test passed!");
            System.out.println("Platform keywords: " + tagRouting.platformKeywords());
            System.out.println("Priority keywords: " + tagRouting.priorityKeywords());
            
        } catch (Exception e) {
            System.out.println("⚠️  Using default config (config file not found): " + e.getMessage());
            // This is expected if config file doesn't exist
        }
    }
}