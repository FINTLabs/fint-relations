package no.fint.relations

import no.fint.relations.integration.testutils.selfid.EmptySelfId
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import spock.lang.Specification

class FintSelfIdAspectSpec extends Specification {
    private FintSelfIdAspect aspect
    private EmptySelfId emptySelfId
    private ProceedingJoinPoint joinPoint

    void setup() {
        emptySelfId = new EmptySelfId()
        joinPoint = Mock(ProceedingJoinPoint) {
            getSignature() >> Mock(MethodSignature)
            getTarget() >> emptySelfId
        }
        aspect = new FintSelfIdAspect()
    }

    def "Throw exception if no selfId is provided"() {
        when:
        aspect.selfIdEndpoint(joinPoint)

        then:
        thrown(IllegalArgumentException)
    }
}
