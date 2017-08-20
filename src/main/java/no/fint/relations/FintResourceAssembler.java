package no.fint.relations;

import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.relations.internal.FintLinkMapper;
import no.fint.relations.internal.FintResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public abstract class FintResourceAssembler<T> extends ResourceAssemblerSupport<FintResource<T>, FintResourceSupport> {
    private Class<?> controllerClass;

    @Autowired
    private FintLinkMapper fintLinkMapper;

    public FintResourceAssembler(Class<?> controllerClass) {
        super(controllerClass, FintResourceSupport.class);
        this.controllerClass = controllerClass;
    }

    public FintResourceSupport createResourceWithId(Object id, FintResource<T> entity, String path) {
        FintResourceSupport fintResourceSupport = instantiateResource(entity);
        fintResourceSupport.add(linkTo(controllerClass).slash(path).slash(id).withSelfRel());
        return fintResourceSupport;
    }

    @Override
    public FintResourceSupport toResource(FintResource<T> fintResource) {
        List<Relation> relations = fintResource.getRelations();
        List<Link> links = relations.stream().map(relation -> {
            String link = fintLinkMapper.getLink(relation.getLink());
            return new Link(link, relation.getRelationName());
        }).collect(Collectors.toList());

        FintResourceSupport resource = assemble(fintResource.getResource(), fintResource);
        resource.setData(fintResource.getResource());

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
        List<FintResourceSupport> resources = toResources(fintResources);
        return ResponseEntity.ok(new FintResources<>(resources, self()));
    }

    public ResponseEntity resource(FintResource<T> fintResource) {
        return ResponseEntity.ok(toResource(fintResource));
    }

    public abstract FintResourceSupport assemble(T resource, FintResource<T> fintResource);

}
