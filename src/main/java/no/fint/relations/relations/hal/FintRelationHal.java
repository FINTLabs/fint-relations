package no.fint.relations.relations.hal;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.relation.FintModel;
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
        try {
            Object body = responseEntity.getBody();
            if (body instanceof Collection) {
                return createCollectionResponse(responseEntity, metadata);
            } else {
                return createSingleResponse(responseEntity, metadata);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Exception occurred when trying to add relations", e);
            return responseEntity;
        }
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata) {
        List<Link> links = new ArrayList<>();
        try {
            Object response = responseEntity.getBody();
            if (response instanceof FintModel) {
                FintModel fintModel = (FintModel) response;
                links.addAll(getLinks(fintModel));
            }
        } catch (ClassCastException e) {
            log.error("The response is not of type Identifiable, {}", e.getMessage());
        }

        links.add(springHateoasIntegration.getSelfLink(metadata));
        Resource<?> resource = new Resource<>(removeInternalRelations(responseEntity.getBody()), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private ResponseEntity createCollectionResponse(ResponseEntity responseEntity, AspectMetadata metadata) {
        Collection values = (Collection) responseEntity.getBody();
        List<Resource> resources = new ArrayList<>();
        for (Object value : values) {
            List<Link> links = new ArrayList<>();
            try {
                if (value instanceof FintModel) {
                    FintModel fintModel = (FintModel) value;
                    links.addAll(getLinks(fintModel));
                    links.add(springHateoasIntegration.getSelfLinkCollection(metadata, fintModel.getId()));
                }
            } catch (ClassCastException e) {
                log.error("The response is not of type Identifiable, {}", e.getMessage());
            }

            Resource<?> resource = new Resource<>(removeInternalRelations(value), links);
            resources.add(resource);
        }

        FintResources embedded = new FintResources(values.size(), resources, springHateoasIntegration.getSelfLink(metadata));
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(embedded);
    }


    private List<Link> getLinks(FintModel fintModel) {
        return fintModel.getRelasjoner().stream().map(rel -> {
            String link = rel.getLink();
            return new Link(fintLinkMapper.getLink(link), rel.getRelationName());
        }).collect(Collectors.toList());
    }

    private Object removeInternalRelations(Object response) {
        if (response instanceof FintModel) {
            FintModel fintModel = (FintModel) response;
            fintModel.setRelasjoner(null);
            return fintModel;
        } else {
            return response;
        }
    }
}
