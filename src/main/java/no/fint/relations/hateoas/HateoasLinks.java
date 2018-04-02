package no.fint.relations.hateoas;

import no.fint.model.resource.FintLinks;

public interface HateoasLinks {

    FintLinks assemble(FintLinks fintLinks);

    Class<?> getModel();

}
