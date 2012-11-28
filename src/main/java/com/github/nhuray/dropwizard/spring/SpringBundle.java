package com.github.nhuray.dropwizard.spring;

import com.github.nhuray.dropwizard.spring.config.ConfigurationPlaceholderConfigurer;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.Bundle;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * A bundle which load Spring Application context to automatically initialize Dropwizard {@link Environment}
 * including health checks, resources, providers, tasks and managed.
 */
public class SpringBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private static final Logger LOG = LoggerFactory.getLogger(SpringBundle.class);

    private ConfigurableApplicationContext context;
    private boolean registerConfiguration;

    /**
     * Creates a new SpringBundle to automatically initialize Dropwizard {@link Environment}
     * <p/>
     * This constructor doesn't register Dropwizard Configuration as a Spring Bean.
     *
     * @param context the application context to load
     */
    public SpringBundle(ConfigurableApplicationContext context) {
        this(context, false);
    }

    /**
     * Creates a new SpringBundle to automatically initialize Dropwizard {@link Environment}
     * <p/>
     * @param context the application context to load
     * @param registerConfiguration register dropwizard configuration as a Spring Bean.
     */
    public SpringBundle(ConfigurableApplicationContext context, boolean registerConfiguration) {
        if (registerConfiguration) {
            Assert.isTrue(!context.isActive(), "Context must be not active in order to register configuration");
        }
        this.context = context;
        this.registerConfiguration = registerConfiguration;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        // Register Dropwizard configuration as a Spring Bean
        if (registerConfiguration) registerConfiguration(configuration, context);

        // Initialize Dropwizard environment
        addHealthChecks(environment, context);
        addProviders(environment, context);
        addInjectableProviders(environment, context);
        addResources(environment, context);
        addTasks(environment, context);
        addManaged(environment, context);
        addLifecycle(environment, context);
    }


    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // nothing doing
    }

    // ~ Dropwizard Environment initialization methods

    private void addManaged(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Managed> beansOfType = context.getBeansOfType(Managed.class);
        for (String beanName : beansOfType.keySet()) {
            // Add managed to Dropwizard environment
            Managed managed = beansOfType.get(beanName);
            environment.manage(managed);
            LOG.info("Added managed: " + managed.getClass().getName());
        }
    }

    private void addLifecycle(Environment environment, ConfigurableApplicationContext context) {
        Map<String, LifeCycle> beansOfType = context.getBeansOfType(LifeCycle.class);
        for (String beanName : beansOfType.keySet()) {
            // Add lifeCycle to Dropwizard environment
            LifeCycle lifeCycle = beansOfType.get(beanName);
            environment.manage(lifeCycle);
            LOG.info("Added lifeCycle: " + lifeCycle.getClass().getName());
        }
    }

    private void addTasks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Task> beansOfType = context.getBeansOfType(Task.class);
        for (String beanName : beansOfType.keySet()) {
            // Add task to Dropwizard environment
            Task task = beansOfType.get(beanName);
            environment.addTask(task);
            LOG.info("Added task: " + task.getClass().getName());
        }
    }

    private void addHealthChecks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, HealthCheck> beansOfType = context.getBeansOfType(HealthCheck.class);
        for (String beanName : beansOfType.keySet()) {
            // Add healthCheck to Dropwizard environment
            HealthCheck healthCheck = beansOfType.get(beanName);
            environment.addHealthCheck(healthCheck);
            LOG.info("Added healthCheck: " + healthCheck.getClass().getName());
        }
    }

    private void addInjectableProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, InjectableProvider> beansOfType = context.getBeansOfType(InjectableProvider.class);
        for (String beanName : beansOfType.keySet()) {
            // Add injectableProvider to Dropwizard environment
            InjectableProvider injectableProvider = beansOfType.get(beanName);
            environment.addProvider(injectableProvider);
            LOG.info("Added injectable provider: " + injectableProvider.getClass().getName());
        }
    }

    private void addProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Provider.class);
        for (String beanName : beansWithAnnotation.keySet()) {
            // Add injectableProvider to Dropwizard environment
            Object provider = beansWithAnnotation.get(beanName);
            environment.addProvider(provider);
            LOG.info("Added provider : " + provider.getClass().getName());
        }
    }

    private void addResources(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Path.class);
        for (String beanName : beansWithAnnotation.keySet()) {
            // Add injectableProvider to Dropwizard environment
            Object resource = beansWithAnnotation.get(beanName);
            environment.addResource(resource);
            LOG.info("Added resource : " + resource.getClass().getName());
        }
    }


    private void registerConfiguration(T configuration, ConfigurableApplicationContext context) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        // Register ConfigurationPlaceholderConfigurer
        ConfigurationPlaceholderConfigurer placeholderConfigurer = new ConfigurationPlaceholderConfigurer(configuration);
        placeholderConfigurer.setIgnoreUnresolvablePlaceholders(false);
        beanFactory.registerSingleton("dw-placeholder", placeholderConfigurer);

        // Register Configuration
        beanFactory.registerSingleton("dw", configuration);

        // Refresh context now
        context.refresh();
    }

    public ConfigurableApplicationContext getContext() {
        return context;
    }
}
