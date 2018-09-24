package no.fint.relations.integration

import no.fint.model.resource.Link
import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.PersonResource
import no.fint.relations.internal.FintLinkMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Ignore
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "fint.relations.links.person=http://my-test-monkey")
class FintLinkMapperIntegrationSpec extends Specification {

    @Autowired
    private FintLinkMapper fintLinkMapper

    @Ignore
    def "Get link value from linkMapper configuration"() {
        when:
        def link = fintLinkMapper.getLink(Link.with(PersonResource, '/test').href)

        then:
        link == 'http://my-test-monkey/test'
    }

}
