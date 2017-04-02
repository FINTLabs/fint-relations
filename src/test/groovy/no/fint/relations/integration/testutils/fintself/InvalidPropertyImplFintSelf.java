package no.fint.relations.integration.testutils.fintself;

import no.fint.relations.annotations.FintSelf;
import no.fint.relations.integration.testutils.dto.Person;

@FintSelf(type = Person.class, property = "name2")
public class InvalidPropertyImplFintSelf {
}
