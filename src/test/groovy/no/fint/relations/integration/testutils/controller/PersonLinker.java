package no.fint.relations.integration.testutils.controller;

import no.fint.relations.FintLinker;
import no.fint.relations.integration.testutils.dto.PersonResource;
import org.springframework.stereotype.Component;

@Component
public class PersonLinker extends FintLinker<PersonResource> {

    public PersonLinker() {
        super(PersonController.class);
    }

    @Override
    public String getSelfHref(PersonResource personResource) {
        return createHrefWithId(personResource.getName(), "name");
    }
}
