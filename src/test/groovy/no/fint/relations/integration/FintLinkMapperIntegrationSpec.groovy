package no.fint.relations.integration

import no.fint.relations.internal.FintLinkMapper
import no.fint.relations.integration.testutils.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "fint.relations.links.person=http://my-test-url")
class FintLinkMapperIntegrationSpec extends Specification {

    @Autowired
    private FintLinkMapper fintLinkMapper

    def "Get link value from linkMapper configuration"() {
        when:
        def link = fintLinkMapper.getLink("\${person}/test")

        then:
        link == 'http://my-test-url/test'
    }

}
