[![Chat at https://discord.gg/sgak4e5NKv](https://img.shields.io/badge/chat-on%20Discord-%234cb697)](https://discord.gg/sgak4e5NKv)
[![CI](https://github.com/javalin/javalin-rendering/workflows/Test%20all%20JDKs%20on%20all%20OSes/badge.svg)](https://github.com/javalin/javalin-rendering/actions)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven](https://img.shields.io/maven-central/v/io.javalin/javalin.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.javalin%22%20AND%20a%3A%22javalin%22)

# About Javalin

* [:heart: Sponsor Javalin](https://github.com/sponsors/tipsy)
* The main project webpage is [javalin.io](https://javalin.io)
* Chat on Discord: https://discord.gg/sgak4e5NKv
* License summary: https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)

## Javalin rendering

The `javalin-rendering` artifact is an optional module for the Javalin web framework that provides a simple way to render HTML using popular template engines. The `javalin-rendering` artifact includes default implementations for several template engines, including JTE, Mustache, Velocity, Pebble, Handlebars, and Thymeleaf.

## Add dependency

### Maven

```xml
<dependency>
    <groupId>io.javalin</groupId>
    <artifactId>javalin-rendering</artifactId>
    <version>5.4.2</version>
</dependency>
```

### Gradle

```groovy
implementation "io.javalin:javalin-rendering:5.4.2"
```

### Usage
Once you have included the `javalin-rendering` artifact and one of the supported template engine dependencies in your project, `Context#render` should automatically  render your templates. By default, the plugin will look in `src/main/resources/templates` for template files.
