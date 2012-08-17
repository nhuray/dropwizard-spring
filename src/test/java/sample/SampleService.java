package sample;

import com.yammer.dropwizard.spring.SpringService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SampleService extends SpringService<SampleServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new SampleService().run(new String[] {"server", "src/test/resources/sample.yml"});
    }

    public SampleService() {
        super("sample-service", new AnnotationConfigApplicationContext("sample"));
    }

}
