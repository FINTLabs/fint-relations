package no.fint.relations.relations;

import no.fint.model.relation.Identifiable;
import no.fint.relations.annotations.FintSelf;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class FintRelationsVerifier implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> fintSelfAnnotations = applicationContext.getBeansWithAnnotation(FintSelf.class);
        Collection<Object> beans = fintSelfAnnotations.values();
        for (Object bean : beans) {
            FintSelf fintSelf = AnnotationUtils.findAnnotation(bean.getClass(), FintSelf.class);
            Class<?> type = fintSelf.type();
            String property = fintSelf.property();
            isConfiguredWithIdentificator(type);

            Field[] fields = type.getDeclaredFields();
            Optional<Field> field = Arrays.stream(fields).filter(f -> f.getName().equals(property)).findAny();
            if (!field.isPresent()) {
                throw new IllegalArgumentException(String.format("@FintSelf requires type and property, the type %s does not have the property %s", type, property));
            }
        }

    }

    private void isConfiguredWithIdentificator(Class<?> type) {
        try {
            Object value = type.newInstance();
            if (!(value instanceof Identifiable)) {
                throw new IllegalArgumentException(String.format("The type %s must implement Identifiable", type.getSimpleName()));
            }

            Identifiable identifiable = (Identifiable) value;
            identifiable.getId();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Verification of type %s failed", type.getSimpleName()), e);
        }
    }
}
