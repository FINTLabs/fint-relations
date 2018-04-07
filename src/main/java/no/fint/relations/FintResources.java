package no.fint.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.fint.model.resource.Link;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class FintResources {

    @JsonProperty("_embedded")
    private Map<String, List<FintResource>> embedded = new LinkedHashMap<>();

    @JsonProperty("_links")
    private Map<String, List<Link>> links = new LinkedHashMap<>();

    public FintResources() {
        embedded.put("_entries", new ArrayList<>());
    }

    public FintResources(List<FintResource> resources, String selfLink) {
        this();
        embedded.get("_entries").addAll(resources);

        List<Link> selfLinks = new ArrayList<>();
        selfLinks.add(Link.with(selfLink));
        links.put("self", selfLinks);
    }

    public void addResource(FintResource resource) {
        embedded.get("_entries").add(resource);
    }

    @JsonProperty("total_items")
    public int getTotalItems() {
        return embedded.values().size();
    }

}