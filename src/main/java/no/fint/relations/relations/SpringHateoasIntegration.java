package no.fint.relations.relations;

import no.fint.relations.AspectMetadata;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class SpringHateoasIntegration {

    public Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        return linkBuilder.withSelfRel();
    }

    public Link getSelfLinkCollection(AspectMetadata metadata, String id) {
        return ControllerLinkBuilder.linkTo(metadata.getCallingClass()).slash(id).withSelfRel();
    }
}
