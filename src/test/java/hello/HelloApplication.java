package hello;

import com.yammer.dropwizard.spring.DropwizardContext;
import com.yammer.dropwizard.spring.SpringService;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HelloApplication extends SpringService<HelloApplicationConfiguration> {

    private static final String CONFIGURATION_FILE = "src/test/resources/hello/hello.yml";

    public static void main(String[] args) throws Exception {
        new HelloApplication().run(new String[]{"server", CONFIGURATION_FILE});
    }

    public HelloApplication() {
        super("hello-application");
    }

    @Override
    protected ConfigurableApplicationContext initializeSpring(DropwizardContext parent) throws BeansException {
        // Configuration based on annotation
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.setParent(parent);
//        context.scan("hello");
//        context.refresh();
        return parent;
    }
}
