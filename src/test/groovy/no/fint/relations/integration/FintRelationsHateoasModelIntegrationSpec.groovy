package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.hateoas.dto.PersonResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "fint.relations.force-https=true")
class FintRelationsHateoasModelIntegrationSpec extends Specification {

    @Value('${local.server.port}')
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    def "JSON response contains _links"() {
        when:
        def response = restTemplate.getForEntity('/hateoas-person/resource/with-link-mapper', String)
        def json = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        json.contains('_links')
    }

    def "Add self link to person and address"() {
        when:
        def response = restTemplate.getForEntity('/hateoas-person/resource/with-link-mapper', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
    }

}
