package no.fint.relations.rel;

import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintRelations;
import org.apache.commons.beanutils.PropertyUtils;
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

import java.lang.reflect.InvocationTargetException;
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
        return createSingleResponse(responseEntity.get(), metadata, fintRelations.rels());
    }


    @Around("execution(* (@no.fint.relations.annotations.FintRelation *).*(..))")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelation[] relations = metadata.getCallingClass().getAnnotationsByType(FintRelation.class);

        Object body = responseEntity.get().getBody();
        if (body instanceof List) {
            return createCollectionResponse(responseEntity.get(), metadata, relations);
        } else {
            return createSingleResponse(responseEntity.get(), metadata, relations);
        }
    }

    private Optional<ResponseEntity> getResponseEntity(Object response) {
        if (response instanceof ResponseEntity) {
            return Optional.of((ResponseEntity) response);
        }
        return Optional.empty();
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Link> links = new ArrayList<>();
        for (FintRelation relation : relations) {
            try {
                addRelations(responseEntity, metadata, relation).ifPresent(links::add);
            } catch (NoSuchMethodException e) {
                log.info(e.getMessage());
            }
            links.add(getSelfLink(metadata));
        }

        Resource<?> resource = new Resource<>(responseEntity.getBody(), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private ResponseEntity createCollectionResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        return null;
    }

    private Optional<Link> addRelations(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation relation) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Optional<Link> link = Optional.empty();
        String relationId = getRelationId(metadata, relation);
        Map<String, FintLinkMapper> beans = applicationContext.getBeansOfType(FintLinkMapper.class);
        if (beans.size() > 0) {
            Collection<FintLinkMapper> values = beans.values();
            Optional<FintLinkMapper> mapper = values.stream().filter(value -> value.type() == metadata.getSelfId().self()).findAny();
            if (metadata.getArguments().length > 0) {
                Object property = PropertyUtils.getNestedProperty(responseEntity.getBody(), metadata.getSelfId().id());
                Relation rel = new Relation(relationId, metadata.getSelfId().id(), relation.id());
                if (mapper.isPresent()) {
                    link = Optional.ofNullable(mapper.get().createRelation(rel, property));
                }
            }
        }
        return link;
    }

    private String getRelationId(AspectMetadata metadata, FintRelation relation) {
        String leftKeyName = metadata.getSelfId().self().getSimpleName().toLowerCase();
        String rightKeyName = relation.objectLink().getSimpleName().toLowerCase();
        return String.format("%s%s:%s", relationBase, leftKeyName, rightKeyName);
    }

    private Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        return linkBuilder.withSelfRel();
    }


}
