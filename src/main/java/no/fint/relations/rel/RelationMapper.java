package no.fint.relations.rel;

import org.springframework.hateoas.Link;

public interface RelationMapper {

    Link createRelation(String id);

    Class<?> type();

}
