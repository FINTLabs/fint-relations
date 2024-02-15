package no.fint.relations.integration.testutils.controller;

import no.fint.relations.FintLinker;
import no.fint.relations.integration.testutils.dto.PersonResource;
import no.fint.relations.integration.testutils.dto.PersonResources;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

@Component
public class PersonLinker extends FintLinker<PersonResource> {

    public PersonLinker() {
        super(PersonResource.class);
    }

    public PersonResource toResource(PersonResource resource) {
        mapLinks(resource);
        resetSelfLinks(resource);
        return resource;
    }

    @Override
    public PersonResources toResources(Collection<PersonResource> resources) {
        return toResources(resources.stream(), 0, 0, resources.size());
    }

    @Override
    public PersonResources toResources(Stream<PersonResource> stream, int offset, int size, int totalItems) {
        PersonResources personResources = new PersonResources();
        stream.peek(resource -> {
            mapLinks(resource);
            resetSelfLinks(resource);
        }).forEach(personResources::addResource);
        addPagination(personResources, offset, size, totalItems);
        return personResources;
    }

    @Override
    public String getSelfHref(PersonResource personResource) {
        return createHrefWithId(personResource.getName(), "name");
    }
}
