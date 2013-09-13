package com.github.nhuray.dropwizard.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import hello.config.HelloAppConfiguration;
import hello.config.HelloConfiguration;
import hello.health.HelloHealthCheck;
import hello.resources.HelloResource;
import hello.service.HelloService;
import hello.tasks.HelloTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpringBundleTest {

    @Mock
    private Environment environment;

    @Mock
    private ObjectMapperFactory objectMapperFactory;

    private HelloAppConfiguration configuration;

    private SpringBundle bundle;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext();
        context.scan("hello");
        bundle = new SpringBundle(context, true, true, true);

        MockitoAnnotations.initMocks(this);

        HelloConfiguration hello = new HelloConfiguration();
        hello.setMessage("Hello");

        configuration = new HelloAppConfiguration();
        configuration.setHello(hello);

        when(environment.getObjectMapperFactory()).thenReturn(objectMapperFactory);
        when(objectMapperFactory.build()).thenReturn(new ObjectMapper());
    }

    @Test
    public void registerResources() throws Exception {
        // When
        bundle.run(configuration, environment);

        // Then
        ArgumentCaptor<HelloResource> resource = ArgumentCaptor.forClass(HelloResource.class);
        verify(environment).addResource(resource.capture());
        assertThat(resource.getValue(), is(HelloResource.class));
    }

    @Test
    public void registerHealthChecks() throws Exception {
        // When
        bundle.run(configuration, environment);

        // Then
        ArgumentCaptor<? extends HealthCheck> healthCheck = ArgumentCaptor.forClass(HealthCheck.class);
        verify(environment).addHealthCheck(healthCheck.capture());
        assertThat(healthCheck.getValue(), is(HelloHealthCheck.class));
    }

    @Test
    public void registerTasks() throws Exception {
        // When
        bundle.run(configuration, environment);

        // Then
        ArgumentCaptor<? extends Task> task = ArgumentCaptor.forClass(Task.class);
        verify(environment).addTask(task.capture());
        assertThat(task.getValue(), is(HelloTask.class));
    }

    @Test
    public void registerConfiguration() throws Exception {
        // When
        bundle.run(configuration, environment);

        // Then
        ArgumentCaptor<HelloResource> resource = ArgumentCaptor.forClass(HelloResource.class);
        verify(environment).addResource(resource.capture());

        HelloResource r = resource.getValue();
        assertThat(r.getPort(), is(8080)); // Defaut port
    }

    @Test
    public void registerEnvironment() throws Exception {
        // When
        bundle.run(configuration, environment);

        // Then
        assertThat(context.getBean("dwEnv"), instanceOf(Environment.class));
    }


    @Test
    public void wiresUpDependencies() throws Exception {
        // When
        bundle.run(configuration, environment);

        // Then
        ArgumentCaptor<HelloResource> resource = ArgumentCaptor.forClass(HelloResource.class);
        verify(environment).addResource(resource.capture());

        HelloResource r = resource.getValue();
        final HelloService helloService = r.getHelloService();
        assertNotNull(helloService);
        assertThat(helloService.getMessage(), is("Hello"));

        assertThat(r.getConfiguration(), instanceOf(Configuration.class));
        assertThat(r.getEnvironment(), instanceOf(Environment.class));
    }



    @Test(expected = IllegalArgumentException.class)
    public void unableToRegisterConfigurationIfSpringContextIsActive() throws Exception {
        // When
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("test"); // active context
        bundle = new SpringBundle(context, true, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unableToRegisterPlaceholderIfSpringContextIsActive() throws Exception {
        // When
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("test"); // active context
        bundle = new SpringBundle(context, false, false, true);
    }


    @Test(expected = IllegalArgumentException.class)
    public void unableToRegisterEnvironmentIfSpringContextIsActive() throws Exception {
        // When
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("test"); // active context
        bundle = new SpringBundle(context, false, true, false);
    }


}
