package no.fint.relations.integration.testutils.mapper;

import no.fint.relations.annotations.FintLinkMapper;
import no.fint.relations.annotations.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@FintLinkMapper
@Component
public class InvalidLinkMapper {

    @FintLinkRelation(leftObject = Person.class, leftId = "name", rightObject = Address.class, rightId = "street2")
    public Link createLink(String invalidInput) {
        return null;
    }
}
