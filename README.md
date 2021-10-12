<img align="left" alt="JAX-RS Tie" src="images/logo.png" width="128" height="128" />

# JAX-RS Tie

Generate a type safe link builder from your JAX-RS annotations.

## Why?

In modern rest applications it is usual to generate links between resources ([Rest Maturity Model Level 3](https://martinfowler.com/articles/richardsonMaturityModel.html#level3)).

In JAX-RS resources can be linked using a [UriBuilder](https://docs.oracle.com/javaee/7/api/javax/ws/rs/core/UriBuilder.html) for example:

```java
@GET
@Path("{planet}")
public String planetLink(@Context UriInfo uriInfo, @PathParam("planet") String planetName) {
  return uriInfo.getBaseUriBuilder()
                .path(PlanetResource.class)
                .path(PlanetResource.class, "planet")
                .build(planetName)
                .toASCIIString();
}
```

The code above generates a link which points to the `planet` method of the `PlanetResource`.

It seams easy, but there are problems with this approach:

* Methods are passed as strings, which could cause problems if a method is renamed during a refactoring
* If we are using sub resources, we have to rememeber the hirachy e.g.: 

```java
uriInfo.getBaseUriBuilder()
       .path(PersonResource.class, "person")
       .path("luke")
       .path(PlanetResource.class, "planet")
       .build("tatooine");
```

JAX-RS Tie tries to tacle both problems.
It will generate a link builder which is automatically regenerated if a resources changes 
and it map the hirachy or the resources.

With JAX-RS Tie link generation could be look as the following:

```java
@GET
@Path("{planet}")
public String planetLink(@Context UriInfo uriInfo, @PathParam("planet") String planetName) {
  return new SwLinks(uriInfo).planets()
                             .planet(planetName)
                             .asString();
}
``` 

## Usage

Only a single annotation is required to use JAX-RS Tie.
Just annotate a class with the `@GenerateLinkBuilder` annotation and JAX-RS Tie generates the link builder
with the name of the annotated class and appends "Links" to the name e.g.: 

```java
@GenerateLinkBuilder
class StarWars {}
```

The example above will create a `StarWarsLinks` link builder in the same package as the `StarWars` class.
The link builder will automatically find all JAX-RS resources which are annotated with the `@Path` annotation.

## Installation

Get the latest stable version from [![Maven Central](https://img.shields.io/maven-central/v/com.cloudogu/jaxrs-tie.svg)](https://search.maven.org/search?q=g:com.cloudogu%20a:jaxrs-tie)

### Gradle

```groovy
compileOnly 'com.cloudogu:jaxrs-tie:x.y.z'
annotationProcessor 'com.cloudogu:jaxrs-tie:x.y.z'
```

### Maven

```xml
<dependency>
  <groupId>com.cloudogu</groupId>
  <artifactId>jaxrs-tie</artifactId>
  <version>x.y.z</version>
  <optional>true</optional>
</dependency>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
