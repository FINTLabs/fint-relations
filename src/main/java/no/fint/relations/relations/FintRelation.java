package no.fint.relations.relations;

import lombok.Getter;

@Getter
public class FintRelation {
    private String namespace;
    private String mainClass;
    private String relationName;
    private String mainClassId;
    private String relatedClassId;

    public FintRelation(String relation) {
        String[] parts = relation.split(":");
        if (parts.length == 6) {
            namespace = parts[1];
            mainClass = parts[2];
            relationName = parts[3];
            mainClassId = parts[4];
            relatedClassId = parts[5];
        } else {
            throw new IllegalArgumentException(String.format("The relation is not valid: %s", relation));
        }
    }

    public FintRelation(String namespace, Class<?> main, String mainClassId, Class<?> related, String relatedClassId) {
        this.namespace = namespace;
        this.mainClass = main.getSimpleName().toLowerCase();
    }
}
