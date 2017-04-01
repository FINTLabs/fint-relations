package no.fint.relations.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;

public class FintRelationsProps {

    @LocalServerPort
    private int port;

    @Getter
    @Value("${fint.relations.default-base-url:https://api.felleskomponent.no}")
    private String relationBase;

    @Value("${fint.relations.test-base-url:http://localhost}")
    private String testRelationBase;

    @Getter
    @Value("${fint.relations.force-https:true}")
    private String forceHttps;

    public String getTestRelationBase() {
        if (testRelationBase.replace("://", "").contains(":")) {
            return testRelationBase;
        } else {
            return String.format("%s:%d", testRelationBase, port);
        }

    }
}
