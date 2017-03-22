package no.fint.relations.relations.hal;

import no.fint.relations.AspectMetadata;
import no.fint.relations.config.FintRelationsProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class SpringHateoasIntegration {

    @Autowired
    private FintRelationsProps fintRelationsProps;

    public Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        Link link = linkBuilder.withSelfRel();
        if (Boolean.valueOf(fintRelationsProps.getForceHttps())) {
            String href = link.getHref().replace("http://", "https://");
            return new Link(href, link.getRel());
        }
        return link;
    }

    public Link getSelfLinkCollection(AspectMetadata metadata, String id) {
        Link link = ControllerLinkBuilder.linkTo(metadata.getCallingClass()).slash(id).withSelfRel();
        if (Boolean.valueOf(fintRelationsProps.getForceHttps())) {
            String href = link.getHref().replace("http://", "https://");
            return new Link(href, link.getRel());
        }
        return link;
    }
}
