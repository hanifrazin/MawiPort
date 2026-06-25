package com.picklement.core.engine;

import com.picklement.core.annotation.GherkinMap;
import com.picklement.core.model.TestCaseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Core Engine untuk memetakan data Gherkin (yang sudah di-flatten menjadi Map)
 * ke dalam POJO TestCaseRecord menggunakan Reflection dan Cache O(1).
 */
public final class ReflectionMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionMapper.class);

    // Cache O(1): sourceKey (dari annotation) -> Target Field
    private final Map<String, Field> sourceKeyToTargetField;

    public ReflectionMapper() {
        // Inisialisasi cache hanya sekali saat aplikasi startup (Sangat Cepat!)
        this.sourceKeyToTargetField = Collections.unmodifiableMap(initializeSourceKeyMapping());
    }

    /**
     * Memetakan data dari Map ke TestCaseRecord.
     * @param target POJO yang akan diisi datanya.
     * @param gherkinData Data hasil flatten dari Parser (Key = sourceKey, Value = data).
     */
    public void mapData(TestCaseRecord target, Map<String, Object> gherkinData) {
        if (target == null || gherkinData == null) return;

        // Looping berdasarkan Cache (Bukan looping field AST yang rumit)
        for (Map.Entry<String, Field> entry : sourceKeyToTargetField.entrySet()) {
            String sourceKey = entry.getKey();
            Field targetField = entry.getValue();

            Object value = gherkinData.get(sourceKey);

            if (value != null) {
                try {
                    targetField.setAccessible(true);
                    targetField.set(target, convertIfNecessary(value, targetField.getType()));
                } catch (IllegalAccessException e) {
                    LOG.error("Failed to set field '{}' with sourceKey '{}'", targetField.getName(), sourceKey, e);
                }
            } else if (isRequired(targetField)) {
                // Fitur Required dari Qwen kita gunakan di sini!
                LOG.warn("⚠️ Required Gherkin property '{}' is missing or null for TC_ID: {}",
                        sourceKey, target.getTcId());
            }
        }
    }

    private Map<String, Field> initializeSourceKeyMapping() {
        Map<String, Field> map = new HashMap<>();
        for (Field f : TestCaseRecord.class.getDeclaredFields()) {
            GherkinMap ann = f.getAnnotation(GherkinMap.class);
            if (ann != null) {
                map.put(ann.sourceKey(), f);
            }
        }
        return map;
    }

    private boolean isRequired(Field targetField) {
        GherkinMap ann = targetField.getAnnotation(GherkinMap.class);
        return ann != null && ann.required();
    }

    private Object convertIfNecessary(Object value, Class<?> targetType) {
        if (targetType.isAssignableFrom(value.getClass())) return value;
        if (value instanceof Enum<?> e) return e.name();
        if (value instanceof Number n) return n.toString();
        return value.toString();
    }
}