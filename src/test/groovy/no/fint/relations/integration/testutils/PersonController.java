package no.fint.relations.integration.testutils;

import no.fint.relations.annotations.FintRelation;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@FintRelation(self = Person.class, objectLink = Address.class, basePath = "/address")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonController {

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

    @RequestMapping("/customObject")
    public Person getTestDto() {
        return new Person("test123");
    }

}
