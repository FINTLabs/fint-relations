package no.fint.relations.integration.testutils.assembler;

import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.integration.testutils.controller.PersonTestController;
import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.integration.testutils.dto.PersonResource;
import org.springframework.stereotype.Component;

@Component
public class MyResourceAssembler extends FintResourceAssembler<Person, PersonResource> {
    public MyResourceAssembler() {
        super(PersonTestController.class, PersonResource.class);
    }

    @Override
    public PersonResource mapToResource(FintResource<Person> resource) {
        Person person = resource.getResource();
        PersonResource personResource = createResourceWithId(person.getName(), resource);
        personResource.setPerson(person);
        return personResource;
    }
}
