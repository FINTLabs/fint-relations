package no.fint.relations.relations.hal;

import com.google.common.collect.Lists;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.config.FintRelationsProps;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HalResourceLinks {

    @Autowired
    private FintRelationsProps props;

    @Autowired
    private FintMappers fintMappers;

    public List<Link> getLinks(Object body, AspectMetadata metadata, FintRelation relation) {
        List<Link> links = new ArrayList<>();
        String relationId = getRelationId(metadata, relation);
        Optional<FintRelationObjectMethod> fintRelation = fintMappers.getMethod(relationId);
        if (fintRelation.isPresent()) {
            List<Link> mapperLinks = getLinksFromMapper(body, metadata.getSelfId().id(), relationId, fintRelation.get());
            if (mapperLinks != null) {
                links.addAll(mapperLinks);
            }
        }

        return links;
    }

    @SuppressWarnings("unchecked")
    private List<Link> getLinksFromMapper(Object body, String selfId, String relationId, FintRelationObjectMethod fintRelation) {
        try {
            Object property = PropertyUtils.getNestedProperty(body, selfId);
            Relation rel = new Relation();
            rel.setType(props.getRelationBase() + relationId);
            rel.setLeftKey((String) property);

            Object response = fintRelation.getMethod().invoke(fintRelation.getLinkMapper(), rel);
            if (response instanceof List) {
                return (List<Link>) response;
            } else if (response instanceof Link) {
                return Lists.newArrayList((Link) response);
            } else {
                return Collections.emptyList();
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("The id (%s) in @FintSelfId was not found", selfId), e);
        }
    }

    private String getRelationId(AspectMetadata metadata, FintRelation relation) {
        String leftKeyClass = metadata.getSelfId().self().getSimpleName().toLowerCase();
        String leftKeyProperty = metadata.getSelfId().id();
        String rightKeyClass = relation.objectLink().getSimpleName().toLowerCase();
        String rightKeyProperty = relation.id();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }
}
