package no.fint.relations.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "fint.relations")
public class FintRelationsProps {

    @Value("${server.port:0}")
    private int serverPort;

    @Value("${local.server.port:0}")
    private int localServerPort;

    @Getter
    private int port = 0;

    @Getter
    @Value("${fint.relations.default-base-url:https://api.felleskomponent.no}")
    private String relationBase;

    @Getter
    private Map<String, String> links = new HashMap<>();

    @Value("${fint.relations.test-base-url:http://localhost}")
    private String testRelationBase;

    @Getter
    @Value("${fint.relations.force-https:true}")
    private String forceHttps;

    @PostConstruct
    public void init() {
        if (localServerPort > 0) {
            port = localServerPort;
        } else if (serverPort > 0) {
            port = serverPort;
        }
    }

    public String getTestRelationBase() {
        if (testRelationBase.replace("://", "").contains(":")) {
            return testRelationBase;
        } else {
            return String.format("%s:%d", testRelationBase, port);
        }
    }
}
