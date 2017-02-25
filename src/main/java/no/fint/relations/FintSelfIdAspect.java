package no.fint.relations;

import no.fint.relations.annotation.FintSelfId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Component
public class FintSelfIdAspect {

    @Around("@annotation(no.fint.role.annotations.FintSelfId)")
    public Object executeEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method requestMethod = signature.getMethod();

        FintSelfId fintSelfId = requestMethod.getAnnotation(FintSelfId.class);
        String selfId = fintSelfId.value();
        if(StringUtils.isEmpty(selfId)) {
            throw new IllegalArgumentException("@FintSelfId is missing value for the self id");
        }

        return proceedingJoinPoint.proceed();
    }

}
