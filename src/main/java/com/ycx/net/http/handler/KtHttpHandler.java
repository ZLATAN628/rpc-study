package com.ycx.net.http.handler;

import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.smarthttp.XPluginImp;
import org.noear.solon.core.handle.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.impl.Request;
import org.smartboot.socket.util.AttachKey;
import org.smartboot.socket.util.Attachment;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class KtHttpHandler extends HttpServerHandler {
    static final Logger log = LoggerFactory.getLogger(KtHttpHandler.class);
    static final AttachKey<KtHttpContext> httpHolderKey = AttachKey.valueOf("httpHolder");

    protected Executor executor;
    private final Handler handler;

    public KtHttpHandler(Handler handler) {
        this.handler = handler;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onClose(Request request) {
        if (request.getAttachment() == null) {
            return;
        }

        KtHttpContext ctx = request.getAttachment().get(httpHolderKey);
        if (ctx != null && ctx.asyncStarted()) {
            try {
                try {
                    ctx.asyncState.onComplete(ctx);
                } catch (Throwable e) {
                    log.warn(e.getMessage(), e);
                }
            } finally {
                request.getAttachment().remove(httpHolderKey);
            }
        }
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, CompletableFuture<Object> future) throws IOException {
        KtHttpContext ctx = new KtHttpContext(request, response, future);
        if (request.getAttachment() == null) {
            request.setAttachment(new Attachment());
        }
        request.getAttachment().put(httpHolderKey, ctx);

        try {
            if (executor == null) {
                handle0(ctx, future);
            } else {
                try {
                    executor.execute(() -> {
                        handle0(ctx, future);
                    });
                } catch (RejectedExecutionException e) {
                    handle0(ctx, future);
                }
            }
        } finally {
            if (ctx.asyncStarted() == false) {
                request.getAttachment().remove(httpHolderKey);
            }
        }
    }

    protected void handle0(KtHttpContext ctx, CompletableFuture<Object> future) {
        try {
            handleDo(ctx);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (ctx.asyncStarted() == false) {
                future.complete(ctx);
            }
        }
    }

    protected void handleDo(KtHttpContext ctx) {
        try {
            if ("PRI".equals(ctx.method())) {
                ctx.innerGetResponse().setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
                return;
            }

            ctx.contentType("text/plain;charset=UTF-8");
            if (ServerProps.output_meta) {
                ctx.headerSet("Solon-Boot", XPluginImp.solon_boot_ver());
            }

            handler.handle(ctx);

            if (ctx.asyncStarted() == false) {
                ctx.innerCommit();
            }

        } catch (Throwable e) {
            log.warn(e.getMessage(), e);

            ctx.innerGetResponse().setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
