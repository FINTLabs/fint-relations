package no.fint.relations.integration.testutils;

import no.fint.relations.annotations.FintSelfId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelfId("id")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new TestDto("test123"));
    }

    @RequestMapping("/customObject")
    public TestDto getTestDto() {
        return new TestDto("test123");
    }

}
