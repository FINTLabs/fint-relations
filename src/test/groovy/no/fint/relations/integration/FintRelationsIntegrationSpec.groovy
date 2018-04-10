package no.fint.relations.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.Person
import no.fint.relations.integration.testutils.dto.PersonResource
import no.fint.relations.integration.testutils.dto.PersonResources
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@ActiveProfiles('test')
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ['server.port=8989'])
class FintRelationsIntegrationSpec extends Specification {
    @LocalServerPort
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private ObjectMapper objectMapper

    def "Add link to personalressurs in person response without link mapper"() {
        when:
        def response = restTemplate.getForEntity('/person/resource/without-link-mapper', PersonResource)
        def resourceDto = response.getBody()
        println(resourceDto)

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.links['personalressurs'].href[0] == 'http://localhost/personalressurs/1'
    }

    def "Add link to personalressurs in person response with link mapper"() {
        when:
        def response = restTemplate.getForEntity('/person/resource/with-link-mapper', PersonResource)
        def resourceDto = response.getBody()
        println(resourceDto)

        then:
        response.statusCode == HttpStatus.OK
        resourceDto.getSelfLinks()[0].href == "http://my-test-url/name/test1"
        resourceDto.links['personalressurs'].href[0] == 'http://my-test-url/1'
    }

    def "Get spring hateoas resource"() {
        when:
        def singleResource = restTemplate.exchange('/person/resource/without-link-mapper', HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Person>>() {
        })
        def singleResourceBody = singleResource.getBody()
        def collectionResources = restTemplate.exchange('/person/resources/without-link-mapper', HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Person>>() {
        })
        def collectionResourcesBody = collectionResources.getBody()

        then:
        singleResourceBody.content != null
        singleResourceBody.links.size() == 2

        collectionResourcesBody.content.size() == 2
        collectionResourcesBody.links.size() == 1
    }

    def "Add relations to list content without link mapper"() {
        when:
        def response = restTemplate.exchange('/person/resources/without-link-mapper', HttpMethod.GET, null, PersonResources)
        def resources = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        //resources.getSelfLinks()[0].href == "http://localhost:${port}/person"
        resources.totalItems == 2
        resources.content.size() == 2
    }

    def "Add relations to list content with link mapper"() {
        when:
        def response = restTemplate.exchange('/person/resources/with-link-mapper', HttpMethod.GET, null, PersonResources)
        def resources = response.getBody()
        println(resources)

        then:
        response.statusCode == HttpStatus.OK
        resources.getSelfLinks()[0].href == "http://my-test-url/" as String
        resources.totalItems == 2
        resources.content.size() == 2
        resources.content[0].address.links['city'][0].href == 'http://city-test-url/testing'
    }

    def "Add array to single element link"() {
        when:
        def response = restTemplate.getForEntity('/person/resources/with-link-mapper', String)
        def body = response.getBody()

        then:
        response.statusCode == HttpStatus.OK
        JsonPath.read(body, '$._links.self[0].href') != null
    }


}
