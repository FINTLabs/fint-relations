package no.fint.relations.integration.testutils.controller;


import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.integration.testutils.dto.Address;
import org.assertj.core.util.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json", "application/ld+json"})
public class FintResourcesController {

    @FintRelations
    @RequestMapping("/fintResource")
    public ResponseEntity getAddress() {
        Address address = new Address("street", "street2");
        FintResource<Address> fintResource = FintResource.with(address).addRelasjoner(
                new Relation.Builder().with(Address.Relasjonsnavn.COUNTRY)
                        .forType(Address.class)
                        .path("/address")
                        .value("street").build()
        );
        return ResponseEntity.ok(Lists.newArrayList(fintResource));
    }

}
