package no.fint.relations

import no.fint.relations.config.FintRelationsProps
import no.fint.relations.integration.testutils.dto.Person
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
        1 * tempProps.getTestRelationBase() >> 'http://local'
        link == 'http://local/test'
    }

    def "Combine configured path with default base url"() {
        given:
        fintLinkMapper = new FintLinkMapper(environment: environment, props: props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${testdto}/id')

        then:
        1 * props.getTestRelationBase() >> 'https://api.felleskomponent.no'
        link == 'https://api.felleskomponent.no/id'
    }

    def "Create links from simple and full class name"() {
        given:
        def fullClassName = Person.getName()
        def simpleClassName = Person.getSimpleName().toLowerCase()
        def links = [:]
        links[fullClassName] = 'http://localhost:8080'

        fintLinkMapper = new FintLinkMapper(links: links, environment: environment, props: props)
        fintLinkMapper.init()

        when:
        def fullClassNameLink = fintLinkMapper.getLink("\${${fullClassName}}/test1")
        def simpleClassNameLink = fintLinkMapper.getLink("\${${simpleClassName}}/test2")

        then:
        fullClassNameLink == 'http://localhost:8080/test1'
        simpleClassNameLink == 'http://localhost:8080/test2'
    }
}
