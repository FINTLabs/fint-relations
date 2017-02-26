package no.fint.relations.rel;

import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelfId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FintRelationAspect {

    @Around("execution(* (@no.fint.relations.annotations.FintRelation *).*(..))")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);

        FintSelfId selfId = metadata.getCallingClass().getAnnotation(FintSelfId.class);
        FintRelation relation = metadata.getCallingClass().getAnnotation(FintRelation.class);

        String simpleName = metadata.getCallingClass().getSimpleName();
        String leftKeyName = simpleName.toLowerCase().replace("controller", "");
        String rightKeyName = relation.objectLink().getSimpleName().toLowerCase();

        Relation rel = new Relation(leftKeyName + ":" + rightKeyName, "", "");

        log.info("fintRelationEndpoint");
        Object response = proceedingJoinPoint.proceed();



        return response;
    }
}
