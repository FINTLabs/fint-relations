package no.fint.relations.integration.testutils.controller;

import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.annotations.FintSelf;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@FintSelf(type = Person.class, property = "name")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json", "application/ld+json"})
public class PersonRelationController {

    @FintRelations
    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntityNoInput() {
        Relation relation = new Relation.Builder()
                .with(Person.Relasjonsnavn.ADDRESS)
                .path("/address")
                .forType(Address.class)
                .value("test1")
                .build();

        Person person = new Person("test1");
        FintResource<Person> fintResource = FintResource.with(person).addRelasjon(relation);
        return ResponseEntity.ok(fintResource);
    }

    @FintRelations
    @RequestMapping("/responseEntity/{name}")
    public ResponseEntity getResponseEntity(@PathVariable String name) {
        return ResponseEntity.ok(new Person(name));
    }

    @FintRelations
    @RequestMapping("/responseEntity/{name1}/twoPathVariables/{name2}")
    public ResponseEntity getResponseEntity(@PathVariable String name1, @PathVariable String name2) {
        return ResponseEntity.ok(new Person(name1 + name2));
    }

    @FintRelations
    @RequestMapping(value = "/responseHeaders", method = RequestMethod.POST)
    public ResponseEntity createResource(@RequestBody Person person) {
        return ResponseEntity.created(URI.create("/responseEntity/" + person.getName())).body("Created resource");
    }

    @FintRelations
    @RequestMapping(value = "/responseEntity/list")
    public ResponseEntity getResponseEntityWithList() {
        FintResource<Person> person1 = new FintResource<>(Person.class, new Person("test1"));
        FintResource<Person> person2 = new FintResource<>(Person.class, new Person("test2"));
        List<FintResource<Person>> personer = new ArrayList<>();
        personer.add(person1);
        personer.add(person2);

        return ResponseEntity.ok(personer);
    }

    @FintRelations
    @RequestMapping("/customObject")
    public Person getTestDto() {
        return new Person("test123");
    }

    @FintRelations
    @RequestMapping("/nullValue")
    public Person getNullValue() {
        return new Person(null);
    }
}
