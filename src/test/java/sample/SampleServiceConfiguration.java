package sample;

import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.config.LoggingConfiguration;
import org.codehaus.jackson.annotate.JsonProperty;

public class SampleServiceConfiguration extends Configuration {

	@JsonProperty
	private SampleConfiguration sample;
	
	public SampleConfiguration getSample() {
		return sample;
	}

    public HttpConfiguration getHttp(){
        return getHttpConfiguration();
    }

    public LoggingConfiguration getLogging(){
        return getLoggingConfiguration();
    }

}
