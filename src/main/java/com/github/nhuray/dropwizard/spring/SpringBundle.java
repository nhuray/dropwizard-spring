package com.github.nhuray.dropwizard.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhuray.dropwizard.spring.config.ConfigurationPlaceholderConfigurer;
import com.google.common.base.Preconditions;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.lifecycle.ServerLifecycleListener;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * A bundle which load Spring Application context to automatically initialize Dropwizard {@link Environment}
 * including health checks, resources, providers, tasks and managed.
 */
public class SpringBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public static final String CONFIGURATION_BEAN_NAME = "dw";
    public static final String ENVIRONMENT_BEAN_NAME = "dwEnv";
    public static final String PLACEHOLDER_BEAN_NAME = "dwPlaceholder";
    public static final String OBJECT_MAPPER_BEAN_NAME = "dwObjectMapper";

    private static final Logger LOG = LoggerFactory.getLogger(SpringBundle.class);

    private ConfigurableApplicationContext context;
    private ConfigurationPlaceholderConfigurer placeholderConfigurer;
    private boolean registerConfiguration;
    private boolean registerEnvironment;

    /**
     * Creates a new SpringBundle to automatically initialize Dropwizard {@link Environment}
     * <p/>
     *
     * @param context the application context to load
     */
    public SpringBundle(ConfigurableApplicationContext context) {
        this(context, false, false, false);
    }

    /**
     * Creates a new SpringBundle to automatically initialize Dropwizard {@link Environment}
     * <p/>
     *
     * @param context               the application context to load
     * @param registerConfiguration register Dropwizard configuration as a Spring Bean.
     * @param registerEnvironment   register Dropwizard environment as a Spring Bean.
     * @param registerPlaceholder   resolve Dropwizard configuration as properties.
     */
    public SpringBundle(ConfigurableApplicationContext context, boolean registerConfiguration, boolean registerEnvironment, boolean registerPlaceholder) {
        if (registerConfiguration || registerEnvironment  || registerPlaceholder) {
            Preconditions.checkArgument(!context.isActive(), "Context must be not active in order to register configuration, environment or placeholder");
        }
        this.context = context;
        this.registerConfiguration = registerConfiguration;
        this.registerEnvironment = registerEnvironment;
        if (registerPlaceholder) this.placeholderConfigurer = new ConfigurationPlaceholderConfigurer();
    }

    /**
     * Creates a new SpringBundle to automatically initialize Dropwizard {@link Environment}
     * <p/>
     *
     * @param context               the application context to load
     * @param registerConfiguration register Dropwizard configuration as a Spring Bean.
     * @param registerEnvironment   register Dropwizard environment as a Spring Bean.
     * @param placeholderConfigurer placeholderConfigurer to resolve Dropwizard configuration as properties.
     */
    public SpringBundle(ConfigurableApplicationContext context, boolean registerConfiguration, boolean registerEnvironment, ConfigurationPlaceholderConfigurer placeholderConfigurer) {
        Preconditions.checkArgument(placeholderConfigurer != null, "PlaceholderConfigurer is required");
        if (registerConfiguration || registerEnvironment  || placeholderConfigurer != null) {
            Preconditions.checkArgument(!context.isActive(), "Context must be not active in order to register configuration, environment or placeholder");
        }
        this.context = context;
        this.registerConfiguration = registerConfiguration;
        this.registerEnvironment = registerEnvironment;
        this.placeholderConfigurer = placeholderConfigurer;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        // Register Dropwizard Configuration as a Bean Spring.
        if (registerConfiguration) registerConfiguration(configuration, context);

        // Register the Dropwizard environment
        if (registerEnvironment) registerEnvironment(environment, context);

        // Register a placeholder to resolve Dropwizard Configuration as properties.
        if (placeholderConfigurer != null) registerPlaceholder(environment, configuration, context);

        // Refresh context if is not active
        if (!context.isActive()) context.refresh();

        // Initialize Dropwizard environment
        registerManaged(environment, context);
        registerLifecycle(environment, context);
        registerServerLifecycleListeners(environment, context);
        registerTasks(environment, context);
        registerHealthChecks(environment, context);
        registerInjectableProviders(environment, context);
        registerProviders(environment, context);
        registerResources(environment, context);
        environment.manage(new SpringContextManaged(context));
    }


    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // nothing doing
    }

    public ConfigurableApplicationContext getContext() {
        return context;
    }

    public void setPlaceholderConfigurer(ConfigurationPlaceholderConfigurer placeholderConfigurer) {
        Preconditions.checkArgument(placeholderConfigurer != null, "PlaceholderConfigurer is required");
        this.placeholderConfigurer = placeholderConfigurer;
    }

    public void setRegisterConfiguration(boolean registerConfiguration) {
        this.registerConfiguration = registerConfiguration;
    }

    public void setRegisterEnvironment(boolean registerEnvironment) {
        this.registerEnvironment = registerEnvironment;
    }

    // ~ Dropwizard Environment initialization methods -----------------------------------------------------------------

    /**
     * Register {@link Managed}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerManaged(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Managed> beansOfType = context.getBeansOfType(Managed.class);
        for (String beanName : beansOfType.keySet()) {
            // Add managed to Dropwizard environment
            Managed managed = beansOfType.get(beanName);
            environment.manage(managed);
            LOG.info("Registering managed: " + managed.getClass().getName());
        }
    }


    /**
     * Register {@link LifeCycle}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerLifecycle(Environment environment, ConfigurableApplicationContext context) {
        Map<String, LifeCycle> beansOfType = context.getBeansOfType(LifeCycle.class);
        for (String beanName : beansOfType.keySet()) {
            // Add lifeCycle to Dropwizard environment
            if (!beanName.equals(ENVIRONMENT_BEAN_NAME)) {
                LifeCycle lifeCycle = beansOfType.get(beanName);
                environment.manage(lifeCycle);
                LOG.info("Registering lifeCycle: " + lifeCycle.getClass().getName());
            }
        }
    }


    /**
     * Register {@link ServerLifecycleListener}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerServerLifecycleListeners(Environment environment, ConfigurableApplicationContext context) {
      Map<String, ServerLifecycleListener> beansOfType = context.getBeansOfType(ServerLifecycleListener.class);
      for (String beanName : beansOfType.keySet()) {
        // Add serverLifecycleListener to Dropwizard environment
        if (!beanName.equals(ENVIRONMENT_BEAN_NAME)) {
          ServerLifecycleListener serverLifecycleListener = beansOfType.get(beanName);
          environment.addServerLifecycleListener(serverLifecycleListener);
          LOG.info("Registering serverLifecycleListener: " + serverLifecycleListener.getClass().getName());
        }
      }
    }


  /**
     * Register {@link Task}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerTasks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Task> beansOfType = context.getBeansOfType(Task.class);
        for (String beanName : beansOfType.keySet()) {
            // Add task to Dropwizard environment
            Task task = beansOfType.get(beanName);
            environment.addTask(task);
            LOG.info("Registering task: " + task.getClass().getName());
        }
    }


    /**
     * Register {@link HealthCheck}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerHealthChecks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, HealthCheck> beansOfType = context.getBeansOfType(HealthCheck.class);
        for (String beanName : beansOfType.keySet()) {
            // Add healthCheck to Dropwizard environment
            HealthCheck healthCheck = beansOfType.get(beanName);
            environment.addHealthCheck(healthCheck);
            LOG.info("Registering healthCheck: " + healthCheck.getClass().getName());
        }
    }


    /**
     * Register {@link InjectableProvider}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerInjectableProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, InjectableProvider> beansOfType = context.getBeansOfType(InjectableProvider.class);
        for (String beanName : beansOfType.keySet()) {
            // Add injectableProvider to Dropwizard environment
            InjectableProvider injectableProvider = beansOfType.get(beanName);
            environment.addProvider(injectableProvider);
            LOG.info("Registering injectable provider: " + injectableProvider.getClass().getName());
        }
    }

    /**
     * Register objects annotated with {@link Provider} in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Provider.class);
        for (String beanName : beansWithAnnotation.keySet()) {
            // Add injectableProvider to Dropwizard environment
            Object provider = beansWithAnnotation.get(beanName);
            environment.addProvider(provider);
            LOG.info("Registering provider : " + provider.getClass().getName());
        }
    }


    /**
     * Register resources annotated with {@link Path} in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerResources(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Path.class);
        for (String beanName : beansWithAnnotation.keySet()) {
            // Add injectableProvider to Dropwizard environment
            Object resource = beansWithAnnotation.get(beanName);
            environment.addResource(resource);
            LOG.info("Registering resource : " + resource.getClass().getName());
        }
    }

    /**
     * Register Dropwizard {@link Configuration} as a Bean Spring.
     *
     * @param configuration Dropwizard {@link Configuration}
     * @param context       spring application context
     */
    private void registerConfiguration(T configuration, ConfigurableApplicationContext context) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton(CONFIGURATION_BEAN_NAME, configuration);
        LOG.info("Registering Dropwizard Configuration under name : " + CONFIGURATION_BEAN_NAME);
    }


    /**
     * Register Dropwizard {@link Environment} as a Bean Spring.
     *
     * @param environment Dropwizard {@link Environment}
     * @param context     Spring application context
     */
    private void registerEnvironment(Environment environment, ConfigurableApplicationContext context) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, environment);
        LOG.info("Registering Dropwizard Environment under name : " + ENVIRONMENT_BEAN_NAME);
    }


    /**
     * Register a placeholder to resolve Dropwizard Configuration as properties.
     *
     * @param configuration Dropwizard configuration
     * @param context       spring application context
     */
    private void registerPlaceholder(Environment environment, T configuration, ConfigurableApplicationContext context) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
      final ObjectMapper objectMapper = environment.getObjectMapperFactory().build();
      placeholderConfigurer.setObjectMapper(objectMapper);
        placeholderConfigurer.setConfiguration(configuration);
        beanFactory.registerSingleton(PLACEHOLDER_BEAN_NAME, placeholderConfigurer);
        beanFactory.registerSingleton(OBJECT_MAPPER_BEAN_NAME, objectMapper);
        LOG.info("Registering Dropwizard Placeholder under name : " + PLACEHOLDER_BEAN_NAME);
    }
}
