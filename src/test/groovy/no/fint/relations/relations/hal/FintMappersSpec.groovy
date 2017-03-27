package no.fint.relations.relations.hal

import no.fint.model.relation.RelationType
import no.fint.relations.annotations.mapper.FintLinkMapper
import no.fint.relations.integration.testutils.dto.Address
import no.fint.relations.integration.testutils.dto.Person
import no.fint.relations.integration.testutils.dto.Telephone
import no.fint.relations.integration.testutils.mapper.AddressLinkMapper
import no.fint.relations.integration.testutils.mapper.TelephoneLinkMapper
import org.springframework.context.ApplicationContext
import spock.lang.Specification

class FintMappersSpec extends Specification {
    private FintMappers fintRelationMappers
    private ApplicationContext applicationContext

    void setup() {
        applicationContext = Mock(ApplicationContext) {
            getBeansWithAnnotation(FintLinkMapper) >> [
                    'addressLinkMapper'  : new AddressLinkMapper(),
                    'telephoneLinkMapper': new TelephoneLinkMapper()
            ]
        }
        fintRelationMappers = new FintMappers(applicationContext: applicationContext)
        fintRelationMappers.init()
    }

    def "Get all FintLinkMapper methods on startup"() {
        when:
        def mappers = fintRelationMappers.getLinkMappers()

        then:
        mappers.size() == 3
        mappers.containsKey(new RelationType.Builder().namespace('fint.no').relationName('address').main(Person, 'name').related(Address, 'street').buildTypeString())
        mappers.containsKey(new RelationType.Builder().namespace('fint.no').relationName('mainNumber').main(Person, 'name').related(Telephone, 'mainNumber').buildTypeString())
        mappers.containsKey(new RelationType.Builder().namespace('fint.no').relationName('secondaryNumber').main(Person, 'name').related(Telephone, 'secondaryNumber').buildTypeString())
    }

    def "Get method by relation id"() {
        when:
        def method = fintRelationMappers.getMethod(new RelationType.Builder().namespace('fint.no').relationName('address').main(Person, 'name').related(Address, 'street').buildTypeString())

        then:
        method.isPresent()
    }
}
