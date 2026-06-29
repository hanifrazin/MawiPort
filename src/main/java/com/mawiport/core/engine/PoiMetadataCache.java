package com.mawiport.core.engine;

import com.mawiport.core.annotation.GherkinMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PoiMetadataCache {
    private final List<PoiFieldMetadata> metadataList = new ArrayList<>();

    public record PoiFieldMetadata(
        int columnIndex,
        String headerName,
        int width,
        Field field
    ) {}

    public void initialize(Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(GherkinMap.class)) {
                GherkinMap annotation = field.getAnnotation(GherkinMap.class);
                field.setAccessible(true);

                metadataList.add(new PoiFieldMetadata(
                    annotation.order(),
                    annotation.headerName(),
                    annotation.width(),
                    field
                ));
            }
        }

        // Sort by column index
        metadataList.sort((a, b) -> Integer.compare(a.columnIndex(), b.columnIndex()));
    }

    public List<PoiFieldMetadata> getMetadata() {
        return new ArrayList<>(metadataList);
    }
}