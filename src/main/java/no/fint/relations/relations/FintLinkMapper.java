package no.fint.relations.relations;

import no.fint.relations.config.FintRelationsProps;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class FintLinkMapper {

    @Autowired
    private Environment environment;

    @Autowired
    private FintRelationsProps props;

    @Qualifier("linkMapper")
    @Autowired(required = false)
    private Map<String, String> links = new HashMap<>();

    private StrSubstitutor strSubstitutor;

    @PostConstruct
    public void init() {
        strSubstitutor = new StrSubstitutor(links);
    }

    public String getLink(String link) {
        if (link.startsWith("${") && link.contains("}")) {
            String defaultLink = getConfiguredLink();
            link = link.replace("}", String.format(":-%s}", defaultLink));
            link = strSubstitutor.replace(link);
            if (link.startsWith("/")) {
                return defaultLink + link;
            }
        }

        return link;
    }

    private String getConfiguredLink() {
        return environment.acceptsProfiles("test") ? props.getTestRelationBase() : props.getRelationBase();
    }

}
