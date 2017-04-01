package no.fint.relations.integration.testutils.controller;

import com.google.common.collect.Lists;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
        return ResponseEntity.ok(Lists.newArrayList(new Person("test1"), new Person("test2")));
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
