package no.fint.relations.relations.hal

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import no.fint.model.relation.Relation
import no.fint.relations.AspectMetadata
import no.fint.relations.integration.testutils.controller.PersonRelationController
import no.fint.relations.integration.testutils.dto.Person
import no.fint.relations.relations.FintLinkMapper
import org.slf4j.LoggerFactory
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ReflectionUtils
import spock.lang.Specification

class FintRelationsHalSpec extends Specification {
    private FintRelationHal fintRelationHal
    private AspectMetadata metadata
    private SpringHateoasIntegration springHateoasIntegration
    private FintLinkMapper fintLinkMapper

    void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
        logger.setLevel(Level.INFO)

        metadata = Mock(AspectMetadata) {
            getCallingClass() >> PersonRelationController
            getMethod() >> ReflectionUtils.findMethod(PersonRelationController, 'getResponseEntityNoInput')
            getArguments() >> new Object[0]
        }
        springHateoasIntegration = Mock(SpringHateoasIntegration) {
            getSelfLink(metadata) >> new Link('http://localhost', Link.REL_SELF)
            getSelfLinkCollection(metadata, _ as String) >> new Link('http://localhost', Link.REL_SELF)
        }
        fintLinkMapper = Mock(FintLinkMapper) {
            getLink(_ as String) >> 'http://localhost'
        }

        fintRelationHal = new FintRelationHal(springHateoasIntegration: springHateoasIntegration, fintLinkMapper: fintLinkMapper)
    }

    def "Add self relation to single resource"() {
        given:
        def response = ResponseEntity.ok(new Person(name: 'test'))

        when:
        def returnValue = fintRelationHal.addRelations(metadata, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resource) responseEntity.getBody()

        then:
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 1
        body.links[0].rel == Link.REL_SELF
    }


    def "Add self relation to collection resource"() {
        given:
        def response = ResponseEntity.ok([new Person(name: 'test')])

        when:
        def returnValue = fintRelationHal.addRelations(metadata, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resources) responseEntity.getBody()

        then:
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 1
        body.links[0].rel == Link.REL_SELF
    }

    def "Add self and relation to other resource for single resource response"() {
        given:
        def relation = new Relation.Builder().with(Person.Relasjonsnavn.ADDRESS).link('http://localhost/pereson').build()
        def person = new Person(name: 'test')
        person.addRelasjon(relation)
        def response = ResponseEntity.ok(person)

        when:
        def returnValue = fintRelationHal.addRelations(metadata, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resource) responseEntity.getBody()

        then:
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 2
    }

    def "Add self and relation to other resource for collection resource response"() {
        given:
        def relation = new Relation.Builder().with(Person.Relasjonsnavn.ADDRESS).link('http://localhost/pereson').build()
        def person = new Person(name: 'test')
        person.addRelasjon(relation)
        def response = ResponseEntity.ok([person])

        when:
        def returnValue = fintRelationHal.addRelations(metadata, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resources) responseEntity.getBody()

        then:
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 1
        body.links[0].rel == Link.REL_SELF
        ((Resource) body[0]).links.size() == 2
    }

}
