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
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintSelfIdIntegrationSpec extends Specification {

}
