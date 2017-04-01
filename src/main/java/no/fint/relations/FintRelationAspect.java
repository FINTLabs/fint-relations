package no.fint.relations;

import lombok.extern.slf4j.Slf4j;
import no.fint.relations.relations.hal.FintRelationHal;
import no.fint.relations.relations.jsonld.FintRelationJsonLd;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Aspect
public class FintRelationAspect {

    @Autowired
    private FintRelationHal fintRelHal;

    @Autowired
    private FintRelationJsonLd fintRelJsonLd;

    @Around("@annotation(no.fint.relations.annotations.FintRelations)")
    public Object fintRelationEndpoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        AspectMetadata metadata = AspectMetadata.with(proceedingJoinPoint);
        Object response = proceedingJoinPoint.proceed();
        Optional<ResponseEntity> responseEntity = getResponseEntity(response);
        if (!responseEntity.isPresent()) {
            return response;
        }

        if (hasJsonLdAcceptHeader()) {
            return fintRelJsonLd.addRelations(metadata, responseEntity.get());
        } else {
            return fintRelHal.addRelations(metadata, responseEntity.get());
        }
    }

    private Optional<ResponseEntity> getResponseEntity(Object response) {
        if (response instanceof ResponseEntity) {
            return Optional.of((ResponseEntity) response);
        }
        log.warn("ResponseEntity return type not found, type: {}", response.getClass().getSimpleName());
        return Optional.empty();
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
