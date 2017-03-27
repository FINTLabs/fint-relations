package no.fint.relations.integration.testutils.mapper;

import com.google.common.collect.Lists;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

@FintLinkMapper(Person.class)
@Component
public class TelephoneLinkMapper {

    @FintLinkRelation("REL_ID_MAINNUMBER")
    public Link createMainTelephonNumberLink(Relation relation) {
        return new Link("http://localhost/telephone/" + relation.getMain(), "mainTelephoneNumber");
    }

    @FintLinkRelation("REL_ID_SECONDARYNUMBER")
    public List<Link> createSecondaryTelephoneNumberLink(Relation relation) {
        return Lists.newArrayList(new Link("http://localhost/telephone/" + relation.getMain(), "secondaryTelephoneNumber"));
    }
}
