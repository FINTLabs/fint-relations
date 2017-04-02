package no.fint.relations.integration.testutils.fintself;

import no.fint.relations.annotations.FintSelf;
import no.fint.relations.integration.testutils.dto.Address;

@FintSelf(type = Address.class, property = "street")
public class NotIdentifiableFintSelf {
}
