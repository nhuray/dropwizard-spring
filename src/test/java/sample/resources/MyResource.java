package sample.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sample.service.MyService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("my-resource")
@Component
public class MyResource {

    @Autowired
    private MyService myService;

    @GET
    public Response doGet() {
        return Response.ok("Service Initialized : " + (myService != null)).build();
    }

    public MyService getMyService() {
        return myService;
    }

}
