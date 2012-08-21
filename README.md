Dropwizard/Spring
===================================

Welcome to the Dropwizard/Spring project


Introduction
------------

[Dropwizard](http://dropwizard.codahale.com) is a Java framework for developing ops-friendly, high-performance, RESTful web services.

[Spring](http://www.springsource.org/spring-framework) is the most popular application development framework for enterprise Java™.

This project provide a simple method for integrating Spring with Dropwizard.


Versions
------------

The current version of the project is **1.0**.

| dropwizard-spring  | Dropwizard   | Spring        |
|:------------------:|:------------:|:-------------:|
| master (1.0)       | 0.5.1        | 3.1.1.RELEASE |

Installation
------------


To install Dropwizard/Spring you just have to add this Maven dependency in your project :

```xml
<dependency>
     <groupId>com.yammer.dropwizard.contrib</groupId>
     <artifactId>dropwizard-spring</artifactId>
     <version>1.0</version>
</dependency>
```

Usage
------------

To use DropWizard/Spring you just have to create a Dropwizard Service which extends ```SpringService``` rather than ```Service```, and create your Spring application context.

For example :

```java
public class SampleService extends SpringService<SampleServiceConfiguration> {

private static final String CONFIGURATION_FILE = "src/test/resources/hello/hello.yml";

public static void main(String[] args) throws Exception {
    new SampleService().run(new String[]{"server", CONFIGURATION_FILE});
}

public SampleService() {
    super("hello-application");
}

@Override
protected ConfigurableApplicationContext initializeSpring(SampleServiceConfiguration configuration, DropwizardContext parent) throws BeansException {
    // Configuration based on annotation
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.setParent(parent);
    context.scan("hello");
    context.refresh();
    return context;
}
```

In this example the ```DropwizardContext``` is a Spring application context exposing the Dropwizard ```Configuration``` as a Spring Bean in order to use it in another Spring beans :

```java
@Path("my-resource")
@Component
public class MyResource {

    @Value("#{dw.http}")
    private HttpConfiguration http;

...
```



Testing
------------

To test Dropwizard/Spring you can checkout the project and run the hello project located in ```src/test/java/hello``.


License
------------

    The Apache Software License, Version 2.0
    http://www.apache.org/licenses/LICENSE-2.0.txt