package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PersonLinkMapper implements FintLinkMapper {

    @Override
    public Link createRelation(Relation relation) {
        return new Link("http://localhost/address/" + relation.getLeftKey(), "address");
    }

    @Override
    public Class<?> type() {
        return Person.class;
    }
}
