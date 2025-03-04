package com.ycx.net.rpc.client;

import com.ycx.net.rpc.codec.RpcResponse;
import com.ycx.net.rpc.serializer.Serializer;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KtRpcClientProcessor extends AbstractMessageProcessor<RpcResponse> {

    private final Serializer serializer;

    public KtRpcClientProcessor(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void process0(AioSession aioSession, RpcResponse response) {
        Object attachment = aioSession.getAttachment();
        if (attachment instanceof ConcurrentHashMap) {
            Map<String, RpcFuture> map = (Map<String, RpcFuture>) attachment;
            RpcFuture future = map.remove(response.getRequestId());
            if (future != null) {
                future.done(response);
            }
        }
    }

    @Override
    public void stateEvent0(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }
}
