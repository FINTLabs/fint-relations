package no.fint.relations.integration.testutils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fint.model.relation.FintModel;
import no.fint.model.relation.Relation;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Person implements FintModel {
    public enum Relasjonsnavn {
        ADDRESS,
        DIFFERENTPROPERTY,
        INVALIDLINK,
        MAINNUMBER,
        SECONDARYNUMBER
    }

    private String name;
    private String name2;
    private List<Relation> relasjoner = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return this.getName();
    }

    @Override
    public void addRelasjon(Relation relation) {
        this.relasjoner.add(relation);
    }
}
