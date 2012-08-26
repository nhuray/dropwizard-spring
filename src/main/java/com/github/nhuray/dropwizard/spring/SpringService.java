package com.github.nhuray.dropwizard.spring;

import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.logging.Log;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.eclipse.jetty.util.component.LifeCycle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Service which load Spring Application context to automatically initialize Dropwizard {@link Environment}
 * including health checks, resources, providers, tasks and managed.
 * <p/>
 * <p/>
 * This code was inspired from  <a href="https://com.com/jaredstehler/dropwizard-guice">dropwizard-guice</a>.
 *
 * @author jstehler
 * @author nhuray
 */
public abstract class SpringService<T extends Configuration> extends Service<T> {

    private static final Log LOG = Log.forClass(SpringService.class);

    protected SpringService(String name) {
        super(name);
    }

    @Override
    protected void initialize(T configuration, Environment environment) throws Exception {
        // User initilization of Spring Application context
        ConfigurableApplicationContext context = initializeApplicationContext(configuration, environment);

        // Initialize Dropwizard environment
        addHealthChecks(environment, context);
        addProviders(environment, context);
        addInjectableProviders(environment, context);
        addResources(environment, context);
        addTasks(environment, context);
        addManaged(environment, context);
        addLifecycle(environment, context);
    }

    /**
     * Initialization method for a Spring {@link ApplicationContext}.
     * <p/>
     *
     * @param configuration dropwizard configuration
     * @param environment   dropwizard environment
     * @return the application context
     * @throws BeansException if context creation failed
     */
    protected abstract ConfigurableApplicationContext initializeApplicationContext(T configuration, Environment environment) throws BeansException;


    // ~ Dropwizard Environment initialization methods

    private void addManaged(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Managed> beansOfType = context.getBeansOfType(Managed.class);
        for (String beanName : beansOfType.keySet()) {
            // Add managed to Dropwizard environment
            Managed managed = beansOfType.get(beanName);
            environment.manage(managed);
            LOG.info("Added managed: " + managed.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void addLifecycle(Environment environment, ConfigurableApplicationContext context) {
        Map<String, LifeCycle> beansOfType = context.getBeansOfType(LifeCycle.class);
        for (String beanName : beansOfType.keySet()) {
            // Add lifeCycle to Dropwizard environment
            LifeCycle lifeCycle = beansOfType.get(beanName);
            environment.manage(lifeCycle);
            LOG.info("Added lifeCycle: " + lifeCycle.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void addTasks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Task> beansOfType = context.getBeansOfType(Task.class);
        for (String beanName : beansOfType.keySet()) {
            // Add task to Dropwizard environment
            Task task = beansOfType.get(beanName);
            environment.addTask(task);
            LOG.info("Added task: " + task.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void addHealthChecks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, HealthCheck> beansOfType = context.getBeansOfType(HealthCheck.class);
        for (String beanName : beansOfType.keySet()) {
            // Add healthCheck to Dropwizard environment
            HealthCheck healthCheck = beansOfType.get(beanName);
            environment.addHealthCheck(healthCheck);
            LOG.info("Added healthCheck: " + healthCheck.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void addInjectableProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, InjectableProvider> beansOfType = context.getBeansOfType(InjectableProvider.class);
        for (String beanName : beansOfType.keySet()) {
            // Add injectableProvider to Dropwizard environment
            InjectableProvider injectableProvider = beansOfType.get(beanName);
            environment.addProvider(injectableProvider);
            LOG.info("Added injectable provider: " + injectableProvider.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void addProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Provider.class);
        for (String beanName : beansWithAnnotation.keySet()) {
            // Add injectableProvider to Dropwizard environment
            Object provider = beansWithAnnotation.get(beanName);
            environment.addProvider(provider);
            LOG.info("Added provider : " + provider.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void addResources(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Path.class);
        for (String beanName : beansWithAnnotation.keySet()) {
            // Add injectableProvider to Dropwizard environment
            Object resource = beansWithAnnotation.get(beanName);
            environment.addResource(resource);
            LOG.info("Added resource : " + resource.getClass().getName());
            // Remove from Spring application context
            removeBeanDefinition(context, beanName);
        }
    }

    private void removeBeanDefinition(ConfigurableApplicationContext context, String beanName) {
        ConfigurableListableBeanFactory configurableListableBeanFactory = context.getBeanFactory();
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableListableBeanFactory;
        // Removing the bean from container
        beanDefinitionRegistry.removeBeanDefinition(beanName);
    }

}
