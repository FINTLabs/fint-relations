package no.fint.relations;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.fint.model.relation.FintResource;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FintResourceCompatibility {

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public boolean isFintResourceData(List<?> data) {
        if (data == null || data.isEmpty()) {
            return false;
        }

        return objectMapper.valueToTree(data.get(0)).hasNonNull("resource");
    }

    public <T extends FintLinks> List<T> convertResourceData(List<?> data, Class<T> cls) {
        JavaType sourceType = objectMapper.getTypeFactory().constructFromCanonical("java.util.List<no.fint.model.relation.FintResource<" + cls.getName() + ">>");
        List<FintResource<T>> original = objectMapper.convertValue(data, sourceType);
        return original.stream().map(fintResource -> {
            T resource = fintResource.getResource();
            fintResource.getRelations().forEach(relation -> resource.addLink(relation.getRelationName(), Link.with(relation.getLink())));
            return resource;
        }).collect(Collectors.toList());
    }
}
