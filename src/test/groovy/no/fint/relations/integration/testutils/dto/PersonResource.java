package no.fint.relations.integration.testutils.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PersonResource implements FintLinks {

    @Getter
    private final Map<String, List<Link>> links = createLinks();

    @Getter
    private AddressResource address;

    private String name;

    public void addPersonalressurs(Link link) {
        addLink("personalressurs", link);
    }

    @Override
    public List<FintLinks> getNestedResources() {
        List<FintLinks> result = new ArrayList<>();
        result.add(address);
        return result;
    }
}
