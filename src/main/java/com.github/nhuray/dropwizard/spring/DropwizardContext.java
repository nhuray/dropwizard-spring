package com.github.nhuray.dropwizard.spring;

import com.yammer.dropwizard.config.Configuration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * {@link org.springframework.context.ApplicationContext} implementation to register Dropwizard {@link Configuration} as a Spring bean.
 * <p/>
 * The name of the Dropwizard {@link Configuration} bean is 'dw'.
 * <p/>
 */
public class DropwizardContext extends AnnotationConfigApplicationContext {

    /**
     * Name of the Dropwizard {@link Configuration} bean in the factory.
     */
    public static final String DROPWIZARD_CONFIGURATION = "dw";

    public DropwizardContext(Configuration configuration) {
        // Register dropwizard configuration
        getBeanFactory().registerSingleton(DROPWIZARD_CONFIGURATION, configuration);

        BeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName(configuration.getClass().getName());
        registerBeanDefinition(configuration.getClass().getName(), beanDefinition);
        refresh();

    }
}
