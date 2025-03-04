package com.ycx.net.rpc.client.registry;

import java.util.List;

public class RpcService {

    private String host;

    private int port;

    private List<RpcServiceInfo> rpcServiceInfos;

    public RpcService(String host, int port, List<RpcServiceInfo> rpcServiceInfos) {
        this.host = host;
        this.port = port;
        this.rpcServiceInfos = rpcServiceInfos;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<RpcServiceInfo> getRpcServiceInfos() {
        return rpcServiceInfos;
    }

    public void setRpcServiceInfos(List<RpcServiceInfo> rpcServiceInfos) {
        this.rpcServiceInfos = rpcServiceInfos;
    }
}
