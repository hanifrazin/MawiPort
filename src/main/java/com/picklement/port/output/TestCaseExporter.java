package com.picklement.port.output;

import com.picklement.core.model.TestCaseRecord;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface TestCaseExporter {
    // Kontrak final: Menggunakan List<TestCaseRecord>
    void exportTestCases(List<TestCaseRecord> records, Path target) throws IOException;
}