package com.ycx.test.http;

import com.ycx.test.http.handler.MultipartUtil;
import org.noear.solon.Utils;
import org.noear.solon.boot.ServerConstants;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.prop.impl.WebSocketServerProps;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Signal;
import org.noear.solon.core.SignalSim;
import org.noear.solon.core.SignalType;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.ClassUtil;

public class PluginImpl {
    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }

    private KtHttpServer _server;

    public void start(AppContext context) {
        if (context.app().enableHttp() == false) {
            return;
        }

        //如果有 jetty 插件，就不启动了
        if (ClassUtil.loadClass("org.noear.solon.boot.jetty.XPluginImp") != null) {
            return;
        }

        context.lifecycle(ServerConstants.SIGNAL_LIFECYCLE_INDEX, () -> {
            start0(context);
        });
    }

    private static final String SMARTHTTP_LOG_LEVEL = "smarthttp.log.level";

    private void start0(AppContext context) throws Throwable {
        if(Utils.isEmpty(System.getProperty(SMARTHTTP_LOG_LEVEL))) {
            System.setProperty(SMARTHTTP_LOG_LEVEL, "WARNING");
        }

        //初始化属性
        ServerProps.init();
        MultipartUtil.init();

        HttpServerProps props = HttpServerProps.getInstance();
        final String _host = props.getHost();
        final int _port = props.getPort();
        final String _name = props.getName();

        _server = new KtHttpServer();
        _server.enableWebSocket(context.app().enableWebSocket());
        _server.setCoreThreads(props.getCoreThreads());

        if (props.isIoBound()) {
            //如果是io密集型的，加二段线程池
            _server.setWorkExecutor(props.newWorkExecutor("ktsmarthttp-"));
        }

        // TODO 添加自定义处理函数
        _server.setHandler(context.app()::tryHandle);

        //尝试事件扩展
        EventBus.publish(_server);
        _server.start(_host, _port);


        final String _wrapHost = props.getWrapHost();
        final int _wrapPort = props.getWrapPort();
        _signal = new SignalSim(_name, _wrapHost, _wrapPort, "http", SignalType.HTTP);
        context.app().signalAdd(_signal);

        if (context.app().enableWebSocket()) {
            //有名字定义时，添加信号注册
            WebSocketServerProps wsProps = WebSocketServerProps.getInstance();
            if (Utils.isNotEmpty(wsProps.getName())) {
                SignalSim wsSignal = new SignalSim(wsProps.getName(), _wrapHost, _wrapPort, "ws", SignalType.WEBSOCKET);
                context.app().signalAdd(wsSignal);
            }
        }

    }

    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;
        }
    }
}
