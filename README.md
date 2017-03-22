# fint-relations

[![Build Status](https://travis-ci.org/FINTlibs/fint-relations.svg?branch=master)](https://travis-ci.org/FINTlibs/fint-relations) 
[![Coverage Status](https://coveralls.io/repos/github/FINTlibs/fint-relations/badge.svg?branch=master)](https://coveralls.io/github/FINTlibs/fint-relations?branch=master) 
[ ![Download](https://api.bintray.com/packages/fint/maven/fint-relations/images/download.svg) ](https://bintray.com/fint/maven/fint-relations/_latestVersion)


## Installation

```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven" 
    }
}

compile('no.fint:fint-relations:<version>')
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
@FintSelfId(self = Person.class, id = "name")
@FintRelation(objectLink = Address.class, id = "street")
@RestController
@RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
public class PersonController {

    @RequestMapping("/person")
    public ResponseEntity getPerson() {
        return ResponseEntity.ok(new Person("test1"));
    }
}
```

Make sure the `@RequestMapping` method return `ResponseEntity`. 
The `@FintSelfId` is used to identify the main resource the controller is responsible for and it will automatically generate the `_self` link. 
For example in PersonController this resource is Person. 
The name in `@FintSelfId` is the property that is used to identify this resource (can be a nested property). 

`@FintRelation` is used to connect to other resources. In the example above the Person is connected to the Address resource. 
These values are used to find the correct LinkMapper. A class can have multiple `@FintRelation` annotations.  

### 3. Create a `@Component` with the `@FintLinkMapper` annotation.
This component will be responsible to build the links that are populated in the response.
The method responsible for creating the `Link` (can be both a single link or a List of links) is annotated with `@FintLinkRelation`.

```java
@FintLinkMapper(leftObject = Person.class, leftId = "name")
@Component
public class AddressLinkMapper {

    @Autowired
    private MyService myService;

    @FintLinkRelation(rightObject = Address.class, rightId = "street")
    public Link createLink(Relation relation) {
        String href = myService.getHref(relation);
        return new Link(href, "address");
    }
}

```


## Configuration

| Key | Description | Default value |
|-----|-------------|---------------|
| fint.relations.force-https | Force the use of HTTPS for the generated self links for both single resource and collection resources | true |
| fint.relations.relation-id-base | The base of the relation id, use to build the type value in the Relation object | https://dokumentasjon.felleskomponent.no/relasjoner/ |


## References

- [Spring HATEOAS](http://docs.spring.io/spring-hateoas/docs/0.23.0.RELEASE/reference/html/)
- [Implementing HAL hypermedia REST API using Spring HATEOAS](https://opencredo.com/hal-hypermedia-api-spring-hateoas/)
- [The REST APIs and HATEOAS](https://developer.paypal.com/docs/api/hateoas-links/)