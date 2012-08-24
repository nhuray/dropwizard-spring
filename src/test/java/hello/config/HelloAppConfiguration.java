package hello.config;

import com.yammer.dropwizard.config.Configuration;
import lombok.Data;
import lombok.Getter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloAppConfiguration extends Configuration {

    @Autowired
	private @Getter HelloConfiguration hello;

}
