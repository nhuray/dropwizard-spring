package hello;

import com.yammer.dropwizard.spring.DropwizardConfiguration;
import hello.service.HelloService;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@DropwizardConfiguration
public class HelloConfiguration {

    @JsonProperty
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    @Bean
    public HelloService helloService(){
         return new HelloService(messages);
    }
}
