package hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

public class HelloService {

    private String message;

    public HelloService(String message) {
        this.message = message;
    }

    public String greeting() {
        return message;
    }

    public String getMessage() {
        return message;
    }
}
