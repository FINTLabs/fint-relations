package no.fint.relations.relations

import no.fint.relations.AspectMetadata
import no.fint.relations.annotations.FintRelation
import spock.lang.Specification

class FintRelationJsonLdSpec extends Specification {
    private FintRelationJsonLd fintRelationJsonLd
    private AspectMetadata metadata

    void setup() {
        metadata = Mock(AspectMetadata)
        fintRelationJsonLd = new FintRelationJsonLd()
    }

    def "Return original response"() {
        given:
        def response = 'test response'
        def relations = [] as FintRelation[]

        when:
        def returnValue = fintRelationJsonLd.addRelations(metadata, relations, response)

        then:
        returnValue == response
    }
}
