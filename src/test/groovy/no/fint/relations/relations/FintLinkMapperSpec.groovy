package no.fint.relations.relations

import no.fint.relations.config.FintRelationsProps
import org.springframework.core.env.Environment
import spock.lang.Specification

class FintLinkMapperSpec extends Specification {
    private FintLinkMapper fintLinkMapper
    private FintRelationsProps props
    private Environment environment

    void setup() {
        props = Mock(FintRelationsProps)
        environment = Mock(Environment) {
            acceptsProfiles() >> true
        }
        fintLinkMapper = new FintLinkMapper(props: props, environment: environment)
    }

    def "Get same link if no template value is provided"() {
        when:
        def link = fintLinkMapper.getLink('http://localhost/test')

        then:
        link == 'http://localhost/test'
    }

    def "Get link with base url when string template is provided"() {
        when:
        def link = fintLinkMapper.getLink('{no.fint.TestDto}/test')

        then:
        1 * props.getRelationBase() >> 'https://api.felleskomponent.no'
        link == 'https://api.felleskomponent.no/test'
    }

    def "Get link with configured props when string template is provided"() {
        given:
        fintLinkMapper = new FintLinkMapper(links: ['no.fint.TestDto': 'http://local'])

        when:
        def link = fintLinkMapper.getLink('{no.fint.TestDto}/test')

        then:
        link == 'http://local/test'
    }
}
