package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    @Autowired
    private MyOtherService myOtherService;


    public MyOtherService getMyOtherService() {
        return myOtherService;
    }

}
