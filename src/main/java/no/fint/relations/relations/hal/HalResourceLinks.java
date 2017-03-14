package no.fint.relations.relations.hal;

import com.google.common.collect.Lists;
import no.fint.relation.model.Relation;
import no.fint.relations.AspectMetadata;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class HalResourceLinks implements ApplicationContextAware {

    @Value("${relation-id-base:https://api.felleskomponent.no/rel/}")
    private String relationBase;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<Link> getLinks(Object body, AspectMetadata metadata, FintRelation relation) {
        List<Link> links = new ArrayList<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(FintLinkMapper.class);
        String relationId = getRelationId(metadata, relation);
        Collection<?> linkMappers = beans.values();
        for (Object linkMapper : linkMappers) {
            Method[] methods = linkMapper.getClass().getMethods();
            for (Method method : methods) {
                FintLinkRelation fintLinkRelation = AnnotationUtils.getAnnotation(method, FintLinkRelation.class);
                String linkRelationId = getRelationId(fintLinkRelation);
                if (fintLinkRelation != null && relationId.equals(linkRelationId)) {
                    validateLinkMapperMethod(method);

                    List<Link> mapperLinks = getLinksFromMapper(body, metadata.getSelfId().id(), relationId, linkMapper, method);
                    if (mapperLinks != null) {
                        links.addAll(mapperLinks);
                    }
                }
            }
        }
        return links;
    }

    @SuppressWarnings("unchecked")
    private List<Link> getLinksFromMapper(Object body, String selfId, String relationId, Object linkMapper, Method method) {
        try {
            Object property = PropertyUtils.getNestedProperty(body, selfId);
            Relation rel = new Relation();
            rel.setType(relationBase + relationId);
            rel.setLeftKey((String) property);

            Object response = method.invoke(linkMapper, rel);
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

    private void validateLinkMapperMethod(Method method) {
        int parameterCount = method.getParameterCount();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        if (parameterCount != 1 || parameterTypes[0] != Relation.class || (returnType != Link.class && returnType != List.class)) {
            throw new IllegalArgumentException(String.format("The method %s needs to take a Relation object as input and return a Link or a list of Link objects", method.getName()));
        }
    }

    private String getRelationId(AspectMetadata metadata, FintRelation relation) {
        String leftKeyClass = metadata.getSelfId().self().getSimpleName().toLowerCase();
        String leftKeyProperty = metadata.getSelfId().id();
        String rightKeyClass = relation.objectLink().getSimpleName().toLowerCase();
        String rightKeyProperty = relation.id();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }

    private String getRelationId(FintLinkRelation fintLinkRelation) {
        if (fintLinkRelation == null) {
            return null;
        }

        String leftKeyClass = fintLinkRelation.leftObject().getSimpleName().toLowerCase();
        String leftKeyProperty = fintLinkRelation.leftId();
        String rightKeyClass = fintLinkRelation.rightObject().getSimpleName().toLowerCase();
        String rightKeyProperty = fintLinkRelation.rightId();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }
}
