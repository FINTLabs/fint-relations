package no.fint.relations.relations;

import no.fint.relations.config.FintRelationsProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FintLinkMapper {

    @Autowired
    private Environment environment;

    @Autowired
    private FintRelationsProps props;

    private Map<String, String> links = new HashMap<>();

    public String getLink(String link) {
        if (link.startsWith("{") && link.contains("}")) {
            int toIndex = link.indexOf("}");
            String path = link.substring(toIndex + 1, link.length());
            String linkClass = link.substring(1, toIndex);
            String linkVal = links.get(linkClass);
            if (linkVal == null) {
                return getConfiguredLink(path);
            } else {
                return linkVal + path;
            }

        }
        return link;
    }

    private String getConfiguredLink(String path) {
        if (environment.acceptsProfiles("test")) {
            return props.getTestRelationBase() + path;
        } else {
            return props.getRelationBase() + path;
        }
    }

}
