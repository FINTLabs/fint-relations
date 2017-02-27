package no.fint.relations.rel

import no.fint.relations.integration.testutils.PersonRelationController
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import spock.lang.Specification

class FintRelationAspectSpec extends Specification {
    private FintRelationAspect aspect
    private PersonRelationController testController
    private MethodSignature methodSignature
    private ProceedingJoinPoint joinPoint

    void setup() {
        testController = new PersonRelationController()
        methodSignature = Mock(MethodSignature)
        joinPoint = Mock(ProceedingJoinPoint) {
            getSignature() >> methodSignature
            getTarget() >> testController
            getArgs() >> new Object[0]
        }
        aspect = new FintRelationAspect()
    }

    def "Call aspect method"() {
        when:
        def response = aspect.fintRelationEndpoint(joinPoint)

        then:
        1 * joinPoint.proceed() >> 'test'
        response == 'test'
    }

}
