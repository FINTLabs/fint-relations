package no.fint.relations.integration.testutils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NoArgsConstructor;
import no.fint.model.resource.AbstractCollectionResources;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
public class PersonResources extends AbstractCollectionResources<PersonResource> {

    public PersonResources(Collection<PersonResource> input) {
        super(input);
    }

    @Deprecated
    @Override
    @JsonIgnore
    public TypeReference<List<PersonResource>> getTypeReference() {
        return new TypeReference<List<PersonResource>>() {
        };
    }
}
