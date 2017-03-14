package no.fint.relations.relations.hal;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.FintResources;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import no.fint.relations.annotations.FintRelation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class FintRelationHal implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Value("${relation-id-base:https://api.felleskomponent.no/rel/}")
    private String relationBase;

    @Autowired
    private SpringHateoasIntegration springHateoasIntegration;

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
            log.warn("Exception occurred when trying to add relations", e);
            return response;
        }
    }

    private Optional<ResponseEntity> getResponseEntity(Object response) {
        if (response instanceof ResponseEntity) {
            return Optional.of((ResponseEntity) response);
        }
        log.warn("ResponseEntity return type not found, type: {}", response.getClass().getSimpleName());
        return Optional.empty();
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        List<Link> links = new ArrayList<>();
        for (FintRelation relation : relations) {
            links.addAll(getRelationLinks(responseEntity.getBody(), metadata, relation));
        }

        links.add(springHateoasIntegration.getSelfLink(metadata));
        Resource<?> resource = new Resource<>(responseEntity.getBody(), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private ResponseEntity createCollectionResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        Collection values = (Collection) responseEntity.getBody();
        List<Resource> resources = new ArrayList<>();
        for (Object value : values) {
            List<Link> links = new ArrayList<>();
            for (FintRelation relation : relations) {
                links.addAll(getRelationLinks(value, metadata, relation));
            }

            links.add(getSelfLinkCollection(metadata, value));
            Resource<?> resource = new Resource<>(value, links);
            resources.add(resource);
        }

        FintResources embedded = new FintResources(values.size(), resources, springHateoasIntegration.getSelfLink(metadata));
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(embedded);
    }

    private Link getSelfLinkCollection(AspectMetadata metadata, Object value) {
        try {
            String id = (String) PropertyUtils.getNestedProperty(value, metadata.getSelfId().id());
            return springHateoasIntegration.getSelfLinkCollection(metadata, id);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("The id (%s) in @FintSelfId was not found", metadata.getSelfId().id()), e);
        }
    }

    private List<Link> getRelationLinks(Object body, AspectMetadata metadata, FintRelation relation) {
        List<Link> links = new ArrayList<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(FintLinkMapper.class);
        String relationId = getRelationId(metadata, relation);
        Collection<?> linkMappers = beans.values();
        for (Object linkMapper : linkMappers) {
            Method[] methods = linkMapper.getClass().getMethods();
            for (Method method : methods) {
                FintLinkRelation fintLinkRelation = AnnotationUtils.getAnnotation(method, FintLinkRelation.class);
                String linkRelationId = getRelationId(fintLinkRelation);
                if (fintLinkRelation != null && relationId.equals(linkRelationId)) {
                    validateLinkMapperMethod(method);

                    List<Link> linkMapperLinks = getLinks(body, metadata.getSelfId().id(), relationId, linkMapper, method);
                    if (linkMapperLinks != null) {
                        links.addAll(linkMapperLinks);
                    }
                }
            }
        }
        return links;
    }

    @SuppressWarnings("unchecked")
    private List<Link> getLinks(Object body, String selfId, String relationId, Object linkMapper, Method method) {
        try {
            Object property = PropertyUtils.getNestedProperty(body, selfId);
            Relation rel = new Relation();
            rel.setType(relationBase + relationId);
            rel.setLeftKey((String) property);

            Object response = method.invoke(linkMapper, rel);
            if (response instanceof List) {
                return (List<Link>) response;
            } else if (response instanceof Link) {
                return Lists.newArrayList((Link) response);
            } else {
                return Collections.emptyList();
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("The id (%s) in @FintSelfId was not found", selfId), e);
        }
    }

    private void validateLinkMapperMethod(Method method) {
        int parameterCount = method.getParameterCount();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        if (parameterCount != 1 || parameterTypes[0] != Relation.class || (returnType != Link.class && returnType != List.class)) {
            throw new IllegalArgumentException(String.format("The method %s needs to take a Relation object as input and return a Link or a list of Link objects", method.getName()));
        }
    }

    private String getRelationId(AspectMetadata metadata, FintRelation relation) {
        String leftKeyClass = metadata.getSelfId().self().getSimpleName().toLowerCase();
        String leftKeyProperty = metadata.getSelfId().id();
        String rightKeyClass = relation.objectLink().getSimpleName().toLowerCase();
        String rightKeyProperty = relation.id();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }

    private String getRelationId(FintLinkRelation fintLinkRelation) {
        if (fintLinkRelation == null) {
            return null;
        }

        String leftKeyClass = fintLinkRelation.leftObject().getSimpleName().toLowerCase();
        String leftKeyProperty = fintLinkRelation.leftId();
        String rightKeyClass = fintLinkRelation.rightObject().getSimpleName().toLowerCase();
        String rightKeyProperty = fintLinkRelation.rightId();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }
}
