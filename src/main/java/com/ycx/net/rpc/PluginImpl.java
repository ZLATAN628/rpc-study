package com.ycx.net.rpc;

import com.ycx.net.cluster.Coordinator;
import com.ycx.net.http.handler.MultipartUtil;
import com.ycx.net.rpc.server.KtRpcServer;
import org.noear.solon.boot.ServerProps;

import java.net.InetSocketAddress;

import static com.ycx.net.cluster.impl.AbstractCoordinator.CoordinatorBuilder;

public class PluginImpl {
    private KtRpcServer _server;

    public void start() {
        try {
            start0();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void start0() throws Throwable {
        //初始化属性
        ServerProps.init();
        MultipartUtil.init();

        // TODO 配置获取
        String host = "172.16.142.208";
        String allNodesAddress = "server.3=172.16.142.208:8077:8076,server.2=172.16.140.41:8077:8076,server.1=172.16.145.239:8077:8076";
        int port = 8077;
        int nodeId = 3;
        // 构建选举协调器
        Coordinator coordinator = new CoordinatorBuilder()
                .nodeId(nodeId)
                .connectThreadsSize(10)
                .allNodesAddress(allNodesAddress)
                .buildIdCoordinator();

        _server = new KtRpcServer(coordinator);
//        if (props.isIoBound()) {
        //如果是io密集型的，加二段线程池
//            _server.setWorkExecutor(props.newWorkExecutor("ktsmartrpc-"));
//        }
        //尝试事件扩展
        _server.start(host, port);
    }

    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;
        }
    }
}
