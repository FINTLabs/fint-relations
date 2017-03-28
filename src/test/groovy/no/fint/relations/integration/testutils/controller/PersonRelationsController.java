package no.fint.relations.integration.testutils.controller;

import com.google.common.collect.Lists;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelf;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelf(Person.class)
@FintRelation("REL_ID_ADDRESS")
@FintRelation("REL_ID_MAINNUMBER")
@FintRelation("REL_ID_SECONDARYNUMBER")
@RestController
@RequestMapping(value = "/relations", method = RequestMethod.GET, produces = {"application/hal+json"})
public class PersonRelationsController {

    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }

    @RequestMapping("/responseEntity/list")
    public ResponseEntity getResponseEntityList() {
        return ResponseEntity.ok(Lists.newArrayList(new Person("test123"), new Person("test234")));
    }
}
