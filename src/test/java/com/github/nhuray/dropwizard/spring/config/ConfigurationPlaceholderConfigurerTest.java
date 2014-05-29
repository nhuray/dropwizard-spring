package com.github.nhuray.dropwizard.spring.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import org.junit.Before;
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

    private ConfigurationPlaceholderConfigurer placeholder;

    @Before
    public void setUp() throws Exception {
        placeholder = new ConfigurationPlaceholderConfigurer();
    }

    @Test
    public void defaultPlaceholder() {
        // Given
        placeholder.setConfiguration(new Configuration());

        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("testBean",
                rootBeanDefinition(ConfigurationTestBean.class)
                        .addPropertyValue("maxThreads", "${server.maxThreads}")
                        .getBeanDefinition()
        );
        placeholder.setObjectMapper(new ObjectMapper());

        // When
        placeholder.postProcessBeanFactory(bf);

        // Then
        ConfigurationTestBean bean = bf.getBean(ConfigurationTestBean.class);
        assertThat(bean.getMaxThreads(), equalTo(1024));
    }

    @Test
    public void customPlaceholderPrefixAndSuffix() {
        // Given
        placeholder.setConfiguration(new Configuration());
        placeholder.setPlaceholderPrefix("@<");
        placeholder.setPlaceholderSuffix(">");

        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("testBean",
                rootBeanDefinition(ConfigurationTestBean.class)
                        .addPropertyValue("maxThreads", "@<server.maxThreads>")
                        .addPropertyValue("rootPath", "${key2}")
                        .getBeanDefinition()
        );
        placeholder.setObjectMapper(new ObjectMapper());

        // When
        placeholder.postProcessBeanFactory(bf);

        // Then
        ConfigurationTestBean bean = bf.getBean(ConfigurationTestBean.class);
        assertThat(bean.getMaxThreads(), is(1024));
        assertThat(bean.getRootPath(), is("${key2}"));
    }

    @Test
    public void nullValueIsPreserved() {
        // Given
        placeholder.setConfiguration(new ConfigurationWithNull());
        placeholder.setNullValue("nullField");

        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("testBean",
                rootBeanDefinition(ConfigurationTestBean.class)
                        .addPropertyValue("connectorType", "${nullProperty}")
                        .getBeanDefinition()
        );
        placeholder.setObjectMapper(new ObjectMapper());

        // When
        placeholder.postProcessBeanFactory(bf);

        // Then
        ConfigurationTestBean bean = bf.getBean(ConfigurationTestBean.class);
        assertThat(bean.getConnectorType(), nullValue());
    }

    private class ConfigurationWithNull extends Configuration {
        @JsonProperty
        private String nullProperty = "nullField";
    }
}