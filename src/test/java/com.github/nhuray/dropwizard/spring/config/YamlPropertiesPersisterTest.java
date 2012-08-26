package com.github.nhuray.dropwizard.spring.config;

import org.junit.Assert;
import org.junit.Test;


import java.io.InputStream;
import java.util.Properties;

public class YamlPropertiesPersisterTest {

    private YamlPropertiesPersister persister;

    @Test
    public void nominal() throws Exception {
        // Given
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/config/test.yml");

        // When
        persister = new YamlPropertiesPersister();
        persister.load(properties, stream);

        // Then
        Assert.assertEquals("test1", properties.get("test.first"));
        Assert.assertEquals("test2", properties.get("test.second[0]"));
        Assert.assertEquals("test3", properties.get("test.second[1]"));
        Assert.assertEquals("test2,test3", properties.get("test.second"));
        Assert.assertEquals("test4,test5", properties.get("test.third"));
        Assert.assertEquals("4", properties.get("test.fourth"));
    }

    @Test
    public void withPrefix() throws Exception {
        // Given
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/config/test.yml");

        // When
        persister = new YamlPropertiesPersister("prefix.");
        persister.load(properties, stream);

        // Then
        Assert.assertEquals("test1", properties.get("prefix.test.first"));
        Assert.assertEquals("test2", properties.get("prefix.test.second[0]"));
        Assert.assertEquals("test3", properties.get("prefix.test.second[1]"));
        Assert.assertEquals("test2,test3", properties.get("prefix.test.second"));
        Assert.assertEquals("test4,test5", properties.get("prefix.test.third"));
        Assert.assertEquals("4", properties.get("prefix.test.fourth"));
    }
}
