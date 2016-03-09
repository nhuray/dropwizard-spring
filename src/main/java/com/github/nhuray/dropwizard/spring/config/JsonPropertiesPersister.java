package com.github.nhuray.dropwizard.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * {@link org.springframework.util.PropertiesPersister} that reads from a JSON source.
 *
 * This code was inspired from  <a href="https://gist.com.com/2051955">Dave Syer YamlPropertiesFactoryBean</a>.
 *
 * @author Dave Syer
 */
public class JsonPropertiesPersister extends DefaultPropertiesPersister {

    private ObjectMapper mapper;

    public JsonPropertiesPersister() {
        this(new ObjectMapper());
    }

    public JsonPropertiesPersister(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void load(Properties props, InputStream is) throws IOException {
        load(props, new InputStreamReader(is));
    }

    /**
     * We want to traverse map representing Json object and each time we find String:String pair we want to
     * save it as Property. As we are going deeper into map we generate compound key as path-like String
     *
     * @param props
     * @param reader
     * @throws IOException
     * @see org.springframework.util.PropertiesPersister#load(java.util.Properties, java.io.Reader)
     */
    @Override
    public void load(Properties props, Reader reader) throws IOException {
        Map map = mapper.readValue(reader, Map.class);
        // now we can populate supplied props
        assignProperties(props, map, null);
    }

    private void assignProperties(Properties properties, Map<String, Object> input, String path) {
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.hasText(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + "." + key;
                }
            }
            Object value = entry.getValue();
            if (value instanceof String) {
                properties.put(key, value);
            } else if (value instanceof Map) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                assignProperties(properties, map, key);
            } else if (value instanceof Collection) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) value;
                properties.put(key, StringUtils.collectionToCommaDelimitedString(collection));
                int count = 0;
                for (Object object : collection) {
                    assignProperties(properties, Collections.singletonMap("[" + (count++) + "]", object), key);
                }
            } else {
                properties.put(key, value == null ? "" : String.valueOf(value));
            }
        }
    }

    @Override
    public void store(Properties props, OutputStream os, String header) throws IOException {
        throw new UnsupportedOperationException("JsonPropertiesPersister is just read-only");
    }

    @Override
    public void store(Properties props, Writer writer, String header) throws IOException {
        throw new UnsupportedOperationException("JsonPropertiesPersister is just read-only");
    }

}