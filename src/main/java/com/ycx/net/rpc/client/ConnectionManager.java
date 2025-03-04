package com.ycx.net.rpc.client;

import com.ycx.net.rpc.client.registry.RpcService;
import com.ycx.net.rpc.client.registry.RpcServiceRegistry;
import com.ycx.net.rpc.codec.RpcProtocol;
import com.ycx.net.rpc.codec.RpcResponse;
import com.ycx.net.rpc.serializer.HessianSerializer;
import com.ycx.net.rpc.serializer.Serializer;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionManager {

    private final Protocol<RpcResponse> protocol;
    private final AbstractMessageProcessor<RpcResponse> messageProcessor;
    private final Map<RpcService, RpcClientSession> connectedServerNodes = new ConcurrentHashMap<>();
    private final Serializer serializer;


    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8,
            600L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    private static class SingletonHolder {
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    private ConnectionManager() {
        this.serializer = new HessianSerializer();
        this.protocol = new RpcProtocol<>(serializer, RpcResponse.class);
        this.messageProcessor = new KtRpcClientProcessor(serializer);
        // TODO 测试
        this.connectServiceNode(RpcServiceRegistry.TEST_NODE);
    }

    public static ConnectionManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void connectServiceNode(RpcService rpcService) {
        threadPoolExecutor.submit(() -> {
            try {
                AioQuickClient client = new AioQuickClient(rpcService.getHost(), rpcService.getPort(), protocol, messageProcessor);
                RpcClientSession session = new RpcClientSession(client, serializer);
                connectedServerNodes.put(rpcService, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public RpcClientSession chooseServerNode(String serviceKey) {
        while (connectedServerNodes.isEmpty()) {
            // TODO wait for service connected
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // TODO choose service node
        return connectedServerNodes.get(RpcServiceRegistry.TEST_NODE);
    }

    public static void stop() {
        threadPoolExecutor.shutdown();
    }

}
