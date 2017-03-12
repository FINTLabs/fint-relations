package no.fint.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

@Data
@EqualsAndHashCode(callSuper = true)
public class FintResources extends Resources {
    @JsonProperty("total_items")
    private int totalItems;

    @SuppressWarnings("unchecked")
    public FintResources(int totalItems, Iterable content, Link... links) {
        super(content, links);
        this.totalItems = totalItems;
    }
}
