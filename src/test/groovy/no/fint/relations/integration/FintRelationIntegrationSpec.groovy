package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.PersonResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintRelationIntegrationSpec extends Specification {

    @Autowired
    private TestRestTemplate restTemplate

    def "Add link to address in person response"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.getLink('address').href != null
    }

}
