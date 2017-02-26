package no.fint.relations.self

import no.fint.relations.integration.testutils.PersonController
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.spockframework.util.ReflectionUtil
import spock.lang.Specification

class FintSelfIdAspectSpec extends Specification {
    private FintSelfIdAspect aspect
    private PersonController testController
    private MethodSignature methodSignature
    private ProceedingJoinPoint joinPoint

    void setup() {
        testController = new PersonController()
        methodSignature = Mock(MethodSignature)
        joinPoint = Mock(ProceedingJoinPoint) {
            getSignature() >> methodSignature
            getTarget() >> testController
            getArgs() >> new Object[0]
        }
        aspect = new FintSelfIdAspect()
    }

    def "Return response directly if type is not ResponseEntity"() {
        when:
        def response = aspect.selfIdEndpoint(joinPoint)

        then:
        1 * methodSignature.getMethod() >> ReflectionUtil.getMethodByName(PersonController, 'getTestDto')
        1 * joinPoint.proceed() >> 'test-response'
        response == 'test-response'
    }
}
