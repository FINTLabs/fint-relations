package no.fint.relations.integration.testutils.controller;

import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintRelations(
        self = Person.class,
        rels = {
                @FintRelation(objectLink = Address.class)
        }
)
@RestController
@RequestMapping(value = "/relations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonRelationsController {

    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }
}
