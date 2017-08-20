package no.fint.relations.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FintResources<T> extends Resources<T> {
    @JsonProperty("total_items")
    private int totalItems;

    public FintResources(List<T> content, Link... links) {
        super(content, links);
        this.totalItems = content.size();
    }
}
