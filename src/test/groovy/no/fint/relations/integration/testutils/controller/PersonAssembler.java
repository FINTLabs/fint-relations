package no.fint.relations.integration.testutils.controller;

import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler extends FintResourceAssembler<Person> {
    public PersonAssembler() {
        super(PersonController.class);
    }

    @Override
    public FintResourceSupport assemble(Person person, FintResource<Person> resource) {
        return createResourceWithId(person.getName(), resource);
    }
}
