package no.fint.relations.relations.hal

import no.fint.relations.annotations.mapper.FintLinkMapper
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
        mappers.containsKey('person.name:address.street')
        mappers.containsKey('person.name:telephone.mainNumber')
        mappers.containsKey('person.name:telephone.secondaryNumber')
    }

    def "Get method by relation id"() {
        when:
        def method = fintRelationMappers.getMethod('person.name:address.street')

        then:
        method.isPresent()
    }
}
