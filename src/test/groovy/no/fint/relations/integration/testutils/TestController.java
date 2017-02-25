package no.fint.relations.integration.testutils;

import no.fint.relations.annotations.FintSelfId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelfId("id")
@RestController
@RequestMapping(value = "/test", method = RequestMethod.GET)
public class TestController {

    @RequestMapping
    public TestDto getText() {
        return new TestDto("123");
    }

}
