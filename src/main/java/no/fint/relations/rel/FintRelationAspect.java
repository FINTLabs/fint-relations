package no.fint.relations.rel;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintRelations;
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

    @Around("execution(* (@no.fint.relations.annotations.FintRelations *).*(..))")
    public Object fintRelationsEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelations fintRelations = metadata.getCallingClass().getAnnotation(FintRelations.class);
        metadata.setSelf(fintRelations.self());
        return createResponse(responseEntity.get(), metadata, fintRelations.rels());
    }


    @Around("execution(* (@no.fint.relations.annotations.FintRelation *).*(..))")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelation relation = metadata.getCallingClass().getAnnotation(FintRelation.class);
        if (relation.self() == Object.class) {
            throw new IllegalArgumentException("The self type is not set on @FintRelation");
        }
        metadata.setSelf(relation.self());
        return createResponse(responseEntity.get(), metadata, relation);
    }

    private Optional<ResponseEntity> getResponseEntity(Object response) {
        if (response instanceof ResponseEntity) {
            return Optional.of((ResponseEntity) response);
        }
        return Optional.empty();
    }

    private ResponseEntity createResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        List<Link> links = new ArrayList<>();
        for (FintRelation relation : relations) {
            String relationId = getRelationId(relation);
            Map<String, FintLinkMapper> beans = applicationContext.getBeansOfType(FintLinkMapper.class);
            if (beans.size() > 0) {
                Collection<FintLinkMapper> values = beans.values();
                Optional<FintLinkMapper> mapper = values.stream().filter(value -> value.type() == relation.self()).findAny();
                if (metadata.getArguments().length > 0) {
                    mapper.ifPresent(fintLinkMapper -> links.add(fintLinkMapper.createRelation(relationId, metadata.getArguments())));
                }
            }

            links.add(getSelfLink(metadata));
        }


        Resource<?> resource = new Resource<>(responseEntity.getBody(), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private String getRelationId(FintRelation relation) {
        String leftKeyName = relation.self().getName().toLowerCase();
        String rightKeyName = relation.objectLink().getSimpleName().toLowerCase();
        return String.format("%s%s:%s", relationBase, leftKeyName, rightKeyName);
    }

    private Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        return linkBuilder.withSelfRel();
    }


}
