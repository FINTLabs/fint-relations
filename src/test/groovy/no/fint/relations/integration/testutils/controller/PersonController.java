package no.fint.relations.integration.testutils.controller;

import no.fint.model.resource.Link;
import no.fint.relations.FintResources;
import no.fint.relations.integration.testutils.dto.AddressResource;
import no.fint.relations.integration.testutils.dto.CityResource;
import no.fint.relations.integration.testutils.dto.PersonResource;
import no.fint.relations.integration.testutils.dto.PersonResources;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonLinker personLinker;


    @GetMapping("/resource/without-link-mapper")
    public PersonResource getPersonWithoutLinkMapper() {
        return personLinker.toResource(createPersonWithoutLinkMapper("test1"));
    }

    @GetMapping("/resource/with-link-mapper")
    public PersonResource getPersonWithLinkMapper() {
        return personLinker.toResource(createPersonWithLinkMapper("test1"));
    }

    @GetMapping("/resources/without-link-mapper")
    public PersonResources getPersonsWithoutLinkMapper() {
        PersonResource person1 = createPersonWithoutLinkMapper("test1");
        PersonResource person2 = createPersonWithoutLinkMapper("test2");
        List<PersonResource> personer = Lists.newArrayList(person1, person2);
        return personLinker.toResources(personer);
    }

    @GetMapping("/resources/with-link-mapper")
    public PersonResources getPersonsWithLinkMapper() {
        PersonResource person1 = createPersonWithLinkMapper("test1");
        PersonResource person2 = createPersonWithLinkMapper("test2");
        List<PersonResource> personer = Lists.newArrayList(person1, person2);
        return personLinker.toResources(personer);
    }


    private PersonResource createPersonWithoutLinkMapper(String name) {
        PersonResource personResource = new PersonResource();
        personResource.setName(name);
        personResource.addPersonalressurs(Link.with("http://localhost/personalressurs/1"));
        return personResource;
    }

    private PersonResource createPersonWithLinkMapper(String name) {
        AddressResource addressResource = new AddressResource("street", "street2");
        addressResource.addCity(Link.with(CityResource.class, "testing"));
        PersonResource personResource = new PersonResource();
        personResource.setAddress(addressResource);
        personResource.setName(name);
        personResource.addPersonalressurs(Link.with(PersonResource.class, "/1"));
        return personResource;
    }

}
