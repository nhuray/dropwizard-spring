package hello.config;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloAppConfiguration extends Configuration {

	@JsonProperty
    @Autowired
	private HelloConfiguration hello;

    public HelloConfiguration getHello() {
        return hello;
    }

}
