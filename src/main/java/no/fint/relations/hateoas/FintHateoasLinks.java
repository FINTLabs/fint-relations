package no.fint.relations.hateoas;

import lombok.Getter;
import no.fint.model.resource.FintLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class FintHateoasLinks implements HateoasLinks {

    private final Class<?> controller;

    @Getter
    private final Class<?> model;

    public FintHateoasLinks(Class<?> controller, Class<?> model) {
        this.controller = controller;
        this.model = model;
    }

    public FintLinks populateRelations(FintLinks fintLinks) {
        Link link = ControllerLinkBuilder.linkTo(controller).withSelfRel();
        fintLinks.addLink(Link.REL_SELF, no.fint.model.resource.Link.with(link.getHref()));
        return fintLinks;
    }


    @Override
    public FintLinks assemble(FintLinks fintLinks) {
        return populateRelations(fintLinks);
    }
}
