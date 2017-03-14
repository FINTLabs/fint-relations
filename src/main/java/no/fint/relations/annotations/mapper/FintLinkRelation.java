package no.fint.relations.annotations.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FintLinkRelation {
    Class<?> leftObject();

    String leftId();

    Class<?> rightObject();

    String rightId();
}
