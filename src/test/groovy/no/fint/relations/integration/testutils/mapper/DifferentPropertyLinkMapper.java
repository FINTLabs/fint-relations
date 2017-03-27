package no.fint.relations.integration.testutils.mapper;

import no.fint.model.relation.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@FintLinkMapper(Person.class)
@Component
public class DifferentPropertyLinkMapper {

    @FintLinkRelation("REL_ID_DIFFERENTPROPERTY")
    public Link createLink(Relation relation) {
        return new Link("http://localhost/" + relation.getMain(), relation.getRelationType().getRelationName());
    }
}
