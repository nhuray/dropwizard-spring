package hello.config;

import com.github.nhuray.dropwizard.spring.config.YamlPropertiesPersister;
import hello.health.HelloHealthCheck;
import hello.service.HelloService;
import hello.tasks.HelloTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE;

@Configuration
public class HelloConfiguration {

    @Value("#{dw.hello.message}")
    private String message;

    @Bean
    public HelloService helloService() {
        return new HelloService(message);
    }

    @Bean
    public HelloTask helloTask() {
        return new HelloTask();
    }

    @Bean
    public HelloHealthCheck helloHealthCheck() {
        return new HelloHealthCheck();
    }

    public String getMessage() {
        return message;
    }

//    @Bean
//    public PropertyPlaceholderConfigurer configurer() {
//        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
//        configurer.setPropertiesPersister(new YamlPropertiesPersister("pouet."));
//        configurer.setLocation(new ClassPathResource("hello/hello.yml"));
//        configurer.setIgnoreUnresolvablePlaceholders(true);
//        configurer.setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
//        return configurer;
//    }
}
