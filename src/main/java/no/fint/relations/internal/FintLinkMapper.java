package no.fint.relations.internal;

import no.fint.model.relation.Relation;
import no.fint.relations.config.FintRelationsProps;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FintLinkMapper {

    private final Environment environment;

    private final FintRelationsProps props;

    private final boolean forceHttps;

    private final String defaultLink;

    @Qualifier("linkMapper")
    @Autowired(required = false)
    private Map<String, String> links = new HashMap<>();

    @Value("${server.context-path:}")
    private String contextPath;

    private StringSubstitutor strSubstitutor;

    public FintLinkMapper(Environment environment, FintRelationsProps props) {
        this.environment = environment;
        this.props = props;
        forceHttps = Boolean.parseBoolean(props.getForceHttps());
        defaultLink = getConfiguredLink();
    }

    @PostConstruct
    public void init() {
        Map<String, String> shortnameLinks = links.entrySet().stream().collect(Collectors.toMap(
                e -> Relation.createType(e.getKey()),
                Map.Entry::getValue
        ));

        Map<String, String> stringMap = new HashMap<>();
        stringMap.putAll(shortnameLinks);
        stringMap.putAll(links);
        strSubstitutor = new StringSubstitutor(stringMap);
    }

    public String getLink(String link) {

        if (link.startsWith("${") && link.contains("}")) {
            link = link.replace("}", String.format(":-%s}", defaultLink));
            link = strSubstitutor.replace(link);
        }

        if (link.startsWith("/")) {
            return defaultLink + link;
        }

        return populateProtocol(link);
    }

    private String getConfiguredLink() {
        return environment.acceptsProfiles("test") ? props.getTestRelationBase() : props.getRelationBase();
    }

    public Link populateProtocol(Link link) {
        String href = populateProtocol(link.getHref());
        return new Link(href, link.getRel());
    }

    public String populateProtocol(String href) {
        if (forceHttps && href.startsWith("http://")) {
            return href.replace("http://", "https://");
        } else {
            return href;
        }
    }

    public static String getName(Class<?> clazz) {
        return no.fint.model.resource.Link.getHrefPlaceholder(clazz);
    }

}
