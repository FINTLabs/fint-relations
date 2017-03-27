package no.fint.relations;

import lombok.Data;
import no.fint.relations.annotations.FintSelf;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Data
public class AspectMetadata {
    private Class<?> callingClass;
    private FintSelf fintSelf;
    private Method method;
    private Object[] arguments;

    private AspectMetadata(ProceedingJoinPoint joinPoint) {
        callingClass = joinPoint.getTarget().getClass();
        if (callingClass.isAnnotationPresent(FintSelf.class)) {
            fintSelf = callingClass.getAnnotation(FintSelf.class);
        } else {
            throw new IllegalArgumentException("Missing @FintSelf");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        method = methodSignature.getMethod();
        arguments = joinPoint.getArgs();
    }

    public static AspectMetadata with(ProceedingJoinPoint joinPoint) {
        return new AspectMetadata(joinPoint);
    }

}
