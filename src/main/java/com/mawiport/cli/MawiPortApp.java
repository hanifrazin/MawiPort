package com.mawiport.cli;

import com.mawiport.adapter.input.GherkinAstAdapter;
import com.mawiport.adapter.output.ApachePoiExcelAdapter;
import com.mawiport.core.engine.ReflectionMapper;
import com.mawiport.core.model.TestCaseRecord;
import java.io.IOException;
import java.lang.reflect.Field;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "mawiport", mixinStandardHelpOptions = true, version = "1.0",
        description = "🥒 Transforms Gherkin .feature files into structured Excel reports.")
public class MawiPortApp implements Callable<Integer> {

    @Option(names = {"-i", "--input"}, description = "Path to the .feature file", required = true)
    private Path inputPath;

    @Option(names = {"-o", "--output"}, description = "Path for the output .xlsx file", required = true)
    private Path outputPath;

    @Override
    public Integer call() throws Exception {
        System.out.println("🥒 MawiPort Engine Starting...");

        // Validate input file exists and prevent path traversal
        if (!Files.exists(inputPath)) {
            System.err.println("Error: Input file does not exist: " + inputPath.toAbsolutePath());
            return 1;
        }
        
        // Validate input file exists
        if (!Files.exists(inputPath)) {
            System.err.println("Error: Input file does not exist: " + inputPath.toAbsolutePath());
            return 1;
        }

        // Validate output directory exists
        if (outputPath.getParent() != null && !Files.exists(outputPath.getParent())) {
            System.err.println("Error: Output directory does not exist: " + outputPath.getParent());
            return 1;
        }
        
        // Debug: Print path information
        System.out.println("DEBUG: Input path: " + inputPath);
        System.out.println("DEBUG: Output path: " + outputPath);
        System.out.println("DEBUG: Output parent: " + outputPath.getParent());
        System.out.println("DEBUG: Output parent exists: " + Files.exists(outputPath.getParent()));

        try {
            // 1. Initialize Components
            GherkinAstAdapter reader = new GherkinAstAdapter();
            ReflectionMapper mapper = new ReflectionMapper(TestCaseRecord.class);
            ApachePoiExcelAdapter exporter = new ApachePoiExcelAdapter();

            // 2. Read & Flatten (Adapter Input)
            System.out.println("📖 Parsing AST from: " + inputPath.toAbsolutePath());
            List<Map<String, Object>> flattenedData = reader.readAndFlatten(inputPath);
            System.out.println("🔍 Found " + flattenedData.size() + " scenarios.");

            // 3. Map to POJO (Core Engine)
            List<TestCaseRecord> records = new ArrayList<>();
            for (Map<String, Object> data : flattenedData) {
                TestCaseRecord record = new TestCaseRecord();
                try {
                    mapper.mapData(record, data);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to map data to TestCaseRecord", e);
                }
                records.add(record);
            }

            // 4. Export to Excel (Adapter Output)
            System.out.println("📊 Generating Excel...");
            exporter.exportToExcel(records, outputPath.toString());

            System.out.println("🎉 Done! Open your file at: " + outputPath.toAbsolutePath());
            return 0;
        } catch (Exception e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        int exitCode = new CommandLine(new MawiPortApp()).execute(args);
        System.exit(exitCode);
    }
}