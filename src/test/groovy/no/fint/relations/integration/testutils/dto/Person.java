package no.fint.relations.integration.testutils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fint.model.relation.Identifiable;

@Data
@NoArgsConstructor
public class Person implements Identifiable {
    public enum Relasjonsnavn {
        ADDRESS,
        DIFFERENTPROPERTY,
        INVALIDLINK,
        MAINNUMBER,
        SECONDARYNUMBER
    }

    private String name;
    private String name2;

    public Person(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return this.getName();
    }
}
