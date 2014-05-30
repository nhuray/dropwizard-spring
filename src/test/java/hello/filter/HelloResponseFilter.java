package hello.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class HelloResponseFilter implements ContainerResponseFilter {

    public HelloResponseFilter() {
        System.out.print("ctor");
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        return null;
    }
}
