package com.mawiport.core.engine;

import com.mawiport.core.annotation.GherkinMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectionMapper {
    private final Map<String, Field> fieldMap;

    public ReflectionMapper(Class<?> clazz) {
        this.fieldMap = createFieldMap(clazz);
    }

    private Map<String, Field> createFieldMap(Class<?> clazz) {
        Map<String, Field> map = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Security: Only make accessible if it's a safe operation
            if (Modifier.isPrivate(field.getModifiers())) {
                try {
                    field.setAccessible(true); // Make private fields accessible
                } catch (SecurityException e) {
                    System.err.println("Warning: Cannot access private field " + field.getName() + ": " + e.getMessage());
                    continue; // Skip this field if access is denied
                }
            }
            map.put(field.getName(), field);
        }

        return map;
    }

    public Map<String, Field> getFieldMap() {
        return new LinkedHashMap<>(fieldMap); // Return a copy to maintain immutability
    }

    public void mapData(Object target, Map<String, Object> data) throws IllegalAccessException {
        Map<String, Field> fieldMap = getFieldMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            
            // Check for GherkinMap annotation
            GherkinMap annotation = field.getAnnotation(GherkinMap.class);
            String sourceKey = field.getName(); // Default to field name
            
            if (annotation != null && !annotation.sourceKey().isEmpty()) {
                sourceKey = annotation.sourceKey();
            }
            
            if (data.containsKey(sourceKey)) {
                Object value = data.get(sourceKey);
                if (value != null) {
                    field.set(target, value.toString()); // Convert to String to be safe
                }
            }
        }
    }
}