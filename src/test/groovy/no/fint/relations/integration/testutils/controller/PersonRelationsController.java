package no.fint.relations.integration.testutils.controller;

import com.google.common.collect.Lists;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/relations", method = RequestMethod.GET, produces = {"application/hal+json"})
public class PersonRelationsController {

    @FintRelations
    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }

    @FintRelations
    @RequestMapping("/responseEntity/list")
    public ResponseEntity getResponseEntityList() {
        return ResponseEntity.ok(Lists.newArrayList(new Person("test123"), new Person("test234")));
    }
}
