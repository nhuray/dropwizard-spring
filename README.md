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

The current version of the project is **0.1**.

| dropwizard-spring  | Dropwizard   | Spring        |
|:------------------:|:------------:|:-------------:|
| master (0.1)       | 0.5.1        | 3.1.1.RELEASE |


Installation
------------


To install Dropwizard/Spring you just have to add this Maven dependency in your project :

```xml
<dependency>
     <groupId>com.github.nhuray</groupId>
     <artifactId>dropwizard-spring</artifactId>
     <version>0.1</version>
</dependency>
```

Usage
------------

To use Dropwizard/Spring you just have to create a Dropwizard Service which extends ```SpringService``` rather than ```Service```, and create your Spring application context.

For example :

```java
public class HelloApp extends SpringService<HelloAppConfiguration> {

    private static final String CONFIGURATION_FILE = "src/test/resources/hello/hello.yml";

    public static void main(String[] args) throws Exception {
        new HelloApp().run(new String[]{"server", CONFIGURATION_FILE});
    }

    public HelloApp() {
        super("hello-application");
    }

    @Override
    protected ConfigurableApplicationContext initializeApplicationContext(HelloAppConfiguration configuration, Environment environment) throws BeansException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("hello");

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        // Register ConfigurationPlaceholderConfigurer
        ConfigurationPlaceholderConfigurer placeholderConfigurer = new ConfigurationPlaceholderConfigurer(configuration);
        placeholderConfigurer.setIgnoreUnresolvablePlaceholders(false); // To test all placeholders are resolved
        placeholderConfigurer.setPlaceholderPrefix("${dw.");
        placeholderConfigurer.setPlaceholderSuffix("}");
        beanFactory.registerSingleton("placeholderConfigurer", placeholderConfigurer);

        // Register Configuration
        beanFactory.registerSingleton("dw", configuration);

        // Refresh
        context.refresh();
        return context;
    }
}
```

In this example we create a Spring application context based on annotation to resolve Spring beans.

Moreover we register a ```ConfigurationPlaceholderConfigurer``` to resolve Dropwizard ```Configuration``` as [Spring placeholders](http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/beans.html#beans-factory-placeholderconfigurer) (For example : ```${http.port}```.

Finally, we register the Dropwizard ```Configuration``` itself with the name ```dw``` to retrieve complex configuration with this SPEL (For example : ```#{dw.httpConfiguration}```).

Please take a look at the hello application located in ```src/test/java/hello```.


License
------------

    The Apache Software License, Version 2.0
    http://www.apache.org/licenses/LICENSE-2.0.txt
