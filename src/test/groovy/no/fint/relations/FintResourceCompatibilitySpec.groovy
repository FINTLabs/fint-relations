package no.fint.relations

import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.model.relation.FintResource
import no.fint.model.resource.Link
import no.fint.relations.integration.testutils.dto.PersonResource
import spock.lang.Specification

class FintResourceCompatibilitySpec extends Specification {
    private FintResourceCompatibility compatibility

    void setup() {
        compatibility = new FintResourceCompatibility(objectMapper: new ObjectMapper())
    }

    def "Is not FintResource given null list"() {
        when:
        def isFintResourceData = compatibility.isFintResourceData(null)

        then:
        !isFintResourceData
    }

    def "Is not FintResource given empty list"() {
        when:
        def isFintResourceData = compatibility.isFintResourceData([])

        then:
        !isFintResourceData
    }

    def "Is FintResource"() {
        given:
        def resource = new FintResource<String>(resource: 'test123')

        when:
        def isFintResourceData = compatibility.isFintResourceData([resource])

        then:
        isFintResourceData
    }

    def "Is not FintResource"() {
        given:
        PersonResource personResource = new PersonResource()
        personResource.setName('test')
        personResource.addPersonalressurs(Link.with("http://localhost/personalressurs/1"))

        when:
        def isFintResourceData = compatibility.isFintResourceData([personResource])

        then:
        !isFintResourceData
    }
}
