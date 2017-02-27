package no.fint.relations.integration.testutils.mapper;

import no.fint.relations.rel.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class TestLinkMapper implements FintLinkMapper {

    @Override
    public Link createRelation(String type, Object... arguments) {
        return null;
    }

    @Override
    public Class<?> type() {
        return null;
    }
}
