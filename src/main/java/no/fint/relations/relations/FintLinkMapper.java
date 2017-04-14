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
        String linkReplacement = strSubstitutor.replace(link);
        if (linkReplacement.startsWith("${") && linkReplacement.contains("}")) {
            String path = link.substring(link.indexOf("}") + 1, link.length());
            return getConfiguredLink(path);
        } else {
            return linkReplacement;
        }
    }

    private String getConfiguredLink(String path) {
        if (environment.acceptsProfiles("test")) {
            return props.getTestRelationBase() + path;
        } else {
            return props.getRelationBase() + path;
        }
    }

}
