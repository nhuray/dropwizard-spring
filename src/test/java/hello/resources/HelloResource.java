package hello.resources;


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

    @Value("#{dw.httpConfiguration.port}")
    private Integer port;

    @GET
    public Response doGet() {
        return Response.ok(String.format("%s<br/>Hello application is running on port : %d", helloService.greeting(), port)).build();
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public Integer getPort() {
        return port;
    }
}
