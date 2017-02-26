package no.fint.relations.integration.testutils.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonResource extends ResourceSupport {
    private String name;
}
