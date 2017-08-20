package no.fint.relations;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class FintResourceSupport extends ResourceSupport {
    @JsonUnwrapped
    private Object data;
}
