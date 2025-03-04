package com.ycx;

import com.ycx.net.rpc.client.KtRpcClient;
import com.ycx.net.test.HelloService;
import com.ycx.net.test.RpcTestService;
import com.ycx.pojo.PatRegister;

import java.util.List;

public class ClientTest {
    public static void main(String[] args) {
        KtRpcClient client = new KtRpcClient("127.0.0.1:4399");
//        HelloService service = client.createService(HelloService.class, "2.0");
//        String result = service.hello("你好");
//        System.out.println(result);

        RpcTestService service = client.createService(RpcTestService.class, "1.0");
        List<PatRegister> result = service.getPatRegisterList(1);
        System.out.println(result);
        client.stop();
    }
}
