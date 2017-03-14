package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.annotations.FintLinkMapper;
import no.fint.relations.annotations.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Person;
import no.fint.relations.integration.testutils.dto.Telephone;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@FintLinkMapper
@Component
public class TelephoneLinkMapper {

    @FintLinkRelation(leftObject = Person.class, leftId = "name", rightObject = Telephone.class, rightId = "mainNumber")
    public Link createMainTelephonNumberLink(Relation relation) {
        return new Link("http://localhost/telephone/" + relation.getLeftKey(), "mainTelephoneNumber");
    }

    @FintLinkRelation(leftObject = Person.class, leftId = "name", rightObject = Telephone.class, rightId = "secondaryNumber")
    public Link createSecondaryTelephoneNumberLink(Relation relation) {
        return new Link("http://localhost/telephone/" + relation.getLeftKey(), "secondaryTelephoneNumber");
    }
}
