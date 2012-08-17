package sample;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class SampleConfiguration {

    @JsonProperty
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

}
