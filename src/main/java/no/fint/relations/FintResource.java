package no.fint.relations;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FintResource extends ResourceSupport {
    @Setter
    private Object content;

    public <T> T getContent(Class<T> type) {
        return type.cast(content);
    }

    @JsonUnwrapped
    public Object getContent() {
        return content;
    }
}
