package no.fint.relations.integration.testutils;

import no.fint.relations.rel.RelationMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class TestRelationMapper implements RelationMapper {

    @Override
    public Link createRelation(String id) {
        return null;
    }

    @Override
    public Class<?> type() {
        return null;
    }
}
