package no.fint.relations.integration.testutils.mapper;

import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@FintLinkMapper(leftObject = Person.class, leftId = "name")
@Component
public class InvalidLinkMapper {

    @FintLinkRelation(rightObject = Address.class, rightId = "street2")
    public Link createLink(String invalidInput) {
        return null;
    }
}
