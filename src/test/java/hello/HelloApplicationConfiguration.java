package hello;

import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.config.LoggingConfiguration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "hello")
public class HelloApplicationConfiguration extends com.yammer.dropwizard.config.Configuration {

    @JsonProperty
    protected HelloConfiguration hello;

//    public HelloConfiguration getHello() {
//        return hello;
//    }

    public HttpConfiguration getHttp() {
        return getHttpConfiguration();
    }

    public LoggingConfiguration getLogging() {
        return getLoggingConfiguration();
    }

//    public void setHello(HelloConfiguration hello) {
//        this.hello = hello;
//    }
}
