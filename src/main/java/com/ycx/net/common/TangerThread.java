package com.ycx.net.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TangerThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(TangerThread.class);

    UncaughtExceptionHandler exceptionHandler = (t, e) -> {
        handleException(t.getName(), e);
    };

    public TangerThread(String name) {
        super(name);
        setUncaughtExceptionHandler(exceptionHandler);
    }

    public TangerThread(Runnable task, String name) {
        super(task, name);
        setUncaughtExceptionHandler(exceptionHandler);
    }

    protected void handleException(String threadName, Throwable e) {
        log.warn("Exception occurred from thread {}", threadName, e);
    }
}
