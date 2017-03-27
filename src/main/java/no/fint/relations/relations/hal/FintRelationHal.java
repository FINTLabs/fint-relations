package no.fint.relations.relations.hal;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.AspectMetadata;
import no.fint.relations.FintResources;
import no.fint.relations.annotations.FintRelation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class FintRelationHal {

    @Autowired
    private SpringHateoasIntegration springHateoasIntegration;

    @Autowired
    private HalResourceLinks halResourceLinks;

    public ResponseEntity addRelations(AspectMetadata metadata, FintRelation[] relations, ResponseEntity responseEntity) {
        try {
            Object body = responseEntity.getBody();
            if (body instanceof Collection) {
                return createCollectionResponse(responseEntity, metadata, relations);
            } else {
                return createSingleResponse(responseEntity, metadata, relations);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Exception occurred when trying to add relations", e);
            return responseEntity;
        }
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) {
        List<Link> links = new ArrayList<>();
        for (FintRelation relation : relations) {
            links.addAll(halResourceLinks.getLinks(responseEntity.getBody(), metadata, relation));
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
                links.addAll(halResourceLinks.getLinks(value, metadata, relation));
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
            String id = (String) PropertyUtils.getNestedProperty(value, metadata.getFintSelf().id());
            return springHateoasIntegration.getSelfLinkCollection(metadata, id);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("The id (%s) in @FintSelf was not found", metadata.getFintSelf().id()), e);
        }
    }

}
