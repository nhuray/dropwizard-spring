package com.github.nhuray.dropwizard.spring;

import com.yammer.dropwizard.config.Configuration;
import org.springframework.context.support.StaticApplicationContext;

/**
 * {@link org.springframework.context.ApplicationContext} implementation to register Dropwizard {@link Configuration} as a Spring bean.
 * <p/>
 * The name of the Dropwizard {@link Configuration} bean is 'dw'.
 * <p/>
 *
 */
public class DropwizardContext extends StaticApplicationContext {

    /**
	 * Name of the Dropwizard {@link Configuration} bean in the factory.
	 */
    public static final String DROPWIZARD_CONFIGURATION = "dw";

    public DropwizardContext(Configuration configuration) {
        // Register dropwizard configuration
        getBeanFactory().registerSingleton(DROPWIZARD_CONFIGURATION, configuration);
        refresh();
    }
}
