package no.fint.relations;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.relations.internal.FintLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class FintLinker<T extends FintLinks> {
    private final Class<T> resourceClass;

    @Autowired
    private FintLinkMapper linkMapper;

    protected FintLinker(Class<T> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public FintResources toResources(List<T> resources) {
        List<T> fintResources = resources.stream().map(this::toNestedResource).collect(Collectors.toList());
        return new FintResources<>(fintResources, self());
    }

    private T toNestedResource(T resource) {
        mapLinks(resource);
        return resource;
    }

    public T toResource(T resource) {
        mapLinks(resource);
        if (resource.getSelfLinks() == null || resource.getSelfLinks().isEmpty()) {
            String self = getSelfHref(resource);
            resource.addLink("self", Link.with(linkMapper.populateProtocol(self)));
        }

        return resource;
    }

    protected void mapLinks(FintLinks resource) {
        if (resource != null) {
            Map<String, List<Link>> links = resource.getLinks();
            links.values().forEach(
                    linkValues -> linkValues.forEach(
                            link -> link.setVerdi(linkMapper.getLink(link.getHref()))
                    )
            );

            List<FintLinks> nestedResources = resource.getNestedResources();
            if (nestedResources.size() > 0) {
                nestedResources.forEach(this::mapLinks);
            }
        }
    }

    public String createHrefWithId(Object id, String path) {
        return linkMapper.getLink(Link.with(resourceClass, path, id.toString()).getHref());
    }

    public String self() {
        return linkMapper.getLink(Link.with(resourceClass).getHref());
    }

    public abstract String getSelfHref(T resource);

}
