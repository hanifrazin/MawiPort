package com.krai.core.annotation;

import com.krai.core.engine.ReflectionMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field should be populated from a Gherkin AST
 * attribute with the given {@code sourceKey}. The {@code required} flag
 * signals whether the mapping is mandatory – a missing value will cause a
 * warning to be logged but will not abort the whole mapping process.
 *
 * <p>Usage example:
 * <pre>
 * {@code @GherkinMap(sourceKey = "name", required = true)}
 * private String testName;
 * </pre>
 *
 * @see ReflectionMapper
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GherkinMap {

    /**
     * The key/name of the property in the source Gherkin AST object.
     */
    String sourceKey();

    /**
     * Whether the source value must be present. If {@code true} and the
     * value is missing, the mapper logs a warning.
     */
    boolean required() default false;
}