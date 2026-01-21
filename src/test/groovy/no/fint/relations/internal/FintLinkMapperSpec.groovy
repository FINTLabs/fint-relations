package no.fint.relations.internal

import no.fint.relations.config.FintRelationsProps
import org.springframework.core.env.Environment
import spock.lang.Specification

class FintLinkMapperSpec extends Specification {
    private FintLinkMapper fintLinkMapper
    private FintRelationsProps props
    private Environment environment

    void setup() {
        props = Mock(FintRelationsProps)
        environment = Mock(Environment)

        environment.acceptsProfiles(_ as String) >> true
        props.getForceHttps() >> "true"
    }

    def "Get same link if no template value is provided"() {
        given:
        props = Mock(FintRelationsProps)

        props.getForceHttps() >> "false"
        props.getTestRelationBase() >> 'https://default.url'

        fintLinkMapper = new FintLinkMapper(environment, props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('http://localhost/test')

        then:
        link == 'http://localhost/test'
    }

    def "Get link with base url when string template is provided"() {
        given:
        props.getTestRelationBase() >> 'https://api.felleskomponent.no'

        fintLinkMapper = new FintLinkMapper(environment, props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${no.fint.TestDto}/test')

        then:
        link == 'https://api.felleskomponent.no/test'
    }

    def "Get link with configured props when string template is provided"() {
        given:
        props = Mock(FintRelationsProps)

        props.getForceHttps() >> "false"
        props.getTestRelationBase() >> 'http://default'

        fintLinkMapper = new FintLinkMapper(environment, props)

        fintLinkMapper.links = ['testdto': 'http://local']
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${testdto}/test')

        then:
        link == 'http://local/test'
    }

    def "Combine configured path with default base url"() {
        given:
        props.getTestRelationBase() >> 'https://api.felleskomponent.no'

        fintLinkMapper = new FintLinkMapper(environment, props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${testdto}/id')

        then:
        link == 'https://api.felleskomponent.no/id'
    }

    def "Empty template is replaced with base url"() {
        given:
        props.getTestRelationBase() >> 'https://api.felleskomponent.no'

        fintLinkMapper = new FintLinkMapper(environment, props)
        fintLinkMapper.init()

        when:
        def link = fintLinkMapper.getLink('${}/id')

        then:
        link == 'https://api.felleskomponent.no/id'
    }

    def "Create links from simple and full class name"() {
        given:
        def fullClassName = 'no.fint.model.testutils.Person'
        def simpleClassName = 'testutils.person'
        def linksMap = [:]
        linksMap[fullClassName] = 'http://localhost:8080'

        props = Mock(FintRelationsProps)

        props.getForceHttps() >> "false"
        props.getTestRelationBase() >> 'http://default'

        fintLinkMapper = new FintLinkMapper(environment, props)
        fintLinkMapper.links = linksMap
        fintLinkMapper.init()

        when:
        def fullClassNameLink = fintLinkMapper.getLink("\${${fullClassName}}/test1")
        def simpleClassNameLink = fintLinkMapper.getLink("\${${simpleClassName}}/test2")

        then:
        fullClassNameLink == 'http://localhost:8080/test1'
        simpleClassNameLink == 'http://localhost:8080/test2'
    }

    def "Add default base url to relative links"() {
        given:
        props.getTestRelationBase() >> 'http://localhost:8080'

        fintLinkMapper = new FintLinkMapper(environment, props)
        fintLinkMapper.init()

        when:
        def relativeLink = fintLinkMapper.getLink('/some/relative/path')

        then:
        relativeLink == 'http://localhost:8080/some/relative/path'
    }
}