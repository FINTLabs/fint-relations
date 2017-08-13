package no.fint.relations

import no.fint.relations.config.FintRelationsProps
import org.springframework.core.env.Environment
import spock.lang.Specification

class FintLinkMapperSpec extends Specification {
    private no.fint.relations.FintLinkMapper fintLinkMapper
    private FintRelationsProps props
    private Environment environment

    void setup() {
        props = Mock(FintRelationsProps)
        environment = Mock(Environment) {
            acceptsProfiles(_ as String) >> true
        }
        fintLinkMapper = new no.fint.relations.FintLinkMapper(props: props, environment: environment)
        fintLinkMapper.init()
    }

    def "Get same link if no template value is provided"() {
        when:
        def link = fintLinkMapper.getLink('http://localhost/test')

        then:
        link == 'http://localhost/test'
    }

    def "Get link with base url when string template is provided"() {
        when:
        def link = fintLinkMapper.getLink('${no.fint.TestDto}/test')

        then:
        1 * props.getTestRelationBase() >> 'https://api.felleskomponent.no'
        link == 'https://api.felleskomponent.no/test'
    }

    def "Get link with configured props when string template is provided"() {
        given:
        fintLinkMapper = new no.fint.relations.FintLinkMapper(links: ['no.fint.TestDto': 'http://local'], environment: environment, props: props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${no.fint.TestDto}/test')

        then:
        1 * props.getTestRelationBase() >> 'https://api.felleskomponent.no'
        link == 'http://local/test'
    }

    def "Combine configured path with default base url"() {
        given:
        fintLinkMapper = new no.fint.relations.FintLinkMapper(links: ['no.fint.TestDto': '/id'], environment: environment, props: props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${no.fint.TestDto}')

        then:
        1 * props.getTestRelationBase() >> 'https://api.felleskomponent.no'
        link == 'https://api.felleskomponent.no/id'
    }
}
