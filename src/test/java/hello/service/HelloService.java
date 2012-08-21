package hello.service;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static java.lang.String.format;

public class HelloService {

    //@Value("#{dw.hello.messages}")
    private List<String> messages;

    public HelloService(List<String> messages) {
        this.messages = messages;
    }

    public String greeting() {
        if (messages != null && messages.size() == 2) {
            return format("%s<br/>%s<br/>Application is running on port : %d", messages.get(0), messages.get(1));
        }
        return "Oops";
    }

}
