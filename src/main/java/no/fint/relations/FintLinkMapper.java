package no.fint.relations;

import no.fint.relation.model.Relation;
import org.springframework.hateoas.Link;

import java.util.Optional;

public interface FintLinkMapper {

    Optional<Link> createLink(Relation relation);

}
