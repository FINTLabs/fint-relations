package no.fint.relations.relations.hal;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.FintResources;
import no.fint.relations.relations.FintLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FintRelationHal {

    @Autowired
    private SpringHateoasIntegration springHateoasIntegration;

    @Autowired
    private FintLinkMapper fintLinkMapper;

    public ResponseEntity addRelations(AspectMetadata metadata, ResponseEntity responseEntity) {
        Object body = responseEntity.getBody();
        if (body instanceof Collection) {
            return createCollectionResponse(responseEntity, metadata);
        } else {
            return createSingleResponse(responseEntity, metadata);
        }
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata) {
        List<Link> links = new ArrayList<>();
        Object response = responseEntity.getBody();
        if (response instanceof FintResource) {
            FintResource fintResource = (FintResource) response;
            links.addAll(getLinks(fintResource));
        }

        links.add(springHateoasIntegration.getSelfLink(metadata));
        Resource<?> resource = new Resource<>(getResource(responseEntity.getBody()), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity createCollectionResponse(ResponseEntity responseEntity, AspectMetadata metadata) {
        Collection values = (Collection) responseEntity.getBody();
        List<Resource> resources = new ArrayList<>();
        for (Object value : values) {
            List<Link> links = new ArrayList<>();
            if (value instanceof FintResource) {
                FintResource fintResource = (FintResource) value;
                links.addAll(getLinks(fintResource));
                fintResource.getId().ifPresent(id -> links.add(springHateoasIntegration.getSelfLinkCollection(metadata, (String) id)));
            }

            Resource<?> resource = new Resource<>(getResource(value), links);
            resources.add(resource);
        }

        FintResources embedded = new FintResources(values.size(), resources, springHateoasIntegration.getSelfLink(metadata));
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(embedded);
    }

    @SuppressWarnings("unchecked")
    private List<Link> getLinks(FintResource fintResource) {
        List<Relation> relations = fintResource.getRelasjoner();
        return relations.stream().map(rel -> {
            String link = rel.getLink();
            return new Link(fintLinkMapper.getLink(link), rel.getRelationName());
        }).collect(Collectors.toList());
    }

    private Object getResource(Object body) {
        if (body instanceof FintResource) {
            return ((FintResource) body).getConvertedResource();
        } else {
            return body;
        }
    }
}
