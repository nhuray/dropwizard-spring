package hello.resources;


import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.validation.Validator;
import hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
@Component
public class HelloResource {

    @Autowired
    private HelloService helloService;

    @Value("${http.port}")
    private Integer port;

    @Value("#{dw}")
    private Configuration configuration;

    @Value("#{dwEnv}")
    private Environment environment;

    @GET
    public Response doGet() {
        return Response.ok(String.format("%s<br/>Hello application is running on port : %d; connectorType : %s", helloService.greeting(), port, configuration.getHttpConfiguration().getConnectorType())).build();
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public Integer getPort() {
        return port;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
