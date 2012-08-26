package com.github.nhuray.dropwizard.spring.config;

import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * {@link org.springframework.util.PropertiesPersister} that reads from a YAML source.
 * <p/>
 * YAML is a nice human-readable format for configuration,
 * and it has some useful hierarchical properties. It's more or less a superset of JSON, so it has a lot of similar
 * features. The Properties created by this factory have nested paths for hierarchical objects, so for instance this
 * YAML
 * <p/>
 * <pre>
 * environments:
 *   dev:
 *     url: http://dev.bar.com
 *     name: Developer Setup
 *   prod:
 *     url: http://foo.bar.com
 *     name: My Cool App
 * </pre>
 * <p/>
 * is transformed into these Properties:
 * <p/>
 * <pre>
 * environments.dev.url=http://dev.bar.com
 * environments.dev.name=Developer Setup
 * environments.prod.url=http://foo.bar.com
 * environments.prod.name=My Cool App
 * </pre>
 * <p/>
 * Lists are represented as comma-separated values (useful for simple String values) and also as property keys with
 * <code>[]</code> dereferencers, for example this YAML:
 * <p/>
 * <pre>
 * servers:
 * - dev.bar.com
 * - foo.bar.com
 * </pre>
 * <p/>
 * becomes java Properties like this:
 * <p/>
 * <pre>
 * servers=dev.bar.com,foo.bar.com
 * servers[0]=dev.bar.com
 * servers[1]=foo.bar.com
 * </pre>
 * <p/>
 * This code was inspired from  <a href="https://gist.github.com/2051955">Dave Syer YamlPropertiesFactoryBean</a>.
 *
 * @author Dave Syer
 */
public class YamlPropertiesPersister extends DefaultPropertiesPersister {


    private String prefix;

    public YamlPropertiesPersister() {
        this("");
    }

    public YamlPropertiesPersister(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void load(Properties props, InputStream is) throws IOException {
        load(props, new InputStreamReader(is));
    }

    /**
     * We want to traverse map representing Yaml object and each time we find String=String pair we want to
     * save it as Property. As we are going deeper into map we generate compound key as path-like String
     *
     * @param props
     * @param reader
     * @throws IOException
     * @see org.springframework.util.PropertiesPersister#load(java.util.Properties, java.io.Reader)
     */
    @Override
    public void load(Properties props, Reader reader) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> map = (Map<String, Object>) yaml.load(reader);
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
                properties.put(prefix + key, value);
            } else if (value instanceof Map) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                assignProperties(properties, map, key);
            } else if (value instanceof Collection) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) value;
                properties.put(prefix + key, StringUtils.collectionToCommaDelimitedString(collection));
                int count = 0;
                for (Object object : collection) {
                    assignProperties(properties, Collections.singletonMap("[" + (count++) + "]", object), key);
                }
            } else {
                properties.put(prefix + key, value == null ? "" : String.valueOf(value));
            }
        }
    }

    @Override
    public void store(Properties props, OutputStream os, String header) throws IOException {
        throw new UnsupportedOperationException("YamlPropertiesPersister is just read-only");
    }

    @Override
    public void store(Properties props, Writer writer, String header) throws IOException {
        throw new UnsupportedOperationException("YamlPropertiesPersister is just read-only");
    }

}