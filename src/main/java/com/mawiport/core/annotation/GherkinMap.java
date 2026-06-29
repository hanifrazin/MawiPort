package com.mawiport.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GherkinMap {
    String sourceKey() default "";  // Source key from Gherkin data
    int order() default 0;
    String headerName() default "";
    int width() default 15;
    boolean optional() default true;  // Whether this field is optional
}