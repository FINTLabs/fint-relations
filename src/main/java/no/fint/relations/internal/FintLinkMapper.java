package no.fint.relations.internal;

import no.fint.model.relation.Relation;
import no.fint.relations.config.FintRelationsProps;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FintLinkMapper {

    @Autowired
    private Environment environment;

    @Autowired
    private FintRelationsProps props;

    @Qualifier("linkMapper")
    @Autowired(required = false)
    private Map<String, String> links = new HashMap<>();

    @Value("${server.context-path:}")
    private String contextPath;

    private StrSubstitutor strSubstitutor;

    @PostConstruct
    public void init() {
        Map<String, String> shortnameLinks = links.entrySet().stream().collect(Collectors.toMap(
                e -> Relation.createType(e.getKey()),
                Map.Entry::getValue
        ));

        Map<String, String> stringMap = new HashMap<>();
        stringMap.putAll(shortnameLinks);
        stringMap.putAll(links);
        strSubstitutor = new StrSubstitutor(stringMap);
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

    public List<Link> populateProtocol(List<Link> links) {
        if (Boolean.valueOf(props.getForceHttps())) {
            return links.stream().map(this::populateProtocol).collect(Collectors.toList());
        } else {
            return links;
        }
    }

    public Link populateProtocol(Link link) {
        if (Boolean.valueOf(props.getForceHttps())) {
            String href = link.getHref();
            href = href.replace("http://", "https://");
            return new Link(href, link.getRel());
        } else {
            return link;
        }
    }

    public static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

}
