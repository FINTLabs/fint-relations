package no.fint.relations.relations

import spock.lang.Specification

class FintRelationSpec extends Specification {

    def "Create fint relation from relation string"() {
        given:
        def relation = 'urn:fint.no:arbeidsforhold:personalressurs:arbeidsforhold.systemid:personalressurs.ansattnummer'

        when:
        def fintRelation = new FintRelation(relation)

        then:
        fintRelation.namespace == 'fint.no'
        fintRelation.mainClass == 'arbeidsforhold'
        fintRelation.relationName == 'personalressurs'
        fintRelation.mainClassId == 'arbeidsforhold.systemid'
        fintRelation.relatedClassId == 'personalressurs.ansattnummer'
    }

    def "Throw IllegalArgumentException when input relation is not a valid format"() {
        given:
        def relation = 'test'

        when:
        new FintRelation(relation)

        then:
        thrown(IllegalArgumentException)
    }
}
