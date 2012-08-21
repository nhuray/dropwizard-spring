package com.yammer.dropwizard.spring;

import com.yammer.dropwizard.config.Configuration;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * {@link org.springframework.context.ApplicationContext} implementation to register Dropwizard {@link Configuration} as a Spring bean.
 * <p/>
 * The name of the Dropwizard {@link Configuration} bean is 'dw'.
 * <p/>
 */
public class DropwizardContext<T extends Configuration> extends AnnotationConfigApplicationContext {

    /**
     * Name of the Dropwizard {@link Configuration} bean in the factory.
     */
    public static final String DROPWIZARD_CONFIGURATION = "dw";

    public DropwizardContext(T configuration) throws IllegalAccessException {
        // Register Dropwizard configuration as a Spring bean
        BeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName(configuration.getClass().getName());
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        Field[] fields = configuration.getClass().getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field); // Make accessible
            propertyValues.add(field.getName(), field.get(configuration));
        }
        registerBeanDefinition(configuration.getClass().getName(), beanDefinition);
        refresh();
    }
}
