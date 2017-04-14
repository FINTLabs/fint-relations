# fint-relations

[![Build Status](https://travis-ci.org/FINTlibs/fint-relations.svg?branch=master)](https://travis-ci.org/FINTlibs/fint-relations) 
[![Coverage Status](https://coveralls.io/repos/github/FINTlibs/fint-relations/badge.svg?branch=master)](https://coveralls.io/github/FINTlibs/fint-relations?branch=master) 


## Installation

```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven" 
    }
}

compile('no.fint:fint-relations:0.0.18')
```

## Usage

### 1. Add `@EnableFintRelations` to the main Application class

```java
@EnableFintRelations
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 2. In the controller class add the relation mapping.

```java
@FintSelf(type = Person.class, property = "name")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
public class PersonController {

    @FintRelations
    @RequestMapping("/person")
    public ResponseEntity getPerson() {
        return ResponseEntity.ok(new Person("test1"));
    }
}
```

Make sure the `@RequestMapping` method return `ResponseEntity`. 
The `@FintSelf` is used to identify the main resource the controller is responsible for and it will automatically generate the `_self` link. 
For example in PersonController this resource is Person. 
The id in `@FintSelf` is the property that is used to identify this resource (can be a nested property). 

`@FintRelations` is used to generate the HATEOAS resources. It takes the relations added to the `FintResource`and appends them to a Spring HATEOAS resource object.

### 3. Add custom link mapper configuration

This will replace the `${}` values with configured values from the map.  
Expose a `Map<String, String>` as a bean with the `@Qualifier` "linkMapper". The value of the map is the fully qualified class name of the resource.  
In the example below the value `${no.fint.relations.integration.testutils.dto.Person}/test` is replaced with `http://my-test-url/test`.

```java
@Qualifier("linkMapper")
@Bean
public Map<String, String> linkMapper() {
    Map<String, String> links = new HashMap<>();
    links.put(Person.class.getName(), "http://my-test-url");
    links.put(Address.class.getName(), "/address");
    return links;
}
```

The value provided can either be a full url or a path. If a path is provided, the base url is appended from the configuration values.  
For example, the Address url will be `https://api.felleskomponent.no/address`.


## Configuration

| Key | Description | Default value |
|-----|-------------|---------------|
| fint.relations.force-https | Force the use of HTTPS for the generated self links for both single resource and collection resources | true |
| fint.relations.default-base-url | The base URL used if no other value is found | https://api.felleskomponent.no |
| fint.relations.test-base-url | The base URL used if the 'test' profile is enabled. This link can contain the port 'http://localhost:8080' or not 'http://localhost'. If no port is specified the 'local.server.port' is used. | http://localhost |


## References

- [HAL - Hypertext Application Language](http://stateless.co/hal_specification.html)
- [Spring HATEOAS](http://docs.spring.io/spring-hateoas/docs/0.23.0.RELEASE/reference/html/)
- [Implementing HAL hypermedia REST API using Spring HATEOAS](https://opencredo.com/hal-hypermedia-api-spring-hateoas/)
- [The REST APIs and HATEOAS](https://developer.paypal.com/docs/api/hateoas-links/)