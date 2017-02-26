package no.fint.relations.integration.testutils;

import no.fint.relations.annotations.FintSelfId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@FintSelfId
@RestController
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

    @RequestMapping("/responseEntity")
    public ResponseEntity getResponseEntityNoInput() {
        return ResponseEntity.ok(new TestDto("test1"));
    }

    @RequestMapping("/responseEntity/{name}")
    public ResponseEntity getResponseEntity(@PathVariable String name) {
        return ResponseEntity.ok(new TestDto(name));
    }

    @RequestMapping("/responseEntity/{name1}/twoPathVariables/{name2}")
    public ResponseEntity getResponseEntity(@PathVariable String name1, @PathVariable String name2) {
        return ResponseEntity.ok(new TestDto(name1 + name2));
    }

    @RequestMapping(value = "/responseHeaders", method = RequestMethod.POST)
    public ResponseEntity createResource(@RequestBody TestDto testDto) {
        return ResponseEntity.created(URI.create("/responseEntity/" + testDto.getName())).body("Created resource");
    }

    @RequestMapping("/customObject")
    public TestDto getTestDto() {
        return new TestDto("test123");
    }

}
