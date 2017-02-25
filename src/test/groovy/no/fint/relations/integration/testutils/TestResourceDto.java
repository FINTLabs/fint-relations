package no.fint.relations.integration.testutils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestResourceDto extends ResourceSupport {
    private String name;
}
