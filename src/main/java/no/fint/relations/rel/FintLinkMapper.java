package no.fint.relations.rel;

import org.springframework.hateoas.Link;

public interface FintLinkMapper {

    Link createRelation(String type, Object... arguments);

    Class<?> type();

}
