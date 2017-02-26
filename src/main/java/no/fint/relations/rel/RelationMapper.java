package no.fint.relations.rel;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public abstract class RelationMapper {

    protected List<Link> links = new ArrayList<>();

    protected void addLink(String href, String rel) {
        links.add(new Link(href, rel));
    }


}
