package hello;


import com.github.nhuray.dropwizard.spring.SpringService;
import com.github.nhuray.dropwizard.spring.config.ConfigurationPlaceholderConfigurer;
import com.yammer.dropwizard.config.Environment;
import hello.config.HelloAppConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HelloApp extends SpringService<HelloAppConfiguration> {

    private static final String CONFIGURATION_FILE = "src/test/resources/hello/hello.yml";

    public static void main(String[] args) throws Exception {
        new HelloApp().run(new String[]{"server", CONFIGURATION_FILE});
    }

    public HelloApp() {
        super("hello-application");
    }

    @Override
    protected ConfigurableApplicationContext initializeApplicationContext(HelloAppConfiguration configuration, Environment environment) throws BeansException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("hello");

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        // Register ConfigurationPlaceholderConfigurer
        ConfigurationPlaceholderConfigurer placeholderConfigurer = new ConfigurationPlaceholderConfigurer(configuration);
        placeholderConfigurer.setIgnoreUnresolvablePlaceholders(false); // To test all placeholders are resolved
        placeholderConfigurer.setPlaceholderPrefix("${dw.");
        placeholderConfigurer.setPlaceholderSuffix("}");
        beanFactory.registerSingleton("placeholderConfigurer", placeholderConfigurer);

        // Register Configuration
        beanFactory.registerSingleton("dw", configuration);

        // Refresh
        context.refresh();
        return context;
    }
}
