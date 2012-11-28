package hello.service;

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
