package no.fint.relations.integration.testutils.controller;


import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelf;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelf(Person.class)
@FintRelation("REL_ID_DIFFERENTPROPERTY")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json", "application/ld+json"})
public class DifferentPropertyNameController {

    @RequestMapping("/differentProperty")
    public ResponseEntity getDifferentProperty() {
        Person person = new Person();
        person.setName2("name2");
        return ResponseEntity.ok(person);
    }
}
