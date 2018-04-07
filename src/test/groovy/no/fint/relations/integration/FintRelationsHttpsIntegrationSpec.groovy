package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.PersonResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resources
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "fint.relations.force-https=true")
class FintRelationsHttpsIntegrationSpec extends Specification {

    @Value('${local.server.port}')
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    def "Force https in self link when fint.relations.force-https property is set to true"() {
        when:
        def response = restTemplate.getForEntity('/person/resource/with-link-mapper', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.
        resourceDto.getLink(Link.REL_SELF).href == "https://localhost:${port}/person/name/test1" as String
    }

    def "Force https in self link for collection when fint.relations.force-https property is set to true"() {
        when:
        def response = restTemplate.getForEntity('/person/resources/with-link-mapper', Resources)
        def body = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        body.getLink(Link.REL_SELF).href == "https://localhost:${port}/person" as String
    }
}
