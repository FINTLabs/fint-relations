package no.fint.relations.integration.testutils;

import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/noLinks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class NoLinksTestController {

    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }
}
