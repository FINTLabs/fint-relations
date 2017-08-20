package no.fint.relations.internal;

import org.springframework.hateoas.RelProvider;
import org.springframework.util.StringUtils;

public class FintRelProvider implements RelProvider {
    
    @Override
    public String getItemResourceRelFor(Class<?> type) {
        return StringUtils.uncapitalize(type.getSimpleName());
    }

    @Override
    public String getCollectionResourceRelFor(Class<?> type) {
        return "_entries";
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
