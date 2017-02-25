package no.fint.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.annotations.FintSelfId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
@Component
public class FintSelfIdAspect {


    @Around("execution(* (@no.fint.relations.annotations.FintSelfId *).*(..))")
    public Object selfIdEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();
        FintSelfId fintSelfId = target.getClass().getAnnotation(FintSelfId.class);
        String selfId = fintSelfId.value();
        if (StringUtils.isEmpty(selfId)) {
            throw new IllegalArgumentException("Missing value for the self id");
        }

        log.info("Self id: {}", selfId);

        Object response = proceedingJoinPoint.proceed();
        if (response instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) response;
            Resource<?> resource = new Resource<>(responseEntity.getBody());
            resource.add(new Link("/test", Link.REL_SELF));
            return ResponseEntity.status(responseEntity.getStatusCode()).body(resource);
        } else {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            log.info("No ResponseEntity, returning standard response: {}.{}()", target.getClass().getSimpleName(), signature.getMethod().getName());
            return response;
        }
    }

}
