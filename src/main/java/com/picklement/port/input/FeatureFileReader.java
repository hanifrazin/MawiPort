package com.picklement.port.input;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Port (input) untuk membaca dan meratakan (flatten) file Gherkin.
 * Adapter bertanggung jawab membedah AST Cucumber dan mengembalikannya
 * sebagai Map universal agar Core Engine tidak tercemari library eksternal.
 */
public interface FeatureFileReader {

    /**
     * Membaca file .feature, mem-parse AST-nya, dan meratakannya menjadi
     * List of Maps. Kunci Map harus sesuai dengan 'sourceKey' di @GherkinMap.
     *
     * @param path lokasi file .feature
     * @return List data yang sudah di-flatten, siap masuk ke ReflectionMapper
     * @throws IOException jika file tidak ditemukan atau gagal dibaca
     */
    List<Map<String, Object>> readAndFlatten(Path path) throws IOException;
}