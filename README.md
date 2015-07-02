Dropwizard/Spring
===================================

Welcome to the Dropwizard/Spring project. This is a updated version of the work done by Nicolas Huray at [https://github.com/nhuray/dropwizard-spring](https://github.com/nhuray/dropwizard-spring) .


Introduction
------------

[Dropwizard](http://dropwizard.codahale.com) is a Java framework for developing ops-friendly, high-performance, RESTful web services.

[Spring](http://www.springsource.org/spring-framework) is the most popular application development framework for enterprise Java™.

This project provide a simple method for integrating Spring with Dropwizard.


Versions
------------

The latest version of the project available on Maven repository is **0.2**.

| dropwizard-spring  | Dropwizard   | Spring        |
|:------------------:|:------------:|:-------------:|
| master (0.3.4)     | 0.8.1        | 4.1.6.RELEASE |
| master (0.3.2)     | 0.7.0        | 4.0.5.RELEASE |
| master (0.3.1)     | 0.6.2        | 3.1.4.RELEASE |
| 0.2                | 0.6.0        | 3.1.3.RELEASE |
| 0.1                | 0.5.1        | 3.1.1.RELEASE |

The latest available release is **0.3.4**.


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

To install a newer release, download the jar and added it to your local maven repository. Execute this command in the same folder as the jar:

```
mvn install:install-file -Dfile=dropwizard-spring-0.3.4.jar -DgroupId=com.github.raduciumag -DartifactId=dropwizard-spring -Dversion=0.3.4-RELEASE -Dpackaging=jar
```

Usage
------------

The Dropwizard/Spring integration allow to automatically initialize Dropwizard environment through a Spring application context including health checks, resources, providers, tasks and managed.

To use Dropwizard/Spring you just have to add a ```SpringBundle``` and create your Spring application context.

For example :

```java

@Override
public void initialize(Bootstrap<HelloAppConfiguration> bootstrap) {
  // register configuration, environment and placeholder
  bootstrap.addBundle(new SpringBundle(applicationContext(), true, true, true));
}

private ConfigurableApplicationContext applicationContext() throws BeansException {
  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
  context.scan("hello");
  return context;
}
```

In this example we create a Spring application context based on annotation to resolve Spring beans.

The ```SpringBundle``` class use the application context to initialize Dropwizard environment including health checks, resources, providers, tasks and managed.

Moreover the ```SpringBundle``` class register :

 - a ```ConfigurationPlaceholderConfigurer``` to resolve Dropwizard configuration (For example : ```${http.port}```).

 - the Dropwizard configuration with the name ```dw``` to retrieve complex configuration (For example : ```#{dw.httpConfiguration}```).

 - the Dropwizard environment with the name ```dwEnv``` to retrieve complex configuration (For example : ```#{dwEnv.validator}```).

Please take a look at the hello application located in ```src/test/java/hello```.


License
------------

    The Apache Software License, Version 2.0
    http://www.apache.org/licenses/LICENSE-2.0.txt
