package no.fint.relations.integration.testutils;

import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.rel.RelationMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PersonRelationMapper implements RelationMapper {

    @Override
    public Link createRelation(String id) {
        return new Link("http://localhost/address", "address");
    }

    @Override
    public Class<?> type() {
        return Person.class;
    }
}
