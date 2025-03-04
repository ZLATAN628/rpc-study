package com.ycx.net.rpc.client;

import com.ycx.net.rpc.client.proxy.RpcProxy;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KtRpcClient {

    private String registryAddress;

    public KtRpcClient(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            8,
            16,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            r -> new Thread(r, "ktsmartrpc-client-" + r.hashCode()),
            new ThreadPoolExecutor.AbortPolicy());


    public static <T, P> T createService(Class<T> interfaceClass, String version) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcProxy<T>(interfaceClass, version)
        );
    }

    public static void submit(Runnable runnable) {
        threadPoolExecutor.submit(runnable);
    }

    public void stop() {
        threadPoolExecutor.shutdown();
        ConnectionManager.stop();
    }


}
