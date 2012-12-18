package hello;


import com.github.nhuray.dropwizard.spring.SpringBundle;
import com.github.nhuray.dropwizard.spring.config.ConfigurationPlaceholderConfigurer;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import hello.config.HelloAppConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HelloApp extends Service<HelloAppConfiguration> {

    private static final String CONFIGURATION_FILE = "src/test/resources/hello/hello.yml";

    public static void main(String[] args) throws Exception {
        new HelloApp().run(new String[]{"server", CONFIGURATION_FILE});
    }

    @Override
    public void initialize(Bootstrap<HelloAppConfiguration> bootstrap) {
        bootstrap.addBundle(new SpringBundle(applicationContext(), true, true, true));
    }

    @Override
    public void run(HelloAppConfiguration configuration, Environment environment) throws Exception {
       // doing nothing
    }


    private ConfigurableApplicationContext applicationContext() throws BeansException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("hello");
        return context;
    }
}
