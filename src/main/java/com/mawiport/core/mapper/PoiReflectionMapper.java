package com.mawiport.core.mapper;

import com.mawiport.core.annotation.GherkinMap;
import com.mawiport.core.model.TestCaseRecord;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PoiReflectionMapper {
    private static final Map<Integer, Field> FIELD_MAPPING = new ConcurrentHashMap<>();

    static {
        // Initialize mapping on class load
        initializeFieldMapping();
    }

    private static void initializeFieldMapping() {
        var testCaseClass = TestCaseRecord.class;
        var fields = testCaseClass.getDeclaredFields();

        for (var field : fields) {
            var annotation = field.getAnnotation(GherkinMap.class);
            if (annotation != null) {
                var order = annotation.order();
                if (order >= 0) {
                    FIELD_MAPPING.put(order, field);
                    field.setAccessible(true); // Enable access to private fields
                }
            }
        }
    }

    public static Field getFieldByOrder(int order) {
        return FIELD_MAPPING.get(order);
    }

    public static Object getFieldValue(Object instance, int order) throws IllegalAccessException {
        var field = getFieldByOrder(order);
        if (field == null) {
            throw new IllegalArgumentException("No field found for order: " + order);
        }
        return field.get(instance);
    }

    public static Map<Integer, Field> getFieldMapping() {
        return new ConcurrentHashMap<>(FIELD_MAPPING);
    }
}