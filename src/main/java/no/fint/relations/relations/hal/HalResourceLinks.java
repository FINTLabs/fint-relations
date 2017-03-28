package no.fint.relations.relations.hal;

import com.google.common.collect.Lists;
import no.fint.model.relation.Identifiable;
import no.fint.model.relation.Relation;
import no.fint.model.relation.RelationUtil;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class HalResourceLinks {

    @Autowired
    private FintMappers fintMappers;

    private final Map<String, String> constants = new HashMap<>();

    public List<Link> getLinks(Identifiable identifiable, AspectMetadata metadata, FintRelation relation) {
        List<Link> links = new ArrayList<>();
        Optional<String> relationId = getConstantValue(metadata.getFintSelf().value(), relation.value());
        if (relationId.isPresent()) {
            Optional<FintRelationObjectMethod> fintRelation = fintMappers.getMethod(relationId.get());
            if (fintRelation.isPresent()) {
                List<Link> mapperLinks = getLinksFromMapper(identifiable, identifiable.getId(), relationId.get(), fintRelation.get());
                if (mapperLinks != null) {
                    links.addAll(mapperLinks);
                }
            }
        }

        return links;
    }

    private Optional<String> getConstantValue(Class<?> clazz, String constant) {
        String id = String.format("%s.%s", clazz.getSimpleName(), constant);
        String constantValue = constants.get(id);
        if (constantValue == null) {
            Optional<String> val = RelationUtil.getConstantValue(clazz, constant);
            if (val.isPresent()) {
                constants.put(id, val.get());
                return val;
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(constantValue);
    }

    @SuppressWarnings("unchecked")
    private List<Link> getLinksFromMapper(Identifiable identifiable, String selfId, String relationId, FintRelationObjectMethod fintRelation) {
        try {
            Relation rel = new Relation();
            rel.setType(relationId);
            rel.setMain(identifiable.getId());

            Object response = fintRelation.getMethod().invoke(fintRelation.getLinkMapper(), rel);
            if (response instanceof List) {
                return (List<Link>) response;
            } else if (response instanceof Link) {
                return Lists.newArrayList((Link) response);
            } else {
                return Collections.emptyList();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Exception when trying to call method on the @FintLinkMapper, id: %s", selfId), e);
        }
    }
}
