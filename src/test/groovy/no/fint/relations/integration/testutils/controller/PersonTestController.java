package no.fint.relations.integration.testutils.controller;

import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.integration.testutils.assembler.MyResourceAssembler;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonTestController {

    @Autowired
    private MyResourceAssembler assembler;


    @GetMapping("/resource/without-link-mapper")
    public ResponseEntity getPersonWithoutLinkMapper() {
        return assembler.resource(createPersonWithoutCurie("test1"));
    }

    @GetMapping("/resource/with-link-mapper")
    public ResponseEntity getPersonWithLinkMapper() {
        return assembler.resource(createPersonWithCurie("test1"));
    }

    @GetMapping("/resources/without-link-mapper")
    public ResponseEntity getPersonsWithoutLinkMapper() {
        FintResource<Person> person1 = createPersonWithoutCurie("test1");
        FintResource<Person> person2 = createPersonWithoutCurie("test2");
        List<FintResource<Person>> personer = Lists.newArrayList(person1, person2);
        return assembler.resources(personer);
    }

    @GetMapping("/resources/with-link-mapper")
    public ResponseEntity getPersonsWithLinkMapper() {
        FintResource<Person> person1 = createPersonWithCurie("test1");
        FintResource<Person> person2 = createPersonWithCurie("test2");
        List<FintResource<Person>> personer = Lists.newArrayList(person1, person2);
        return assembler.resources(personer);
    }

    private FintResource<Person> createPersonWithoutCurie(String name) {
        Relation relation = new Relation.Builder()
                .with(Person.Relasjonsnavn.ADDRESS)
                .link("http://localhost/address/test")
                .build();

        return FintResource.with(new Person(name)).addRelasjoner(relation);
    }

    private FintResource<Person> createPersonWithCurie(String name) {
        Relation relation = new Relation.Builder()
                .with(Person.Relasjonsnavn.ADDRESS)
                .link("/address/test")
                .build();

        return FintResource.with(new Person(name)).addRelasjoner(relation);
    }

}
