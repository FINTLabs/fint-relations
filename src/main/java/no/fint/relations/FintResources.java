package no.fint.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

@Data
public class FintResources extends Resources {
    @JsonProperty("total_items")
    private int totalItems;

    public FintResources(int totalItems, Iterable content, Link... links) {
        super(content, links);
        this.totalItems = totalItems;
    }
}
