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

compile('no.fint:fint-relations:1.1.5')
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


### 2. Create a resource assembler

Extend the `FintResourceAssembler` class with the type of model, in the example below this is the `Person` model.
In the constructor add the the controller class responsible for the endpoints of this model. Finally, override the `assemble` method.
This is where the logic to build the id and optionally the path is.

```java
@Component
public class PersonAssembler extends FintResourceAssembler<Person> {
    public PersonAssembler() {
        super(PersonController.class);
    }

    @Override
    public FintResourceSupport assemble(Person person, FintResource<Person> resource) {
        return createResourceWithId(person.getName(), resource);
    }
}
```

If the id of the resource has additional parts in the, this can be added to the `createResourceWithId`:
```java
@Override
public FintResourceSupport assemble(Person person, FintResource<Person> fintResource) {
    return createResourceWithId(person.getFodselsnummer().getIdentifikatorverdi(), fintResource, "fodselsnummer");
}
```


### 3. Add custom link mapper configuration

This will replace the `${}` values with configured values from the map.  
Expose a `Map<String, String>` as a bean with the `@Qualifier` "linkMapper". The value of the map is the class name and parts of the package structure of the resource.
For example, for the class `no.fint.model.administrasjon.personal.Arbeidsforhold` the value will be `administrasjon.personal.arbeidsforhold`.

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
| fint.relations.test-base-url | The base URL used if the 'test' profile is enabled. This link can contain the port 'http://localhost:8080' or not 'http://localhost'. If no port is specified the 'local.server.port' (or 'server.port') is used. | http://localhost |


## References

- [HAL - Hypertext Application Language](http://stateless.co/hal_specification.html)
- [Spring HATEOAS](http://docs.spring.io/spring-hateoas/docs/0.23.0.RELEASE/reference/html/)
- [Implementing HAL hypermedia REST API using Spring HATEOAS](https://opencredo.com/hal-hypermedia-api-spring-hateoas/)
- [The REST APIs and HATEOAS](https://developer.paypal.com/docs/api/hateoas-links/)
