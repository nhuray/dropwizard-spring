package hello.resources;


import com.yammer.dropwizard.config.HttpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import hello.service.HelloService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static java.lang.String.format;

@Path("/hello")
@Component
public class HelloResource {

    @Autowired
    private HelloService helloService;

    @Value("#{dw.http}")
    private HttpConfiguration http;

    @GET
    public Response doGet() {
        return Response.ok(
                    format("%s<br/>Application is running on port : %d", helloService.greeting(), http.getPort())).build();
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public Integer getPort() {
        return http.getPort();
    }

}
