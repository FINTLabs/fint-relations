package no.fint.relations.integration

import no.fint.relations.FintResource
import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.TestDto
import no.fint.relations.integration.testutils.TestResourceDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.Resource
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintRelationsIntegrationSpec extends Specification {

    @Autowired
    private TestRestTemplate restTemplate

    def "Return custom DTO content"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity', TestDto)

        then:
        response.statusCode == HttpStatus.OK
        response.getBody().name == "test123"
    }

    def "Add selfId when response type is ResponseEntity"() {
        when:
        def response = restTemplate.getForEntity    ('/responseEntity', TestResourceDto)

        then:
        response.statusCode == HttpStatus.OK
    }

    def "Do not add selfId when response type is a custom object"() {
        when:
        def response = restTemplate.getForEntity('/customObject', TestDto)

        then:
        response.statusCode == HttpStatus.OK
    }

}
