package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.TestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintRelationsIntegrationSpec extends Specification {

    @Autowired
    private TestRestTemplate restTemplate

    def "Enable fint-relations"() {
        when:
        def response = restTemplate.getForEntity('/test', TestDto)

        then:
        response.statusCode == HttpStatus.OK
    }

}
