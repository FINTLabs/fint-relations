package no.fint.relations;

import no.fint.relation.model.Relation;
import org.springframework.hateoas.Link;

public interface FintLinkMapper {

    Link createRelation(Relation relation, Object selfIdProperty);

    Class<?> type();

}
