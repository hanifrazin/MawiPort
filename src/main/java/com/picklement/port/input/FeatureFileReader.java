package com.picklement.port.input;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface FeatureFileReader {
    List<Map<String, Object>> readAndFlatten(Path path) throws IOException;
}