package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TelephoneLinkMapper implements FintLinkMapper {

    @Override
    public Optional<Link> createLink(Relation relation) {
        if (relation.getType().endsWith("person.name:telephone.number")) {
            return Optional.of(new Link("http://localhost/telephone/" + relation.getLeftKey(), "telephone"));
        }

        return Optional.empty();
    }
}
