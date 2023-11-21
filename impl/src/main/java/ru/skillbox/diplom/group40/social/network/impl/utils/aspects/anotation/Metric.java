package ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Metric {
    String label() default "";
    String nameMetric() default "";
}
