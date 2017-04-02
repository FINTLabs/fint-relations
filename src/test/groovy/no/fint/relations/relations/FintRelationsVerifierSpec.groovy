package no.fint.relations.relations

import no.fint.relations.annotations.FintSelf
import no.fint.relations.integration.testutils.fintself.InvalidPropertyFintSelf
import no.fint.relations.integration.testutils.fintself.NotIdentifiableFintSelf
import no.fint.relations.integration.testutils.fintself.ValidFintSelf
import org.springframework.context.ApplicationContext
import spock.lang.Specification

class FintRelationsVerifierSpec extends Specification {
    private FintRelationsVerifier verifier
    private ApplicationContext applicationContext

    void setup() {
        applicationContext = Mock(ApplicationContext)
        verifier = new FintRelationsVerifier(applicationContext: applicationContext)
    }

    def "Valid self configuration configuration"() {
        when:
        verifier.init()

        then:
        1 * applicationContext.getBeansWithAnnotation(FintSelf) >> ['test': new ValidFintSelf()]
        noExceptionThrown()
    }

    def "Throw exception when unknown property in self configuration"() {
        when:
        verifier.init()

        then:
        1 * applicationContext.getBeansWithAnnotation(FintSelf) >> ['test': new InvalidPropertyFintSelf()]
        thrown(IllegalArgumentException)
    }

    def "Throw exception when self type is not of type Identifiable"() {
        when:
        verifier.init()

        then:
        1 * applicationContext.getBeansWithAnnotation(FintSelf) >> ['test': new NotIdentifiableFintSelf()]
        thrown(IllegalArgumentException)
    }
}
