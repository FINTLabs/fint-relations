package no.fint.relations.integration.testutils.controller;

import no.fint.model.resource.Link;
import no.fint.relations.FintLinker;
import no.fint.relations.integration.testutils.dto.PersonResource;
import no.fint.relations.integration.testutils.dto.PersonResources;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PersonLinker extends FintLinker<PersonResource> {

    public PersonLinker() {
        super(PersonResource.class);
    }

    @Override
    public PersonResources toResources(Collection<PersonResource> resources) {
        PersonResources personResources = new PersonResources();
        resources.stream().map(this::toResource).forEach(personResources::addResource);
        personResources.addSelf(Link.with(self()));
        return personResources;
    }

    @Override
    public String getSelfHref(PersonResource personResource) {
        return createHrefWithId(personResource.getName(), "name");
    }
}
