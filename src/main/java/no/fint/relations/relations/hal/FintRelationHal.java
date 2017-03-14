package no.fint.relations.relations.hal;

import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.FintLinkMapper;
import no.fint.relations.FintResources;
import no.fint.relations.annotations.FintRelation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
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
            links.addAll(getLinks(responseEntity.getBody(), metadata, relation));
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
                links.addAll(getLinks(value, metadata, relation));
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

    private List<Link> getLinks(Object body, AspectMetadata metadata, FintRelation relation) {
        List<Link> links = new ArrayList<>();
        Map<String, FintLinkMapper> beans = applicationContext.getBeansOfType(FintLinkMapper.class);
        if (beans.size() > 0) {
            String relationId = getRelationId(metadata, relation);
            Collection<FintLinkMapper> linkMappers = beans.values();
            for (FintLinkMapper linkMapper : linkMappers) {
                try {
                    Object property = PropertyUtils.getNestedProperty(body, metadata.getSelfId().id());
                    Relation rel = new Relation();
                    rel.setType(relationBase + relationId);
                    rel.setLeftKey((String) property);

                    Optional<Link> link = linkMapper.createLink(rel);
                    link.ifPresent(links::add);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new IllegalArgumentException(String.format("The id (%s) in @FintSelfId was not found", metadata.getSelfId().id()), e);
                }
            }
        }
        return links;
    }

    private String getRelationId(AspectMetadata metadata, FintRelation relation) {
        String leftKeyClass = metadata.getSelfId().self().getSimpleName().toLowerCase();
        String leftKeyProperty = metadata.getSelfId().id();
        String rightKeyClass = relation.objectLink().getSimpleName().toLowerCase();
        String rightKeyProperty = relation.id();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }
}
