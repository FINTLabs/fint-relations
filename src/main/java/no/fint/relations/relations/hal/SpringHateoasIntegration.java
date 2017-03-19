package no.fint.relations.relations.hal;

import no.fint.relations.AspectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class SpringHateoasIntegration {

    @Value("${fint.relations.force-https:true}")
    private String forceHttps;

    public Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        Link link = linkBuilder.withSelfRel();
        if (Boolean.valueOf(forceHttps)) {
            String href = link.getHref().replace("http://", "https://");
            return new Link(href, link.getRel());
        }
        return link;
    }

    public Link getSelfLinkCollection(AspectMetadata metadata, String id) {
        Link link = ControllerLinkBuilder.linkTo(metadata.getCallingClass()).slash(id).withSelfRel();
        if (Boolean.valueOf(forceHttps)) {
            String href = link.getHref().replace("http://", "https://");
            return new Link(href, link.getRel());
        }
        return link;
    }
}
