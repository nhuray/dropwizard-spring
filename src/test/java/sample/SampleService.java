package sample;

import com.yammer.dropwizard.spring.DropwizardContext;
import com.yammer.dropwizard.spring.SpringService;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class SampleService extends SpringService<SampleServiceConfiguration> {

    private static final String CONFIGURATION_FILE = "src/test/resources/sample/sample.yml";

    public static void main(String[] args) throws Exception {
        new SampleService().run(new String[]{"server", CONFIGURATION_FILE});
    }

    public SampleService() {
        super("sample-application");
    }

    @Override
    protected ConfigurableApplicationContext initializeSpring(SampleServiceConfiguration configuration, DropwizardContext parent) throws Exception {
        // Configuration based on annotation
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(parent);
        context.scan("sample");
        context.refresh();
        return context;
    }
}
