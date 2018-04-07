package no.fint.relations.integration.testutils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResource implements FintLinks {

    @Getter
    private final Map<String, List<Link>> links = createLinks();

    private String street;
    private String street2;

    public void addCity(Link link) {
        addLink("city", link);
    }
}
