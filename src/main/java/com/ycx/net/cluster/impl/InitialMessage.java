package com.ycx.net.cluster.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static com.ycx.net.cluster.impl.CnxManager.maxBuffer;

public class InitialMessage {
    private static final long KT_MAGIC = 7739838825226137191L;
    private int nodeId;
    private InetSocketAddress address;

    public InitialMessage(int nodeId, InetSocketAddress address) {
        this.nodeId = nodeId;
        this.address = address;
    }

    public static void writeMsg(DataOutputStream dout, int nodeId, InetSocketAddress listenAddress) throws IOException {
        dout.writeLong(KT_MAGIC);
        dout.writeInt(nodeId);
        String addr = formatInetAddr(listenAddress);
        byte[] addrBytes = addr.getBytes();
        dout.writeInt(addrBytes.length);
        dout.write(addrBytes);
        dout.flush();
    }

    public static class InitialMessageException extends Exception {
        InitialMessageException(String message, Object... args) {
            super(String.format(message, args));
        }
    }

    static public InitialMessage parse(DataInputStream din)
            throws InitialMessageException, IOException {
        int nodeId;

        long magic = din.readLong();

        if (magic != KT_MAGIC) {
            throw new InitialMessageException(
                    "Got UnKnown Magic Number: %d", magic);
        }

        nodeId = din.readInt();

        int remaining = din.readInt();
        if (remaining <= 0 || remaining > maxBuffer) {
            throw new InitialMessageException(
                    "Unreasonable buffer length: %s", remaining);
        }

        byte[] b = new byte[remaining];
        int num_read = din.read(b);

        if (num_read != remaining) {
            throw new InitialMessageException(
                    "Read only %s bytes out of %s sent by server %s",
                    num_read, remaining, nodeId);
        }

        String addr = new String(b);
        String[] host_port = addr.split(":");
        if (host_port.length != 2) {
            throw new InitialMessageException("Badly formed address: %s", addr);
        }

        int port;
        try {
            port = Integer.parseInt(host_port[1]);
        } catch (NumberFormatException e) {
            throw new InitialMessageException("Bad port number: %s", host_port[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InitialMessageException("No port number in: %s", addr);
        }

        return new InitialMessage(nodeId, isWildcardAddress(host_port[0]) ? null :
                new InetSocketAddress(host_port[0], port));
    }

    public static boolean isWildcardAddress(final String hostname) {
        try {
            return InetAddress.getByName(hostname).isAnyLocalAddress();
        } catch (UnknownHostException e) {
            // if we can not resolve, it can not be a wildcard address
            return false;
        } catch (SecurityException e) {
//            LOG.warn("SecurityException in getByName() for" + hostname);
            return false;
        }
    }

    public static String formatInetAddr(InetSocketAddress addr) {
        InetAddress ia = addr.getAddress();

        if (ia == null) {
            return String.format("%s:%s", addr.getHostString(), addr.getPort());
        }

        if (ia instanceof Inet6Address) {
            return String.format("[%s]:%s", ia.getHostAddress(), addr.getPort());
        } else {
            return String.format("%s:%s", ia.getHostAddress(), addr.getPort());
        }
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

}
