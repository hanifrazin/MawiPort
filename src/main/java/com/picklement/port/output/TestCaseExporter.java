package com.picklement.port.output;

import com.picklement.core.model.TestCaseRecord;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Port (output) untuk mengekspor data ke Excel.
 * Menggunakan Iterable untuk menjamin Streaming / O(1) Memory Footprint.
 */
public interface TestCaseExporter {

    /**
     * Menulis records ke file Excel menggunakan metode Streaming.
     *
     * @param records Iterable (bisa berupa List, Stream, atau Generator)
     *                untuk mencegah OutOfMemory pada data raksasa.
     * @param target  lokasi file .xlsx tujuan
     * @throws IOException jika gagal menulis ke disk
     */
    void exportTestCases(Iterable<TestCaseRecord> records, Path target) throws IOException;
}