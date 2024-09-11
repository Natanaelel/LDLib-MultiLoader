package com.lowdragmc.lowdraglib.gui.graphprocessor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OutputPort {
    String name() default "";
    boolean allowMultiple() default true;
    String[] tips() default {};

}