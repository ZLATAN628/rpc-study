package com.ycx;

import com.ycx.net.rpc.server.KtRpcServer;
import com.ycx.net.test.*;

public class ServerTest {

    private String tx;

    public static void main(String[] args) throws Throwable {

        KtRpcServer server = new KtRpcServer(null);
        HelloImpl1 h1 = new HelloImpl1();
        HelloImpl2 h2 = new HelloImpl2();
        RpcTestImpl rpcTest = new RpcTestImpl();
        server.addService(HelloService.class.getName(), "1.0", h1);
        server.addService(HelloService.class.getName(), "2.0", h2);
        server.addService(RpcTestService.class.getName(), "1.0", rpcTest);
        server.start("172.16.142.208", 12877);


//        Solon.start(Main.class, args, (app) -> {
//            System.out.println(app.enableCaching());
//        });
    }

}