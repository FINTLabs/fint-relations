package no.fint.relations.relations.hal;

import com.google.common.collect.Lists;
import no.fint.model.relation.Relation;
import no.fint.model.relation.RelationUtil;
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
        Optional<String> relationId = RelationUtil.getConstantValue(metadata.getFintSelf().self(), relation.value());
        if (relationId.isPresent()) {
            Optional<FintRelationObjectMethod> fintRelation = fintMappers.getMethod(relationId.get());
            if (fintRelation.isPresent()) {
                List<Link> mapperLinks = getLinksFromMapper(body, metadata.getFintSelf().id(), relationId.get(), fintRelation.get());
                if (mapperLinks != null) {
                    links.addAll(mapperLinks);
                }
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
            rel.setMain((String) property);

            Object response = fintRelation.getMethod().invoke(fintRelation.getLinkMapper(), rel);
            if (response instanceof List) {
                return (List<Link>) response;
            } else if (response instanceof Link) {
                return Lists.newArrayList((Link) response);
            } else {
                return Collections.emptyList();
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("The id (%s) in @FintSelf was not found", selfId), e);
        }
    }
}
