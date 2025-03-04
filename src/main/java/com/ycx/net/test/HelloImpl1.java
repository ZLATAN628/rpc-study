package com.ycx.net.test;

public class HelloImpl1 implements HelloService {

    @Override
    public String hello(String name) {
        return name + " 1";
    }
}
