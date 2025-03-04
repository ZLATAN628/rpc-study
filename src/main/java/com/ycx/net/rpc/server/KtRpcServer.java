package com.ycx.net.rpc.server;

import com.ycx.net.cluster.Coordinator;
import com.ycx.net.cluster.NodeRole;
import com.ycx.net.rpc.codec.RpcProtocol;
import com.ycx.net.rpc.codec.RpcRequest;
import com.ycx.net.rpc.serializer.HessianSerializer;
import com.ycx.net.rpc.serializer.Serializer;
import com.ycx.net.rpc.util.ServiceUtil;
import org.noear.solon.boot.ServerLifecycle;
import org.noear.solon.core.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.extension.plugins.Plugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.ycx.net.cluster.NodeRole.CANDIDATE;

public class KtRpcServer implements ServerLifecycle {
    private static final Logger log = LoggerFactory.getLogger(KtRpcServer.class);
    static String nodeId;

    static {
        // TODO 获取 nodeId
        nodeId = "1";
    }

    private AioQuickServer server;
    private Protocol<RpcRequest> protocol;
    private AbstractMessageProcessor<RpcRequest> processor;
    private ExecutorService executor = new ThreadPoolExecutor(getCoreThreadNum(),
            getCoreThreadNum() * 32,
            60, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new NamedThreadFactory("rpc-server"));

    private final List<Plugin> plugins = new ArrayList<>();
    private final Serializer serializer;
    private final Map<String, Object> serviceMap = new HashMap<>(16);
    // 选举协调器
    private final Coordinator coordinator;

    public KtRpcServer(Coordinator coordinator) {
        this.serializer = new HessianSerializer();
        this.coordinator = coordinator;
    }

    @Override
    public void start(String host, int port) throws Throwable {
        // 等待选举完成 TODO 是否需要异步
        this.coordinator.startElection();
        if (protocol == null) {
            protocol = new RpcProtocol<>(serializer, RpcRequest.class);
        }
        if (processor == null) {
            processor = new KtRpcServerProcessor(serviceMap, serializer, executor);
        }
        plugins.forEach(processor::addPlugin);
        server = new AioQuickServer(host, port, protocol, processor)
                .setBannerEnabled(false)
                .setReadBufferSize(1024 * 8)
                .setThreadNum(Runtime.getRuntime().availableProcessors());

        server.start();
        log.info("server start success on {}:{}", host, port);
    }

    public void stop() {
        server.shutdown();
    }

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public void setProtocol(Protocol<RpcRequest> protocol) {
        this.protocol = protocol;
    }

    public void setProcessor(AbstractMessageProcessor<RpcRequest> processor) {
        this.processor = processor;
    }

    public void addService(String interfaceName, String version, Object bean) {
        String key = ServiceUtil.makeServiceKey(interfaceName, version);
        serviceMap.put(key, bean);
    }

    private int getCoreThreadNum() {
        return Runtime.getRuntime().availableProcessors();
    }
}
