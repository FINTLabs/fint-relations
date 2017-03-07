package no.fint.relations.annotations;

import java.lang.annotation.*;

@Repeatable(FintRelations.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FintRelation {
    Class<?> objectLink();

    String id();
}
