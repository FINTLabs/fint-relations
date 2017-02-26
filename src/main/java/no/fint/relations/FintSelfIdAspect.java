package no.fint.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.annotations.FintSelfId;
import no.fint.relations.aspect.AspectMetadata;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
@Component
public class FintSelfIdAspect {

    @Autowired
    private EntityLinks entityLinks;

    @Around("execution(* (@no.fint.relations.annotations.FintSelfId *).*(..))")
    public Object selfIdEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);

        String selfId = getSelfId(metadata);
        Object response = proceedingJoinPoint.proceed();
        if (response instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) response;
            return addSelfLink(metadata, responseEntity);
        } else {
            log.info("No ResponseEntity, returning standard response: {}.{}()", metadata.getCallingClass().getSimpleName(), metadata.getMethod().getName());
            return response;
        }
    }

    private Object addSelfLink(AspectMetadata metadata, ResponseEntity responseEntity) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        Resource<?> resource = new Resource<>(responseEntity.getBody(), linkBuilder.withSelfRel());
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private String getSelfId(AspectMetadata metadata) {
        FintSelfId fintSelfId = metadata.getCallingClass().getAnnotation(FintSelfId.class);
        String selfId = fintSelfId.value();
        if (StringUtils.isEmpty(selfId)) {
            throw new IllegalArgumentException("Missing value for the self id");
        }

        return selfId;
    }

}
