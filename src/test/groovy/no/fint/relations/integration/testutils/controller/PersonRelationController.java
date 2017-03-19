package no.fint.relations.integration.testutils.controller;

import com.google.common.collect.Lists;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelfId;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@FintSelfId(self = Person.class, id = "name")
@FintRelation(objectLink = Address.class, id = "street")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json", "application/ld+json"})
public class PersonRelationController {

    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntityNoInput() {
        return ResponseEntity.ok(new Person("test1"));
    }

    @RequestMapping("/responseEntity/{name}")
    public ResponseEntity getResponseEntity(@PathVariable String name) {
        return ResponseEntity.ok(new Person(name));
    }

    @RequestMapping("/responseEntity/{name1}/twoPathVariables/{name2}")
    public ResponseEntity getResponseEntity(@PathVariable String name1, @PathVariable String name2) {
        return ResponseEntity.ok(new Person(name1 + name2));
    }

    @RequestMapping(value = "/responseHeaders", method = RequestMethod.POST)
    public ResponseEntity createResource(@RequestBody Person person) {
        return ResponseEntity.created(URI.create("/responseEntity/" + person.getName())).body("Created resource");
    }

    @RequestMapping(value = "/responseEntity/list")
    public ResponseEntity getResponseEntityWithList() {
        return ResponseEntity.ok(Lists.newArrayList(new Person("test1"), new Person("test2")));
    }

    @RequestMapping("/customObject")
    public Person getTestDto() {
        return new Person("test123");
    }

    @RequestMapping("/nullValue")
    public Person getNullValue() {
        return new Person(null);
    }
}
