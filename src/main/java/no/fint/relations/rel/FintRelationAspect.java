package no.fint.relations.rel;

import lombok.extern.slf4j.Slf4j;
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
        log.info("fintRelationEndpoint");
        return proceedingJoinPoint.proceed();
    }
}
