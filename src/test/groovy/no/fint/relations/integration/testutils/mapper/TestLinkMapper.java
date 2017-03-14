package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestLinkMapper implements FintLinkMapper {

    @Override
    public Optional<Link> createLink(Relation relation) {
        return Optional.empty();
    }

}
