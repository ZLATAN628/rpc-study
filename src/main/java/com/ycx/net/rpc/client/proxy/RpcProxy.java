package com.ycx.net.rpc.client.proxy;

import com.ycx.net.rpc.client.ConnectionManager;
import com.ycx.net.rpc.client.RpcClientSession;
import com.ycx.net.rpc.client.RpcFuture;
import com.ycx.net.rpc.codec.RpcRequest;
import com.ycx.net.rpc.util.ServiceUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class RpcProxy<T> implements InvocationHandler {

    private Class<T> klass;
    private String version;

    public RpcProxy(Class<T> klass, String version) {
        this.klass = klass;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        String className = method.getDeclaringClass().getName();
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setVersion(version);
        request.setClassName(className);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        String serviceKey = ServiceUtil.makeServiceKey(className, version);
        RpcClientSession rpcClientSession = ConnectionManager.getInstance().chooseServerNode(serviceKey);
        RpcFuture future = rpcClientSession.sendRequest(request);
        Object res = future.get();
        // TODO 何时关闭 rpc 连接
        rpcClientSession.close();
        return res;
    }
}
