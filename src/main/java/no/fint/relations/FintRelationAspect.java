package no.fint.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.relations.FintRelationHal;
import no.fint.relations.relations.FintRelationJsonLd;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class FintRelationAspect {

    @Autowired
    private FintRelationHal fintRelHal;

    @Autowired
    private FintRelationJsonLd fintRelJsonLd;

    @Around("execution(* (@no.fint.relations.annotations.FintRelations *).*(..))")
    public Object fintRelationsEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelations relations = metadata.getCallingClass().getAnnotation(FintRelations.class);
        Object response = proceedingJoinPoint.proceed();
        if (hasJsonLdAcceptHeader()) {
            return fintRelJsonLd.addRelations(metadata, relations.value(), response);
        } else {
            return fintRelHal.addRelations(metadata, relations.value(), response);
        }
    }


    @Around("execution(* (@no.fint.relations.annotations.FintRelation *).*(..))")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        FintRelation[] relations = metadata.getCallingClass().getAnnotationsByType(FintRelation.class);
        Object response = proceedingJoinPoint.proceed();
        if (hasJsonLdAcceptHeader()) {
            return fintRelJsonLd.addRelations(metadata, relations, response);
        } else {
            return fintRelHal.addRelations(metadata, relations, response);
        }
    }

    private boolean hasJsonLdAcceptHeader() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
            return !StringUtils.isEmpty(acceptHeader) && (acceptHeader.startsWith("application/ld+json"));
        }
        return false;
    }

}
