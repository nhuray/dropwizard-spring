package hello.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class HelloResponseFilter implements ContainerResponseFilter {

    public HelloResponseFilter() {
        System.out.print("ctor");
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {

    }
}
