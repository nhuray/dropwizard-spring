package hello.resources;


import hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
@Component
public class HelloResource {

    @Autowired
    private HelloService helloService;

    @GET
    public Response doGet() {
        return Response.ok(helloService.greeting()).build();
    }

    public HelloService getHelloService() {
        return helloService;
    }

}
