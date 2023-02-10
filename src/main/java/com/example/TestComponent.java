package com.example;


import com.example.annotation.Component;
import com.example.annotation.Start;

@Component
public class TestComponent {

    @Start
    public void start() {
        System.out.println("Test component start !");
    }

    public void print(String text) {
        System.out.println("text: " + text);
    }
}
