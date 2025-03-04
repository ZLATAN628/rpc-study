package com.ycx.net.rpc.client.registry;

import java.util.ArrayList;
import java.util.List;

public class RpcServiceRegistry {

    static {
        List<RpcServiceInfo> serviceInfos = new ArrayList<>();
        serviceInfos.add(new RpcServiceInfo("com.ycx.net.test.HelloService", "1.0"));
        serviceInfos.add(new RpcServiceInfo("com.ycx.net.test.RpcTestService", "1.0"));
        TEST_NODE = new RpcService("172.16.142.208", 12877, serviceInfos);
    }

    private final List<RpcService> rpcServices;

    public static final RpcService TEST_NODE;

    public RpcServiceRegistry() {
        this.rpcServices = new ArrayList<>();
        // TODO 测试数据
        this.rpcServices.add(TEST_NODE);
    }

    public List<RpcService> getRpcServices() {
        return rpcServices;
    }

}
