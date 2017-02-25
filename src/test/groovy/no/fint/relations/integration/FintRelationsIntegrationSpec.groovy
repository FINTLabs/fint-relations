package no.fint.relations.integration

import no.fint.relations.FintSelfIdAspect
import no.fint.relations.integration.testutils.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = TestApplication.class)
class FintRelationsIntegrationSpec extends Specification {

    @Autowired
    private FintSelfIdAspect fintSelfIdAspect

    def "Enable fint-relations"() {
        when:
        def aspectIsCreated = (fintSelfIdAspect != null)

        then:
        aspectIsCreated
    }

}
