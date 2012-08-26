package com.github.nhuray.dropwizard.spring.config;

import org.junit.Assert;
import org.junit.Test;


import java.io.InputStream;
import java.util.Properties;

public class JsonPropertiesPersisterTest {

    private JsonPropertiesPersister persister;

    @Test
    public void nominal() throws Exception {
        // Given
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/config/test.json");

        // When
        persister = new JsonPropertiesPersister();
        persister.load(properties, stream);

        // Then
        Assert.assertEquals("test1", properties.get("test.first"));
        Assert.assertEquals("test2", properties.get("test.second[0]"));
        Assert.assertEquals("test3", properties.get("test.second[1]"));
        Assert.assertEquals("test2,test3", properties.get("test.second"));
        Assert.assertEquals("4", properties.get("test.third"));
    }

}
