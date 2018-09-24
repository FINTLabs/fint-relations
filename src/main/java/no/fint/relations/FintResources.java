package no.fint.relations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.fint.model.resource.AbstractCollectionResources;
import no.fint.model.resource.Link;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Deprecated
public class FintResources<T> extends AbstractCollectionResources<T> {

    public FintResources(List<T> resources, String selfLink) {
        embedded.entries.addAll(resources);
        
        List<Link> selfLinks = new ArrayList<>();
        selfLinks.add(Link.with(selfLink));
        links.put("self", selfLinks);
    }

    @JsonIgnore
    @Override
    public TypeReference<List<T>> getTypeReference() {
        return new TypeReference<List<T>>() {
        };
    }

}