package com.ycx.net.rpc.server;

import com.ycx.net.rpc.codec.Beat;
import com.ycx.net.rpc.codec.RpcRequest;
import com.ycx.net.rpc.codec.RpcResponse;
import com.ycx.net.rpc.serializer.Serializer;
import com.ycx.net.rpc.util.ServiceUtil;
import net.sf.cglib.reflect.FastClass;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioSession;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class KtRpcServerProcessor extends AbstractMessageProcessor<RpcRequest> {

    private final Map<String, Object> serviceMap;

    private final Serializer serializer;

    private final ExecutorService executorService;

    public KtRpcServerProcessor(Map<String, Object> serviceMap, Serializer serializer, ExecutorService executorService) {
        this.serviceMap = serviceMap;
        this.serializer = serializer;
        this.executorService = executorService;
    }

    @Override
    public void process0(AioSession aioSession, RpcRequest request) {
        System.out.println("server get msg");
        if (Beat.BEAT_ID.equalsIgnoreCase(request.getRequestId())) {
            return;
        }
        executorService.execute(() -> {
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            try {
                Object result = process1(request);
                response.setResult(result);
            } catch (Throwable t) {
                response.setError(t.toString());
            }

            // write object
            try {
                byte[] data = serializer.serialize(response);
                aioSession.writeBuffer().writeInt(data.length);
                aioSession.writeBuffer().write(data);
                aioSession.writeBuffer().flush();
            } catch (Exception e) {
//            log error
            }
        });
    }

    private Object process1(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        String version = request.getVersion();
        String serviceKey = ServiceUtil.makeServiceKey(className, version);
        Object serviceBean = serviceMap.get(serviceKey);
        if (serviceBean == null) {
            return null;
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // JDK reflect
//        Method method = serviceClass.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        return method.invoke(serviceBean, parameters);

        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        return serviceFastClass.invoke(methodIndex, serviceBean, parameters);
    }

    @Override
    public void stateEvent0(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }
}
