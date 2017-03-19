package no.fint.relations

import no.fint.relations.annotations.FintRelation
import no.fint.relations.integration.testutils.controller.PersonRelationController
import no.fint.relations.relations.hal.FintRelationHal
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class FintRelationAspectSpec extends Specification {
    private FintRelationAspect aspect
    private PersonRelationController testController
    private MethodSignature methodSignature
    private ProceedingJoinPoint joinPoint
    private FintRelationHal fintRelationHal

    void setup() {
        testController = new PersonRelationController()
        methodSignature = Mock(MethodSignature)
        joinPoint = Mock(ProceedingJoinPoint) {
            getSignature() >> methodSignature
            getTarget() >> testController
            getArgs() >> new Object[0]
        }
        fintRelationHal = Mock(FintRelationHal)
        aspect = new FintRelationAspect(fintRelHal: fintRelationHal)
    }

    def "Call aspect method"() {
        when:
        def response = aspect.fintRelationEndpoint(joinPoint)

        then:
        1 * joinPoint.proceed() >> ResponseEntity.ok('test')
        1 * fintRelationHal.addRelations(_ as AspectMetadata, _ as FintRelation[], _ as ResponseEntity) >> ResponseEntity.ok('test')
        ((ResponseEntity)response).body == 'test'
    }

    def "Return original single response if return value is not ResponseEntity"() {
        when:
        def response = aspect.fintRelationEndpoint(joinPoint)

        then:
        1 * joinPoint.proceed() >> 'test'
        response == 'test'
    }

    def "Return original collection response if return value is not ResponseEntity"() {
        when:
        def response = aspect.fintRelationsEndpoint(joinPoint)
        def collectionResponse = (Collection)response

        then:
        1 * joinPoint.proceed() >> ['test1', 'test2']
        collectionResponse.size() == 2
        collectionResponse[0] == 'test1'
        collectionResponse[1] == 'test2'
    }
}
