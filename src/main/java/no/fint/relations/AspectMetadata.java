package no.fint.relations;

import lombok.Data;
import no.fint.relations.annotations.FintSelfId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Data
public class AspectMetadata {
    private Class<?> callingClass;
    private FintSelfId selfId;
    private Method method;
    private Object[] arguments;

    private AspectMetadata(ProceedingJoinPoint joinPoint) {
        callingClass = joinPoint.getTarget().getClass();
        if (callingClass.isAnnotationPresent(FintSelfId.class)) {
            selfId = callingClass.getAnnotation(FintSelfId.class);
        } else {
            throw new IllegalArgumentException("Missing @FintSelfId");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        method = methodSignature.getMethod();
        arguments = joinPoint.getArgs();
    }

    public static AspectMetadata with(ProceedingJoinPoint joinPoint) {
        return new AspectMetadata(joinPoint);
    }

}
