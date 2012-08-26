package hello.config;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;

public class HelloAppConfiguration extends Configuration {

    @JsonProperty
	private HelloConfiguration hello;

    public HelloConfiguration getHello() {
        return hello;
    }

    public void setHello(HelloConfiguration hello) {
        this.hello = hello;
    }
}
