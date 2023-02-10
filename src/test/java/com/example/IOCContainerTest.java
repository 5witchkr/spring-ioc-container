package com.example;

import com.example.container.IOCContainer;
import org.junit.jupiter.api.Test;


public class IOCContainerTest {

    @Test
    public void test() {
        IOCContainer container = IOCContainer.createContainer("com.example");
        container.start();
    }
}
