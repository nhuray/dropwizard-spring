package com.github.nhuray.dropwizard.spring.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * Tests for {@link ConfigurationPlaceholderConfigurer}
 * <p/>
 * Adapted from <a href="https://com.com/SpringSource/spring-framework/blob/master/spring-beans/src/test/java/org/springframework/beans/factory/config/PropertyPlaceholderConfigurerTests.java" >PropertyPlaceholderConfigurerTests</a>
 */
public class ConfigurationPlaceholderConfigurerTest {

    @Test
    public void defaultPlaceholder() {
        // Given
        Configuration defaultConfiguration = new Configuration();
        ConfigurationPlaceholderConfigurer dc = new ConfigurationPlaceholderConfigurer(defaultConfiguration);


        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("testBean",
                rootBeanDefinition(ConfigurationTestBean.class)
                        .addPropertyValue("connectorType", "${http.connectorType}")
                        .getBeanDefinition());

        // When
        dc.postProcessBeanFactory(bf);

        // Then
        ConfigurationTestBean bean = bf.getBean(ConfigurationTestBean.class);
        assertThat(bean.getConnectorType(), equalTo("blocking"));
    }

    @Test
    public void customPlaceholderPrefixAndSuffix() {
        // Given
        Configuration defaultConfiguration = new Configuration();
        ConfigurationPlaceholderConfigurer dc = new ConfigurationPlaceholderConfigurer(defaultConfiguration);
        dc.setPlaceholderPrefix("@<");
        dc.setPlaceholderSuffix(">");

        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("testBean",
                rootBeanDefinition(ConfigurationTestBean.class)
                        .addPropertyValue("connectorType", "@<http.connectorType>")
                        .addPropertyValue("rootPath", "${key2}")
                        .getBeanDefinition());

        // When
        dc.postProcessBeanFactory(bf);

        // Then
        ConfigurationTestBean bean = bf.getBean(ConfigurationTestBean.class);
        assertThat(bean.getConnectorType(), is("blocking"));
        assertThat(bean.getRootPath(), is("${key2}"));
    }

    @Test
    public void nullValueIsPreserved() {
        // Given
        Configuration configuration = new ConfigurationWithNull();
        ConfigurationPlaceholderConfigurer dc = new ConfigurationPlaceholderConfigurer(configuration);
        dc.setNullValue("nullField");

        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("testBean",
                rootBeanDefinition(ConfigurationTestBean.class)
                        .addPropertyValue("connectorType", "${nullProperty}")
                        .getBeanDefinition());

        // When
        dc.postProcessBeanFactory(bf);

        // Then
        ConfigurationTestBean bean = bf.getBean(ConfigurationTestBean.class);
        assertThat(bean.getConnectorType(), nullValue());
    }

    private class ConfigurationWithNull extends Configuration {
        @JsonProperty
        private String nullProperty = "nullField";
    }


}