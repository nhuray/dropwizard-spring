package sample.resources;


import com.yammer.dropwizard.config.HttpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import sample.service.MyService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static java.lang.String.format;

@Path("my-resource")
@Component
public class MyResource {

    @Autowired
    private MyService myService;

    @Value("#{dw.http}")
    private HttpConfiguration http;

    @Value("#{dw.sample.messages[0]}")
    private String title;

    @Value("#{dw.sample.messages[1]}")
    private String description;

    @GET
    public Response doGet() {
        return Response.ok(
                    format("%s<br/>%s<br/>Application is running on port : %d", title, description, http.getPort())).build();
    }

    public MyService getMyService() {
        return myService;
    }

    public Integer getPort() {
        return http.getPort();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
