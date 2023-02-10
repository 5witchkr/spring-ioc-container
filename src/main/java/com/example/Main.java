package com.example;


import com.example.container.IOCContainer;

public class Main {
    public static void main(String[] args) {
        IOCContainer.createContainer("com.example").start();
    }
}