package no.fint.relations.integration.testutils;

import no.fint.relations.annotations.EnableFintRelations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableFintRelations
@SpringBootApplication
public class TestApplication {
}
