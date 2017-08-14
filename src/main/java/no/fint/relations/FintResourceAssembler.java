package no.fint.relations;

import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public abstract class FintResourceAssembler<T, D extends ResourceSupport> extends ResourceAssemblerSupport<FintResource<T>, D> {
    private final Class<?> controllerClass;

    @Autowired
    private FintLinkMapper fintLinkMapper;

    public FintResourceAssembler(Class<?> controllerClass, Class<D> resourceType) {
        super(controllerClass, resourceType);
        this.controllerClass = controllerClass;
    }

    public D createResourceWithId(Object id, FintResource<T> entity, String path) {
        D instance = instantiateResource(entity);
        instance.add(linkTo(controllerClass).slash(path).slash(id).withSelfRel());
        return instance;
    }

    @Override
    public D toResource(FintResource<T> fintResource) {
        List<Relation> relations = fintResource.getRelations();
        List<Link> links = relations.stream().map(relation -> {
            String link = fintLinkMapper.getLink(relation.getLink());
            return new Link(link, relation.getRelationName());
        }).collect(Collectors.toList());

        D resource = mapToResource(fintResource);
        List<Link> resourceLinks = resource.getLinks();
        links.addAll(resourceLinks);
        resource.removeLinks();

        resource.add(fintLinkMapper.populateProtocol(links));
        return resource;
    }

    public Link self() {
        Link self = ControllerLinkBuilder.linkTo(controllerClass).withSelfRel();
        return fintLinkMapper.populateProtocol(self);
    }

    public ResponseEntity resources(List<FintResource<T>> fintResources) {
        List<D> resources = toResources(fintResources);
        return ResponseEntity.ok(new FintResources<>(resources, self()));
    }

    public ResponseEntity resource(FintResource<T> fintResource) {
        return ResponseEntity.ok(toResource(fintResource));
    }

    public abstract D mapToResource(FintResource<T> resource);

}
