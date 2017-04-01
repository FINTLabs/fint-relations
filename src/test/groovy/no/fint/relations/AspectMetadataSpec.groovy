package no.fint.relations

import no.fint.relations.integration.testutils.controller.PersonRelationController
import no.fint.relations.integration.testutils.dto.Person
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.util.ReflectionUtils
import spock.lang.Specification

class AspectMetadataSpec extends Specification {

    def "Create AspectMetadata from ProceedingJoinPoint"() {
        given:
        def proceedingJoinPoint = Mock(ProceedingJoinPoint) {
            getTarget() >> new PersonRelationController()
            getSignature() >> Mock(MethodSignature) {
                getMethod() >> ReflectionUtils.findMethod(PersonRelationController, 'getResponseEntityNoInput')
            }
            getArgs() >> new Object[0]
        }

        when:
        def metadata = AspectMetadata.with(proceedingJoinPoint)

        then:
        metadata.callingClass == PersonRelationController
        metadata.arguments.length == 0
    }
}
