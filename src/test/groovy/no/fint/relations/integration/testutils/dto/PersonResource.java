package no.fint.relations.integration.testutils.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonResource extends ResourceSupport {
    @JsonUnwrapped
    private Person person;
}
