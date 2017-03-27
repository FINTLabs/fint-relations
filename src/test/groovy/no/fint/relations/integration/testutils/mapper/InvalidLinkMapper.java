package no.fint.relations.integration.testutils.mapper;

import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@FintLinkMapper(Person.class)
@Component
public class InvalidLinkMapper {

    @FintLinkRelation("REL_ID_INVALIDLINK")
    public Link createLink(String invalidInput) {
        return null;
    }
}
