package no.fint.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.annotations.FintSelfId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
@Component
public class FintSelfIdAspect {


    @Around("execution(* (@no.fint.relations.annotations.FintSelfId *).*(..))")
    public Object executeEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();
        FintSelfId fintSelfId = target.getClass().getAnnotation(FintSelfId.class);
        String selfId = fintSelfId.value();
        if (StringUtils.isEmpty(selfId)) {
            throw new IllegalArgumentException("Missing value for the self id");
        }
        log.info("Self id: {}", selfId);

        return proceedingJoinPoint.proceed();
    }

}
