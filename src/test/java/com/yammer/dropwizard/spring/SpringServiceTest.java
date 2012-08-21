package com.yammer.dropwizard.spring;

import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import hello.HelloConfiguration;
import hello.HelloApplication;
import hello.HelloApplicationConfiguration;
import hello.health.HelloHealthCheck;
import hello.resources.HelloResource;
import hello.tasks.HelloTask;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class SpringServiceTest {

    @Mock
    private HelloApplicationConfiguration configuration;

    @Mock
    private Environment environment;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        final HelloConfiguration sampleConfiguration = mock(HelloConfiguration.class);
//        when(configuration.getHello()).thenReturn(sampleConfiguration);
        when(sampleConfiguration.getMessages()).thenReturn(asList("Hello", "there"));
        final HttpConfiguration httpConfiguration = mock(HttpConfiguration.class);
        when(configuration.getHttp()).thenReturn(httpConfiguration);
        when(httpConfiguration.getPort()).thenReturn(1111);
    }

    @Test
    public void itInstallsResources() throws Exception {
        // When
        HelloApplication s = new HelloApplication();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<HelloResource> resource = ArgumentCaptor.forClass(HelloResource.class);
        verify(environment).addResource(resource.capture());
        assertThat(resource.getValue(), is(HelloResource.class));
    }

    @Test
    public void itWiresUpDependencies() throws Exception {
        // When
        HelloApplication s = new HelloApplication();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<HelloResource> resource = ArgumentCaptor.forClass(HelloResource.class);
        verify(environment).addResource(resource.capture());

        HelloResource r = resource.getValue();
        assertThat(r.getMyService(), not(nullValue()));
    }

    @Test
    public void itResolveDropwizardConfiguration() throws Exception {
        // When
        HelloApplication s = new HelloApplication();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<HelloResource> resource = ArgumentCaptor.forClass(HelloResource.class);
        verify(environment).addResource(resource.capture());

        HelloResource r = resource.getValue();
        assertThat(r.getPort(), is(1111));
    }

    @Test
    public void itInstallsHealthChecks() throws Exception {
        // When
        HelloApplication s = new HelloApplication();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<? extends HealthCheck> healthCheck = ArgumentCaptor.forClass(HealthCheck.class);
        verify(environment).addHealthCheck(healthCheck.capture());
        assertThat(healthCheck.getValue(), is(HelloHealthCheck.class));
    }

    @Test
    public void itInstallsTasks() throws Exception {
        // When
        HelloApplication s = new HelloApplication();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<? extends Task> task = ArgumentCaptor.forClass(Task.class);
        verify(environment).addTask(task.capture());
        assertThat(task.getValue(), is(HelloTask.class));
    }
}
