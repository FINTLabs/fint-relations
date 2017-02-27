package no.fint.relations.rel;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Aspect
@Component
public class FintRelationAspect implements ApplicationContextAware {

    @Value("${relation-id-base:uri:https://api.felleskomponent.no/rel/}")
    private String relationBase;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Around("execution(* (@no.fint.relations.annotations.FintRelation *).*(..))")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        if(response instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) response;

            AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);

            FintRelation relation = metadata.getCallingClass().getAnnotation(FintRelation.class);

            String simpleName = metadata.getCallingClass().getSimpleName();
            String leftKeyName = simpleName.toLowerCase().replace("controller", "");
            String rightKeyName = relation.objectLink().getSimpleName().toLowerCase();
            String relationId = String.format("%s%s:%s", relationBase, leftKeyName, rightKeyName);

            List<Link> links = new ArrayList<>();
            Map<String, RelationMapper> beans = applicationContext.getBeansOfType(RelationMapper.class);
            if (beans.size() > 0) {
                Collection<RelationMapper> values = beans.values();
                Optional<RelationMapper> mapper = values.stream().filter(value -> value.type() == relation.self()).findAny();
                mapper.ifPresent(relationMapper -> links.add(relationMapper.createRelation(relationId)));
            }

            links.add(getSelfLink(metadata));


            Resource<?> resource = new Resource<>(responseEntity.getBody(), links);
            return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
        }

        return response;
    }

    private Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        return linkBuilder.withSelfRel();
    }


}
