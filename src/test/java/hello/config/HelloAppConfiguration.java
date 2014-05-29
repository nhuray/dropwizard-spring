package hello.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

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
