package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.rel.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PersonLinkMapper implements FintLinkMapper {

    @Override
    public Link createRelation(Relation relation, Object... arguments) {
        return new Link("http://localhost/address", "address");
    }

    @Override
    public Class<?> type() {
        return Person.class;
    }
}
