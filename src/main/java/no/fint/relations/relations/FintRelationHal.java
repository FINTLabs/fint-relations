package no.fint.relations.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.FintLinkMapper;
import no.fint.relations.FintResources;
import no.fint.relations.annotations.FintRelation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class FintRelationHal implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Value("${relation-id-base:uri:https://api.felleskomponent.no/rel/}")
    private String relationBase;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Object addRelations(AspectMetadata metadata, FintRelation[] relations, Object response) {
        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        try {
            Object body = responseEntity.get().getBody();
            if (body instanceof Collection) {
                return createCollectionResponse(responseEntity.get(), metadata, relations);
            } else {
                return createSingleResponse(responseEntity.get(), metadata, relations);
            }
        } catch (IllegalArgumentException e) {
            return response;
        }
    }

    private Optional<ResponseEntity> getResponseEntity(Object response) {
        if (response instanceof ResponseEntity) {
            return Optional.of((ResponseEntity) response);
        }
        return Optional.empty();
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        List<Link> links = new ArrayList<>();
        for (FintRelation relation : relations) {
            addRelations(responseEntity.getBody(), metadata, relation).ifPresent(links::add);
        }

        links.add(getSelfLink(metadata));
        Resource<?> resource = new Resource<>(responseEntity.getBody(), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private ResponseEntity createCollectionResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        Collection values = (Collection) responseEntity.getBody();
        List<Resource> resources = new ArrayList<>();
        for (Object value : values) {
            List<Link> links = new ArrayList<>();
            for (FintRelation relation : relations) {
                addRelations(value, metadata, relation).ifPresent(links::add);
            }

            try {
                String id = (String) PropertyUtils.getNestedProperty(value, metadata.getSelfId().id());
                Link selfLink = ControllerLinkBuilder.linkTo(metadata.getCallingClass()).slash(id).withSelfRel();
                links.add(selfLink);
                Resource<?> resource = new Resource<>(value, links);
                resources.add(resource);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("The id (%s) in @FintSelfId was not found", metadata.getSelfId().id()), e);
            }
        }

        FintResources embedded = new FintResources(values.size(), resources, getSelfLink(metadata));
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(embedded);
    }

    private Optional<Link> addRelations(Object body, AspectMetadata metadata, FintRelation relation) {
        Optional<Link> link = Optional.empty();
        String relationId = getRelationId(metadata, relation);
        Map<String, FintLinkMapper> beans = applicationContext.getBeansOfType(FintLinkMapper.class);
        if (beans.size() > 0) {
            Collection<FintLinkMapper> values = beans.values();
            Optional<FintLinkMapper> mapper = values.stream().filter(value -> value.type() == metadata.getSelfId().self()).findAny();
            try {
                Object property = PropertyUtils.getNestedProperty(body, metadata.getSelfId().id());
                Relation rel = new Relation();
                rel.setType(relationId);
                rel.setLeftKey((String) property);
                if (mapper.isPresent()) {
                    link = Optional.ofNullable(mapper.get().createRelation(rel));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("The id (%s) in @FintSelfId was not found", metadata.getSelfId().id()), e);
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
