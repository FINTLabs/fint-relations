package no.fint.relations.relations.hal;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.Link;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class FintMappers implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Map<String, FintRelationObjectMethod> mapperMethods;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        mapperMethods = new HashMap<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(FintLinkMapper.class);
        Collection<?> linkMappers = beans.values();
        for (Object linkMapper : linkMappers) {
            addRelationObjects(linkMapper);
        }
    }

    private void addRelationObjects(Object linkMapper) {
        Method[] methods = linkMapper.getClass().getMethods();
        for (Method method : methods) {
            FintLinkMapper fintLinkerMapper = linkMapper.getClass().getAnnotation(FintLinkMapper.class);
            FintLinkRelation fintLinkRelation = AnnotationUtils.getAnnotation(method, FintLinkRelation.class);
            String relationId = getRelationId(fintLinkerMapper, fintLinkRelation);
            if (relationId != null) {
                validateLinkMapperMethod(method);
                mapperMethods.put(relationId, new FintRelationObjectMethod(method, linkMapper));
            }
        }
    }

    public Map<String, FintRelationObjectMethod> getLinkMappers() {
        return mapperMethods;
    }

    public Optional<FintRelationObjectMethod> getMethod(String relationId) {
        FintRelationObjectMethod objectMethod = mapperMethods.get(relationId);
        if (objectMethod == null) {
            log.warn("Unable to find relation for {}", relationId);
            return Optional.empty();
        } else {
            return Optional.of(mapperMethods.get(relationId));
        }
    }

    private void validateLinkMapperMethod(Method method) {
        int parameterCount = method.getParameterCount();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        if (parameterCount != 1 || parameterTypes[0] != Relation.class || (returnType != Link.class && returnType != List.class)) {
            log.error("The method {} needs to take a Relation object as input and return a Link or a list of Link objects", method.getName());
        }
    }

    private String getRelationId(FintLinkMapper fintLinkMapper, FintLinkRelation fintLinkRelation) {
        if (fintLinkRelation == null) {
            return null;
        }

        String leftKeyClass = fintLinkMapper.leftObject().getSimpleName().toLowerCase();
        String leftKeyProperty = fintLinkMapper.leftId();
        String rightKeyClass = fintLinkRelation.rightObject().getSimpleName().toLowerCase();
        String rightKeyProperty = fintLinkRelation.rightId();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }

}
