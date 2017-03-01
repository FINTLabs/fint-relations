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
        if(relation.getType().endsWith("person:address")) {
            return new Link("http://localhost/address/" + relation.getLeftKey(), "address");
        } else {
            return new Link("http://localhost/telephone/" + relation.getLeftKey(), "telephone");
        }
    }

    @Override
    public Class<?> type() {
        return Person.class;
    }
}
