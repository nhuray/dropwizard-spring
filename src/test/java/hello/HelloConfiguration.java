package hello;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class HelloConfiguration {

    @JsonProperty
    private String message;

    public String getMessage() {
        return message;
    }
}
