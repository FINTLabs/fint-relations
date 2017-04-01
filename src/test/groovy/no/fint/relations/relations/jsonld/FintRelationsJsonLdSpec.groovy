package no.fint.relations.relations.jsonld

import no.fint.relations.AspectMetadata
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class FintRelationsJsonLdSpec extends Specification {
    private FintRelationJsonLd fintRelationJsonLd
    private AspectMetadata metadata

    void setup() {
        metadata = Mock(AspectMetadata)
        fintRelationJsonLd = new FintRelationJsonLd()
    }

    def "Return original response"() {
        given:
        def response = 'test response'

        when:
        def returnValue = fintRelationJsonLd.addRelations(metadata, ResponseEntity.ok(response))

        then:
        returnValue.body == response
    }
}
