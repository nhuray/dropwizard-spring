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

The current version of the project is **0.2**.

| dropwizard-spring  | Dropwizard   | Spring        |
|:------------------:|:------------:|:-------------:|
| master (0.2)       | 0.6.0        | 3.1.3.RELEASE |
| 0.1                | 0.5.1        | 3.1.1.RELEASE |


Installation
------------


To install Dropwizard/Spring you just have to add this Maven dependency in your project :

```xml
<dependency>
     <groupId>com.github.nhuray</groupId>
     <artifactId>dropwizard-spring</artifactId>
     <version>0.2</version>
</dependency>
```

Usage
------------

The Dropwizard/Spring integration allow to automatically initialize Dropwizard environment through a Spring application context including health checks, resources, providers, tasks and managed.

To use Dropwizard/Spring you just have to add a ```SpringBundle``` and create your Spring application context.

For example :

```java
public class HelloApp extends Service<HelloAppConfiguration> {

    private static final String CONFIGURATION_FILE = "src/test/resources/hello/hello.yml";

    public static void main(String[] args) throws Exception {
      new HelloApp().run(new String[]{"server", CONFIGURATION_FILE});
    }

    @Override
    public void initialize(Bootstrap<HelloAppConfiguration> bootstrap) {
      // register configuration, environment and placeholder
      bootstrap.addBundle(new SpringBundle(applicationContext(), true, true, true));
    }

    @Override
    public void run(HelloAppConfiguration configuration, Environment environment) throws Exception {
      // doing nothing
    }


    private ConfigurableApplicationContext applicationContext() throws BeansException {
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
      context.scan("hello");
      return context;
    }
}
```

In this example we create a Spring application context based on annotation to resolve Spring beans.

The ```SpringBundle``` class use the application context to initialize Dropwizard environment including health checks, resources, providers, tasks and managed.

Moreover the ```SpringBundle``` class register :

 - a ```ConfigurationPlaceholderConfigurer``` to resolve Dropwizard configuration as [Spring placeholders](http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/beans.html#beans-factory-placeholderconfigurer) (For example : ```${http.port}```).

 - the Dropwizard configuration with the name ```dw``` to retrieve complex configuration with [Spring Expression Language](http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/expressions.html) (For example : ```#{dw.httpConfiguration}```).

 - the Dropwizard environment with the name ```dwEnv``` to retrieve complex configuration with [Spring Expression Language](http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/expressions.html) (For example : ```#{dwEnv.validator}```).

Please take a look at the hello application located in ```src/test/java/hello```.


License
------------

    The Apache Software License, Version 2.0
    http://www.apache.org/licenses/LICENSE-2.0.txt
