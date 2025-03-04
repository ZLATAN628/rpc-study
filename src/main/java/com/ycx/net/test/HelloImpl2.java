package com.ycx.net.test;

public class HelloImpl2 implements HelloService {

    @Override
    public String hello(String name) {
        return name + " 2";
    }
}
