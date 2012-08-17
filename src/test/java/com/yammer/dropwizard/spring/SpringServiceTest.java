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
import sample.SampleConfiguration;
import sample.SampleService;
import sample.SampleServiceConfiguration;
import sample.health.MyHealthCheck;
import sample.resources.MyResource;
import sample.tasks.MyTask;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class SpringServiceTest {

    @Mock
    private SampleServiceConfiguration configuration;

    @Mock
    private Environment environment;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        final SampleConfiguration sampleConfiguration = mock(SampleConfiguration.class);
        when(configuration.getSample()).thenReturn(sampleConfiguration);
        when(sampleConfiguration.getMessages()).thenReturn(asList("Hello", "there"));
        final HttpConfiguration httpConfiguration = mock(HttpConfiguration.class);
        when(configuration.getHttp()).thenReturn(httpConfiguration);
        when(httpConfiguration.getPort()).thenReturn(1111);
    }

    @Test
    public void itInstallsResources() throws Exception {
        // When
        SampleService s = new SampleService();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
        verify(environment).addResource(resource.capture());
        assertThat(resource.getValue(), is(MyResource.class));
    }

    @Test
    public void itWiresUpDependencies() throws Exception {
        // When
        SampleService s = new SampleService();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
        verify(environment).addResource(resource.capture());

        MyResource r = resource.getValue();
        assertThat(r.getMyService(), not(nullValue()));
        assertThat(r.getMyService().getMyOtherService(), not(nullValue()));
    }

    @Test
    public void itResolveDropwizardConfiguration() throws Exception {
        // When
        SampleService s = new SampleService();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
        verify(environment).addResource(resource.capture());

        MyResource r = resource.getValue();
        assertThat(r.getPort(), is(1111));
        assertThat(r.getTitle(), is("Hello"));
        assertThat(r.getDescription(), is("there"));
    }

    @Test
    public void itInstallsHealthChecks() throws Exception {
        // When
        SampleService s = new SampleService();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<? extends HealthCheck> healthCheck = ArgumentCaptor.forClass(HealthCheck.class);
        verify(environment).addHealthCheck(healthCheck.capture());
        assertThat(healthCheck.getValue(), is(MyHealthCheck.class));
    }

    @Test
    public void itInstallsTasks() throws Exception {
        // When
        SampleService s = new SampleService();
        s.initializeWithBundles(configuration, environment);

        // Then
        ArgumentCaptor<? extends Task> task = ArgumentCaptor.forClass(Task.class);
        verify(environment).addTask(task.capture());
        assertThat(task.getValue(), is(MyTask.class));
    }
}
