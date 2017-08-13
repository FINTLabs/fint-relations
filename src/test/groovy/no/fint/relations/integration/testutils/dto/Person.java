package no.fint.relations.integration.testutils.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
    public enum Relasjonsnavn {
        ADDRESS
    }

    private String name;

    public Person(String name) {
        this.name = name;
    }
}
