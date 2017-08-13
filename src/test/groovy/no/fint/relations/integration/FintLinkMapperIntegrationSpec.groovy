package no.fint.relations.integration

import no.fint.relations.integration.testutils.TestApplication
import no.fint.relations.integration.testutils.dto.Person
import no.fint.relations.FintLinkMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FintLinkMapperIntegrationSpec extends Specification {

    @Autowired
    private FintLinkMapper fintLinkMapper

    def "Get link value from linkMapper configuration"() {
        when:
        def link = fintLinkMapper.getLink("\${${Person.class.getName()}}/test")

        then:
        link == 'http://my-test-url/test'
    }

}
