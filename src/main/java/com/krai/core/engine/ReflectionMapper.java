package com.krai.core.engine;

import com.krai.core.annotation.GherkinMap;
import com.krai.core.model.TestCaseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionMapper {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionMapper.class);
    private final Map<String, Field> sourceKeyToTargetField;

    public ReflectionMapper() {
        this.sourceKeyToTargetField = Collections.unmodifiableMap(initializeSourceKeyMapping());
    }

    public void mapData(TestCaseRecord target, Map<String, Object> gherkinData) {
        if (target == null || gherkinData == null) return;
        for (Map.Entry<String, Field> entry : sourceKeyToTargetField.entrySet()) {
            String sourceKey = entry.getKey();
            Field targetField = entry.getValue();
            Object value = gherkinData.get(sourceKey);

            if (value != null && !value.toString().trim().isEmpty()) {
                try {
                    targetField.setAccessible(true);
                    targetField.set(target, value.toString());
                } catch (IllegalAccessException e) {
                    LOG.error("Failed to set field '{}'", targetField.getName(), e);
                }
            } else if (isRequired(targetField)) {
                LOG.warn("⚠️ Required property '{}' is missing for TC: {}", sourceKey, target.getTcId());
            }
        }
    }

    private Map<String, Field> initializeSourceKeyMapping() {
        Map<String, Field> map = new HashMap<>();
        for (Field f : TestCaseRecord.class.getDeclaredFields()) {
            GherkinMap ann = f.getAnnotation(GherkinMap.class);
            if (ann != null) map.put(ann.sourceKey(), f);
        }
        return map;
    }

    private boolean isRequired(Field f) {
        GherkinMap ann = f.getAnnotation(GherkinMap.class);
        return ann != null && ann.required();
    }
}