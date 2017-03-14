package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.PersonResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resources
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintRelationsIntegrationSpec extends Specification {

    @Autowired
    private TestRestTemplate restTemplate

    def "Multiple relations"() {
        when:
        def response = restTemplate.getForEntity('/relations/responseEntity', PersonResource)
        def personResource = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        personResource.links.size() == 4
        personResource.getLink(Link.REL_SELF).href.endsWith('/relations/responseEntity')
        personResource.getLink('address').href == 'http://localhost/address/test123'
        personResource.getLink('mainTelephoneNumber').href == 'http://localhost/telephone/test123'
        personResource.getLink('secondaryTelephoneNumber').href == 'http://localhost/telephone/test123'
    }

    def "Multiple relations on list of resources"() {
        when:
        def response = restTemplate.getForEntity('/relations/responseEntity/list', Resources)
        def resources = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resources.links.size() == 1
        resources.content.size() == 2
    }
}
