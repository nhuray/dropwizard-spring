package hello.config;

import hello.health.HelloHealthCheck;
import hello.service.HelloService;
import hello.tasks.HelloTask;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfiguration {

    @Value("${dw.hello.message}")
    @JsonProperty
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

    public void setMessage(String message) {
        this.message = message;
    }

}
