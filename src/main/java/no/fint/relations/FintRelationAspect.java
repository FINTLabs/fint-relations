package no.fint.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintRelations;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
@Aspect
@Component
public class FintRelationAspect implements ApplicationContextAware {

    @Value("${relation-id-base:uri:https://api.felleskomponent.no/rel/}")
    private String relationBase;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Around("execution(* (@no.fint.relations.annotations.FintRelations *).*(..))")
    public Object fintRelationsEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelations fintRelations = metadata.getCallingClass().getAnnotation(FintRelations.class);
        return createSingleResponse(responseEntity.get(), metadata, fintRelations.rels());
    }


    @Around("execution(* (@no.fint.relations.annotations.FintRelation *).*(..))")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        if (isNotHalAcceptHeader()) {
            return response;
        }

        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelation[] relations = metadata.getCallingClass().getAnnotationsByType(FintRelation.class);

        Object body = responseEntity.get().getBody();
        if (body instanceof Collection) {
            return createCollectionResponse(responseEntity.get(), metadata, relations);
        } else {
            return createSingleResponse(responseEntity.get(), metadata, relations);
        }
    }

    private boolean isNotHalAcceptHeader() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
            return StringUtils.isEmpty(acceptHeader) && !(acceptHeader.startsWith("application/hal+json"));
        }
        return false;
    }

    private Optional<ResponseEntity> getResponseEntity(Object response) {
        if (response instanceof ResponseEntity) {
            return Optional.of((ResponseEntity) response);
        }
        return Optional.empty();
    }

    private ResponseEntity createSingleResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Link> links = new ArrayList<>();
        for (FintRelation relation : relations) {
            try {
                addRelations(responseEntity.getBody(), metadata, relation).ifPresent(links::add);
            } catch (NoSuchMethodException e) {
                log.info(e.getMessage());
            }
        }

        links.add(getSelfLink(metadata));
        Resource<?> resource = new Resource<>(responseEntity.getBody(), links);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(resource);
    }

    private ResponseEntity createCollectionResponse(ResponseEntity responseEntity, AspectMetadata metadata, FintRelation... relations) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Collection values = (Collection) responseEntity.getBody();
        List<Resource> resources = new ArrayList<>();
        for (Object value : values) {
            List<Link> links = new ArrayList<>();
            for (FintRelation relation : relations) {
                try {
                    addRelations(value, metadata, relation).ifPresent(links::add);
                } catch (NoSuchMethodException e) {
                    log.info(e.getMessage());
                }
            }

            String id = (String) PropertyUtils.getNestedProperty(value, metadata.getSelfId().id());
            Link selfLink = ControllerLinkBuilder.linkTo(metadata.getCallingClass()).slash(id).withSelfRel();
            links.add(selfLink);
            Resource<?> resource = new Resource<>(value, links);
            resources.add(resource);
        }

        FintResources embedded = new FintResources(values.size(), resources, getSelfLink(metadata));
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(embedded);
    }

    private Optional<Link> addRelations(Object body, AspectMetadata metadata, FintRelation relation) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Optional<Link> link = Optional.empty();
        String relationId = getRelationId(metadata, relation);
        Map<String, FintLinkMapper> beans = applicationContext.getBeansOfType(FintLinkMapper.class);
        if (beans.size() > 0) {
            Collection<FintLinkMapper> values = beans.values();
            Optional<FintLinkMapper> mapper = values.stream().filter(value -> value.type() == metadata.getSelfId().self()).findAny();
            Object property = PropertyUtils.getNestedProperty(body, metadata.getSelfId().id());
            Relation rel = new Relation();
            rel.setType(relationId);
            rel.setLeftKey((String) property);
            if (mapper.isPresent()) {
                link = Optional.ofNullable(mapper.get().createRelation(rel));
            }
        }
        return link;
    }

    private String getRelationId(AspectMetadata metadata, FintRelation relation) {
        String leftKeyName = metadata.getSelfId().self().getSimpleName().toLowerCase();
        String rightKeyName = relation.objectLink().getSimpleName().toLowerCase();
        return String.format("%s%s:%s", relationBase, leftKeyName, rightKeyName);
    }

    private Link getSelfLink(AspectMetadata metadata) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(metadata.getCallingClass(), metadata.getMethod(), metadata.getArguments());
        return linkBuilder.withSelfRel();
    }
}
