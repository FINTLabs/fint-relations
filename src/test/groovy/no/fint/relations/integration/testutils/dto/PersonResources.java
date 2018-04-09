package no.fint.relations.integration.testutils.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import no.fint.model.resource.AbstractCollectionResources;

import java.util.List;

public class PersonResources extends AbstractCollectionResources<PersonResource> {
    
    @Override
    public TypeReference<List<PersonResource>> getTypeReference() {
        return new TypeReference<List<PersonResource>>() {
        };
    }
}
