package com.ycx.net.rpc.client;

import com.ycx.net.rpc.codec.RpcRequest;
import com.ycx.net.rpc.serializer.Serializer;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientSession {
    private ConcurrentHashMap<String, RpcFuture> pendingRPC = new ConcurrentHashMap<>();
    private final AioSession aioSession;
    private final AioQuickClient aioClient;
    private final Serializer serializer;
    private final Map<String, RpcFuture> rpcFutureMap = new ConcurrentHashMap<>();

    public RpcClientSession(AioQuickClient aioClient, Serializer serializer) {
        this.aioClient = aioClient;
        try {
            this.aioSession = aioClient.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serializer = serializer;
        aioSession.setAttachment(rpcFutureMap);
    }

    public RpcFuture sendRequest(RpcRequest request) {
        RpcFuture future = new RpcFuture(request);
        System.out.println("client send request");
        rpcFutureMap.put(request.getRequestId(), future);
        try {
            byte[] data = serializer.serialize(request);
            aioSession.writeBuffer().writeInt(data.length);
            aioSession.writeBuffer().write(data);
            aioSession.writeBuffer().flush();
        } catch (Exception e) {
//            log error
        }
        return future;
    }

    public void close() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        aioClient.shutdown();
    }
}
