package no.fint.relations.integration.testutils.controller;

import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelfId;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelfId(self = Person.class, id = "name")
@FintRelation(objectLink = Address.class, id = "street2")
@RestController
@RequestMapping(value = "/invalidLinkMapper", method = RequestMethod.GET)
public class InvalidLinkMapperTestController {

    @RequestMapping
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }


}
