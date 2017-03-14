package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressLinkMapper implements FintLinkMapper {

    @Override
    public Optional<Link> createLink(Relation relation) {
        if (relation.getType().endsWith("person.name:address.street")) {
            return Optional.of(new Link("http://localhost/address/" + relation.getLeftKey(), "address"));
        }
        return Optional.empty();
    }
}
