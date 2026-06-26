package com.krai.cli;

import com.krai.adapter.input.GherkinAstAdapter;
import com.krai.adapter.output.FastExcelAdapter;
import com.krai.core.engine.ReflectionMapper;
import com.krai.core.model.TestCaseRecord;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "krai", mixinStandardHelpOptions = true, version = "1.0",
        description = "🥒 Transforms Gherkin .feature files into structured Excel reports.")
public class KraiApp implements Callable<Integer> {

    @Option(names = {"-i", "--input"}, description = "Path to the .feature file", required = true)
    private Path inputPath;

    @Option(names = {"-o", "--output"}, description = "Path for the output .xlsx file", required = true)
    private Path outputPath;

    @Override
    public Integer call() throws Exception {
        System.out.println("🥒 Krai Engine Starting...");

        // 1. Initialize Components
        GherkinAstAdapter reader = new GherkinAstAdapter();
        ReflectionMapper mapper = new ReflectionMapper();
        FastExcelAdapter exporter = new FastExcelAdapter();

        // 2. Read & Flatten (Adapter Input)
        System.out.println("📖 Parsing AST from: " + inputPath.toAbsolutePath());
        List<Map<String, Object>> flattenedData = reader.readAndFlatten(inputPath);
        System.out.println("🔍 Found " + flattenedData.size() + " scenarios.");

        // 3. Map to POJO (Core Engine)
        List<TestCaseRecord> records = new ArrayList<>();
        for (Map<String, Object> data : flattenedData) {
            TestCaseRecord record = new TestCaseRecord();
            // Memanggil method mapData yang menerima Map<String, Object>
            mapper.mapData(record, data);
            records.add(record);
        }

        // 4. Export to Excel (Adapter Output)
        System.out.println("📊 Generating Excel...");
        exporter.exportTestCases(records, outputPath);

        System.out.println("🎉 Done! Open your file at: " + outputPath.toAbsolutePath());
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new KraiApp()).execute(args);
        System.exit(exitCode);
    }
}