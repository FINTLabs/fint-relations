package no.fint.relations.integration.testutils.hateoas.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class PersonResource implements FintLinks {

    @Getter
    private final Map<String, List<Link>> links = createLinks();

    @Getter
    private AddressResource addressResource;

    private String name;

    public void addKjonn(Link link) {
        addLink("kjonn", link);
    }

    @Override
    public List<FintLinks> getNestedResources() {
        List<FintLinks> result = new ArrayList<>();
        result.add(addressResource);
        return null;
    }
}
