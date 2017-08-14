package no.fint.relations;

import no.fint.relations.config.FintRelationsProps;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private Map<String, String> linkMappers = new HashMap<>();

    private StrSubstitutor strSubstitutor;

    @PostConstruct
    public void init() {
        if (linkMappers.size() == 0) {
            strSubstitutor = new StrSubstitutor(props.getLinks());
        } else {
            strSubstitutor = new StrSubstitutor(linkMappers);
        }
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

}
