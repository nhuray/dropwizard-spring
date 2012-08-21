package hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelloService {

    @Value("#{dw.hello.message}")
    private String message;

    public String greeting() {
        return message;
    }

    public String getMessage() {
        return message;
    }
}
