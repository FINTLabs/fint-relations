package no.fint.relations;

import no.fint.model.resource.AbstractCollectionResources;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.relations.internal.FintLinkMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class FintLinker<T extends FintLinks> {
    private final Class<T> resourceClass;

    @Autowired
    private FintLinkMapper linkMapper;

    protected FintLinker(Class<T> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public abstract AbstractCollectionResources<T> toResources(Collection<T> resources);

    private T toNestedResource(T resource) {
        mapLinks(resource);
        return resource;
    }

    public T toResource(T resource) {
        mapLinks(resource);
        if (resource.getSelfLinks() == null || resource.getSelfLinks().isEmpty()) {
            getAllSelfHrefs(resource)
                    .filter(StringUtils::isNotBlank)
                    .map(linkMapper::populateProtocol)
                    .map(Link::with)
                    .forEach(link -> resource.addLink("self", link));
        }

        return resource;
    }

    protected void mapLinks(FintLinks resource) {
        if (resource != null) {
            resource.getLinks().values().stream().filter(Objects::nonNull).flatMap(List::stream).filter(Objects::nonNull).forEach(
                    link -> link.setVerdi(linkMapper.getLink(link.getHref()))
            );

            resource.getNestedResources().forEach(this::mapLinks);
        }
    }

    public String createHrefWithId(Object id, String path) {
        return linkMapper.getLink(Link.with(resourceClass, path, id.toString()).getHref());
    }

    public String self() {
        return linkMapper.getLink(Link.with(resourceClass).getHref());
    }

    public abstract String getSelfHref(T resource);

    public Stream<String> getAllSelfHrefs(T resource) {
        return Stream.of(getSelfHref(resource));
    }

}
