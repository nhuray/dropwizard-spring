package com.yammer.dropwizard.spring;

import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.logging.Log;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Service which automatically adds items to the service environment,
 * including health checks, resources.
 * <p/>
 * This code was inspired from  <a href="https://github.com/jaredstehler/dropwizard-guice">dropwizard-guice</a>.
 *
 * @author jstehler
 * @author nhuray
 */
public abstract class SpringService<T extends Configuration> extends Service<T> {

    private static final Log LOG = Log.forClass(SpringService.class);

    private ApplicationContext context;

    protected SpringService(String name, ApplicationContext context) {
        super(name);
        this.context = context;
    }

    @Override
    protected void initialize(T configuration, Environment environment) throws Exception {
        addHealthChecks(environment, context);
        addProviders(environment, context);
        addInjectableProviders(environment, context);
        addResources(environment, context);
        addTasks(environment, context);
        addManaged(environment, context);
    }

    private void addManaged(Environment environment, ApplicationContext context) {
        final Map<String, Managed> beansOfType = context.getBeansOfType(Managed.class);
        for (Managed managed : beansOfType.values()) {
            environment.manage(managed);
            LOG.info("Added managed: " + managed.getClass().getName());
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
