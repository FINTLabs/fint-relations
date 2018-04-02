package no.fint.relations.integration.testutils.hateoas.controller;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.relations.hateoas.FintHateoasLinkAssembler;
import no.fint.relations.integration.testutils.hateoas.dto.AddressResource;
import no.fint.relations.integration.testutils.hateoas.dto.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hateoas-person")
public class PersonHateoasController {

    @Autowired
    private FintHateoasLinkAssembler assembler;

    @GetMapping("/resource/with-link-mapper")
    public FintLinks getPerson() {
        return assembler.assemble(createPersonResource());
    }

    private PersonResource createPersonResource() {
        PersonResource personResource = new PersonResource();
        personResource.setName("test");
        personResource.addKjonn(Link.with("http://localhost/kjonn"));
        personResource.setAddressResource(new AddressResource("street1", "street2"));
        return personResource;
    }
}
