package no.fint.relations.integration.testutils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fint.model.relation.Identifiable;
import no.fint.model.relation.RelationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Identifiable {
    public static final String REL_ID_ADDRESS = new RelationType.Builder()
            .namespace("fint.no").relationName("address").main(Person.class, "name").related(Address.class, "street").buildTypeString();

    public static final String REL_ID_DIFFERENTPROPERTY = new RelationType.Builder()
            .namespace("fint.no").relationName("differentproperty").main(Person.class, "name").related(Address.class, "street").buildTypeString();

    public static final String REL_ID_INVALIDLINK = new RelationType.Builder()
            .namespace("fint.no").relationName("invalidLink").main(Person.class, "name").related(Address.class, "street").buildTypeString();

    public static final String REL_ID_MAINNUMBER = new RelationType.Builder()
            .namespace("fint.no").relationName("mainNumber").main(Person.class, "name").related(Telephone.class, "mainNumber").buildTypeString();

    public static final String REL_ID_SECONDARYNUMBER = new RelationType.Builder()
            .namespace("fint.no").relationName("secondaryNumber").main(Person.class, "name").related(Telephone.class, "secondaryNumber").buildTypeString();

    private String name;
    private String name2;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return this.getName();
    }
}
