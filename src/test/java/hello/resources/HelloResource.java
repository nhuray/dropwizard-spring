package hello.resources;


import com.yammer.dropwizard.config.HttpConfiguration;
import hello.HelloApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import hello.service.HelloService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static java.lang.String.format;

@Path("my-resource")
@Component
public class HelloResource {

    @Autowired
    private HelloService myService;

    @Autowired
    private HelloApplicationConfiguration configuration;


//    @Value("#{dw.http}")
    private HttpConfiguration http;

    @GET
    public Response doGet() {
        return Response.ok(myService.greeting()).build();
    }

    public HelloService getMyService() {
        return myService;
    }

    public Integer getPort() {
        return http.getPort();
    }

}
