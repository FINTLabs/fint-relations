package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PersonLinkMapper implements FintLinkMapper {

    @Override
    public Link createRelation(Relation relation, Object selfIdProperty) {
        return new Link("http://localhost/address/" + selfIdProperty, "address");
    }

    @Override
    public Class<?> type() {
        return Person.class;
    }
}
