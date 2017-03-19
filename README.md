# fint-relations

[![Build Status](https://travis-ci.org/FINTlibs/fint-relations.svg?branch=master)](https://travis-ci.org/FINTlibs/fint-relations) 
[![Coverage Status](https://coveralls.io/repos/github/FINTlibs/fint-relations/badge.svg?branch=master)](https://coveralls.io/github/FINTlibs/fint-relations?branch=master) 
[ ![Download](https://api.bintray.com/packages/fint/maven/fint-relations/images/download.svg) ](https://bintray.com/fint/maven/fint-relations/_latestVersion)


## Installation

```
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven" 
    }
}

compile('no.fint:fint-relations:<version>')
```

## Usage

Add `@EnableFintRelations` to the main Application class

```
@EnableFintRelations
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

In the controller class add the relation mapping. Make sure the `@RequestMapping` method return `ResponseEntity`. 
The `@FintSelfId` is used to identify the main resource the controller is responsible for. For example in PersonController, this resource is Person. 
The name in `@FintSelfId` is the property that is used to identify this resource (can be a nested property), it will automatically generate the `_self` link. 

`@FintRelation` is used to connect to other resources. For example the Person resource is connected to the Address resource. 
These values are used to find the correct LinkMapper. A class can have multiple `@FintRelation` annotations.  


```
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

Create a `@Component` that has the annotation `@FintLinkMapper`. This component will be responsible to build the links that are populated in the response.
The method responsible for creating the `Link` (can be both a single link or a List of links) is annotated with `@FintLinkRelation`.

```
@FintLinkMapper
@Component
public class AddressLinkMapper {

    @FintLinkRelation(leftObject = Person.class, leftId = "name", rightObject = Address.class, rightId = "street")
    public Link createLink(Relation relation) {
        return new Link("http://localhost/address/" + relation.getLeftKey(), "address");
    }
}

```


## Configuration

| Key | Description | Default value |
|-----|-------------|---------------|
| fint.relations.force-https | Force the use of HTTPS for the generated self links for both single resource and collection resources | true |
