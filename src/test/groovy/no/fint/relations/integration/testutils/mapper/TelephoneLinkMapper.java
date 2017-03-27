package no.fint.relations.integration.testutils.mapper;

import com.google.common.collect.Lists;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.integration.testutils.dto.Telephone;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

@FintLinkMapper(leftObject = Person.class, leftId = "name")
@Component
public class TelephoneLinkMapper {

    @FintLinkRelation(rightObject = Telephone.class, rightId = "mainNumber")
    public Link createMainTelephonNumberLink(Relation relation) {
        return new Link("http://localhost/telephone/" + relation.getMain(), "mainTelephoneNumber");
    }

    @FintLinkRelation(rightObject = Telephone.class, rightId = "secondaryNumber")
    public List<Link> createSecondaryTelephoneNumberLink(Relation relation) {
        return Lists.newArrayList(new Link("http://localhost/telephone/" + relation.getMain(), "secondaryTelephoneNumber"));
    }
}
