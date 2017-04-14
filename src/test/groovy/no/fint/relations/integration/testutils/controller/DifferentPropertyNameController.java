package no.fint.relations.integration.testutils.controller;


import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json", "application/ld+json"})
public class DifferentPropertyNameController {

    @FintRelations
    @RequestMapping("/differentProperty")
    public ResponseEntity getDifferentProperty() {
        Relation relation = new Relation.Builder().with(Person.Relasjonsnavn.DIFFERENTPROPERTY).forType(Address.class).value("123").build();
        Person person = new Person();
        person.setName2("name2");

        FintResource<Person> fintResource = FintResource.with(person).addRelasjoner(relation);
        return ResponseEntity.ok(fintResource);
    }
}
