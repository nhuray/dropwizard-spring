package hello;

import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.config.LoggingConfiguration;
import org.codehaus.jackson.annotate.JsonProperty;

public class HelloAppConfiguration extends Configuration {

	@JsonProperty
	private HelloConfiguration hello;
	
	public HelloConfiguration getHello() {
		return hello;
	}

    public HttpConfiguration getHttp(){
        return getHttpConfiguration();
    }

    public LoggingConfiguration getLogging(){
        return getLoggingConfiguration();
    }

}
