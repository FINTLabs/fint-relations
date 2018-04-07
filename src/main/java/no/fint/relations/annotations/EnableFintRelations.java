package no.fint.relations.annotations;

import no.fint.relations.config.FintRelationsConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FintRelationsConfig.class)
public @interface EnableFintRelations {
}