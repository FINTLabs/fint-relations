package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.Person
import no.fint.relations.integration.testutils.dto.PersonResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.Link
import org.springframework.hateoas.LinkDiscoverer
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintRelationIntegrationSpec extends Specification {

    @Value('${local.server.port}')
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private LinkDiscoverer linkDiscoverer

    def "Add link to address in person response"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.getLink('address').href == 'http://localhost/address/test1'
    }

    def "Return custom DTO content"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity/test123', Person)

        then:
        response.statusCode == HttpStatus.OK
        response.getBody().name == "test123"
    }

    def "Add selfId when response type is ResponseEntity, no PathVariables"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.name == 'test1'
        resourceDto.getLink(Link.REL_SELF).href == "http://localhost:${port}/responseEntity" as String
    }

    def "Add selfId when response type is ResponseEntity, one PathVariable"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity/test123', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.name == 'test123'
        resourceDto.getLink(Link.REL_SELF).href == "http://localhost:${port}/responseEntity/test123" as String
    }

    def "Add selfId when response type is ResponseEntity, two PathVariables"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity/test123/twoPathVariables/test234', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.name == 'test123test234'
        resourceDto.getLink(Link.REL_SELF).href == "http://localhost:${port}/responseEntity/test123/twoPathVariables/test234" as String
    }

    def "Add selfId with string response"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity/test234', String)
        def selfLink = linkDiscoverer.findLinkWithRel(Link.REL_SELF, response.getBody())

        then:
        response.statusCode == HttpStatus.OK
        selfLink.getRel() == Link.REL_SELF
        selfLink.getHref() == "http://localhost:${port}/responseEntity/test234" as String
    }

    def "Do not add selfId when response type is a custom object"() {
        when:
        def response = restTemplate.getForEntity('/customObject', Person)

        then:
        response.statusCode == HttpStatus.OK
    }

    def "Keep response headers from original responseEntity"() {
        when:
        def response = restTemplate.postForEntity('/responseHeaders', new Person(name: 'test123'), String)

        then:
        response.statusCode == HttpStatus.CREATED
        response.headers.get(HttpHeaders.LOCATION)[0] == '/responseEntity/test123'
    }

    def "Do not add links when @FintSelfId is not present"() {
        when:
        def response = restTemplate.getForEntity('/noLinks/responseEntity', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.name == 'test123'
        resourceDto.links.size() == 0
    }

    def "Add relations to list content"() {
        when:
        def response = restTemplate.getForEntity('/responseEntity/list', String)
        def body = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        body.contains('_embedded')
        body.contains('_links')
        body.contains('_entries')
        body.contains('total_items')
    }

    def "No links added when no matching link mapper is found"() {
        when:
        def response = restTemplate.getForEntity('/noMatchingLinkMappers', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.links.size() == 1
        resourceDto.getLink(Link.REL_SELF).href == "http://localhost:${port}/noMatchingLinkMappers" as String
    }

    def "Return original response when invalid method on link mapper"() {
        when:
        def response = restTemplate.getForEntity('/invalidLinkMapper', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.links.size() == 0
    }

    def "Return original response when using the application/ld+json accept header"() {
        given:
        def headers = new HttpHeaders()
        headers.add(HttpHeaders.ACCEPT, 'application/ld+json')

        when:
        def response = restTemplate.exchange('/responseEntity', HttpMethod.GET, new HttpEntity<>(headers), Person)

        then:
        response.statusCode == HttpStatus.OK
        response.getBody() != null
    }

    def "Skip null values in serialization"() {
        when:
        def response = restTemplate.getForEntity('/nullValue', String)

        then:
        !response.body.contains('name')
    }

    def "Add relations with different property name"() {
        when:
        def response = restTemplate.getForEntity('/differentProperty', PersonResource)
        def resourceDto = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.links.size() == 2
        resourceDto.hasLink(Link.REL_SELF)
        resourceDto.hasLink('differentproperty')
    }

}
