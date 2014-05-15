package hello.resources;


import hello.service.HelloService;
import io.dropwizard.Configuration;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.jetty.HttpsConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
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

    @Value("#{dw}")
    private Configuration configuration;

    @Value("#{dwEnv}")
    private Environment environment;

    @GET
    public Response doGet() {
        DefaultServerFactory serverFactory = (DefaultServerFactory) configuration.getServerFactory();
        HttpConnectorFactory connectorFactory = (HttpConnectorFactory) serverFactory.getApplicationConnectors().get(0);
        String type = connectorFactory instanceof HttpsConnectorFactory ? "https" : "http";
        int port = connectorFactory.getPort();
        return Response.ok(String.format("%s<br/>Hello application is running on port : %d; connectorType : %s", helloService.greeting(), port, type)).build();
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public Integer getPort() {
        DefaultServerFactory serverFactory = (DefaultServerFactory) configuration.getServerFactory();
        HttpConnectorFactory connectorFactory = (HttpConnectorFactory) serverFactory.getApplicationConnectors().get(0);
        return connectorFactory.getPort();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
