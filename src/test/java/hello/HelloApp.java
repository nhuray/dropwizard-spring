package hello;


import com.github.nhuray.dropwizard.spring.DropwizardContext;
import com.github.nhuray.dropwizard.spring.SpringService;
import org.springframework.beans.BeansException;
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
    protected ConfigurableApplicationContext initializeSpring(HelloAppConfiguration configuration, DropwizardContext parent) throws BeansException {
        // Configuration based on annotation
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(parent);
        context.scan("hello");
        context.refresh();
        return context;
    }
}
