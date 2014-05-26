package hello.resources;

import hello.service.HelloService;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
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

    @Value("${server.applicationConnectors[0].port}")
    private Integer port;

    @Value("${server.applicationConnectors[0].type}")
    private String type;

    @Value("#{dw}")
    private Configuration configuration;

    @Value("#{dwEnv}")
    private Environment environment;

    @GET
    public Response doGet() {
        return Response.ok(String.format("%s<br/>Hello application is running on port : %d; connectorType : %s", helloService.greeting(), port, type)).build();
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
