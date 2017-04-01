package no.fint.relations.config

import spock.lang.Specification

class FintRelationsPropsSpec extends Specification {

    def "Get local server port when running with test profile"() {
        when:
        def props = new FintRelationsProps(testRelationBase: 'http://localhost', port: 1234)

        then:
        props.testRelationBase == 'http://localhost:1234'
    }

    def "Get local server address when running with test profile"() {
        when:
        def props = new FintRelationsProps(testRelationBase: 'http://localhost:8080')

        then:
        props.testRelationBase == 'http://localhost:8080'
    }
}
