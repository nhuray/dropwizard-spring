Dropwizard-Spring
================

This project demonstrates a simple method for integrating Spring with Dropwizard.

It was inspired by [Dropwizard Guice Extension](https://raw.github.com/jaredstehler/dropwizard-guice).

### Usage

It's simple to use; simple extend from SpringService<? extends Configuration> rather than Service, and create your Spring application context.

Now you'll be able to use @Autowired (or @Inject) annotations throughout your project for dependency injection!


### Testing

See the test classes located within src/test/java/com/yammer/dropwizard/spring/SpringServiceTest.java for an example.