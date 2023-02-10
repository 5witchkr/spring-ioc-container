package com.example;

import com.example.annotation.Autowired;
import com.example.annotation.Component;
import com.example.annotation.Start;


@Component
public class MyApp {

    @Autowired
    private TestComponent testComponent;


    @Start
    public void start(){
        testComponent.print("TEST!!");
    }
}
