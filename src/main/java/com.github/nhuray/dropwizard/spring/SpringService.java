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
 * This code was inspired from  <a href="https://github.com/jaredstehler/dropwizard-guice">dropwizard-guice</a>.
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
        // Initialize Dropwizard context
        DropwizardContext parent = new DropwizardContext(configuration);
        ConfigurableApplicationContext context = initializeSpring(configuration, parent);

        // Check if the application context is active
        if (!context.isActive())
            context.refresh();

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
     * Initialization method for Spring application context.
     * <p/>
     * The parent context may be used to register Dropwizard {@link Configuration} as a Spring bean.
     *
     * @param configuration dropwizard configuration.
     * @param parent        the dropwizard parent context
     * @return the application context
     * @throws Exception
     */
    protected abstract ConfigurableApplicationContext initializeSpring(T configuration, DropwizardContext parent) throws BeansException;


    // ~ Dropwizard Environment initialization methods

    private void addManaged(Environment environment, ApplicationContext context) {
        final Map<String, Managed> beansOfType = context.getBeansOfType(Managed.class);
        for (Managed managed : beansOfType.values()) {
            environment.manage(managed);
            LOG.info("Added managed: " + managed.getClass().getName());
        }
    }

    private void addLifecycle(Environment environment, ApplicationContext context) {
        Map<String, LifeCycle> beansOfType = context.getBeansOfType(LifeCycle.class);
        for (LifeCycle lifeCycle : beansOfType.values()) {
            environment.manage(lifeCycle);
            LOG.info("Added lifeCycle: " + lifeCycle.getClass().getName());
        }
    }

    private void addTasks(Environment environment, ApplicationContext context) {
        final Map<String, Task> beansOfType = context.getBeansOfType(Task.class);
        for (Task task : beansOfType.values()) {
            environment.addTask(task);
            LOG.info("Added task: " + task.getClass().getName());
        }
    }

    private void addHealthChecks(Environment environment, ApplicationContext context) {
        final Map<String, HealthCheck> beansOfType = context.getBeansOfType(HealthCheck.class);
        for (HealthCheck healthCheck : beansOfType.values()) {
            environment.addHealthCheck(healthCheck);
            LOG.info("Added healthCheck: " + healthCheck.getClass().getName());
        }
    }

    private void addInjectableProviders(Environment environment, ApplicationContext context) {
        final Map<String, InjectableProvider> beansOfType = context.getBeansOfType(InjectableProvider.class);
        for (InjectableProvider injectableProvider : beansOfType.values()) {
            environment.addProvider(injectableProvider);
            LOG.info("Added injectable provider: " + injectableProvider.getClass().getName());
        }
    }

    private void addProviders(Environment environment, ApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Provider.class);
        for (Object provider : beansWithAnnotation.values()) {
            environment.addProvider(provider);
            LOG.info("Added provider : " + provider.getClass().getName());
        }
    }

    private void addResources(Environment environment, ApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Path.class);
        for (Object resource : beansWithAnnotation.values()) {
            environment.addResource(resource);
            LOG.info("Added resource : " + resource.getClass().getName());
        }
    }

}
