package no.fint.relations.integration.testutils.controller;

import com.google.common.collect.Lists;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.annotations.FintSelfId;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.integration.testutils.dto.Telephone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelfId(self = Person.class, id = "name")
@FintRelations(
        rels = {
                @FintRelation(objectLink = Address.class, id = "street"),
                @FintRelation(objectLink = Telephone.class, id = "number")
        }
)
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
