package com.ycx.net.cluster.impl;

import com.ycx.net.common.TangerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class RecvWorker extends TangerThread {
    private static final Logger log = LoggerFactory.getLogger(RecvWorker.class);
    private final Socket socket;
    private final DataInputStream din;
    private final int nodeId;
    private final SendWorker sendWorker;
    private final CnxManager cnxManager;
    private volatile boolean running = true;

    public RecvWorker(Socket socket, DataInputStream din, int nodeId,
                      SendWorker sendWorker, CnxManager cnxManager) {
        super(String.format("RecvWorker-%d", nodeId));
        this.socket = socket;
        this.din = din;
        this.nodeId = nodeId;
        this.sendWorker = sendWorker;
        this.cnxManager = cnxManager;
    }

    public void finish() {
        if (!running) {
            return;
        }
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            while (running && socket != null) {
                int length = din.readInt();
                if (length < 0) {
                    throw new IOException("Received packet with invalid packet: " + length);
                }
                byte[] bytes = new byte[length];
                din.readFully(bytes, 0, length);
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                cnxManager.recvVote(nodeId, buffer.duplicate());
            }
        } catch (Exception e) {
            log.warn("thread {} exit for exception: ", this.getName(), e);
        } finally {
            sendWorker.finish();
        }
    }
}
