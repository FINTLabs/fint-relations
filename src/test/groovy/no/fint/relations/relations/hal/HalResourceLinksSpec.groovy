package no.fint.relations.relations.hal

import no.fint.model.relation.Relation
import no.fint.relations.AspectMetadata
import no.fint.relations.annotations.FintRelation
import no.fint.relations.annotations.FintSelfId
import no.fint.relations.config.FintRelationsProps
import no.fint.relations.integration.testutils.controller.PersonRelationController
import no.fint.relations.integration.testutils.dto.Address
import no.fint.relations.integration.testutils.dto.Person
import no.fint.relations.integration.testutils.mapper.AddressLinkMapper
import org.springframework.util.ReflectionUtils
import spock.lang.Specification

class HalResourceLinksSpec extends Specification {
    private HalResourceLinks halResourceLinks
    private AspectMetadata metadata
    private FintRelation fintRelation
    private FintMappers fintMappers
    private FintRelationsProps fintRelationsProps

    void setup() {
        metadata = Mock(AspectMetadata) {
            getCallingClass() >> PersonRelationController
            getMethod() >> ReflectionUtils.findMethod(PersonRelationController, 'getResponseEntityNoInput')
            getArguments() >> new Object[0]
            getSelfId() >> Mock(FintSelfId) {
                self() >> Person
                id() >> 'name'
            }
        }
        fintRelation = Mock(FintRelation) {
            objectLink() >> Address
        }
        fintMappers = Mock(FintMappers)
        fintRelationsProps = Mock(FintRelationsProps) {
            getRelationBase() >> 'http://localhost:8080/'
        }
        halResourceLinks = new HalResourceLinks(fintMappers: fintMappers, props: fintRelationsProps)
    }

    def "Get links from LinkMapper"() {
        given:
        def person = new Person(name: 'test')
        def objectMethod = new FintRelationObjectMethod(ReflectionUtils.findMethod(AddressLinkMapper, 'createLink', Relation), new AddressLinkMapper())

        when:
        def links = halResourceLinks.getLinks(person, metadata, fintRelation)

        then:
        1 * fintMappers.getMethod(_ as String) >> Optional.of(objectMethod)
        links.size() == 1
        links[0].rel == 'address'
    }
}
