package no.fint.relations

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
            acceptsProfiles(_ as String) >> true
        }
        fintLinkMapper = new FintLinkMapper(props: props, environment: environment)
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
        def tempProps = Mock(FintRelationsProps) {
            getLinks() >> ['testdto': 'http://local']
        }
        fintLinkMapper = new FintLinkMapper(environment: environment, props: tempProps)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${testdto}/test')

        then:
        1 * tempProps.getTestRelationBase() >> 'https://api.felleskomponent.no'
        link == 'http://local/test'
    }

    def "Combine configured path with default base url"() {
        given:
        def tempProps = Mock(FintRelationsProps) {
            getLinks() >> ['testdto': '/id']
        }
        fintLinkMapper = new FintLinkMapper(environment: environment, props: tempProps)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${testdto}')

        then:
        1 * tempProps.getTestRelationBase() >> 'https://api.felleskomponent.no'
        link == 'https://api.felleskomponent.no/id'
    }
}
