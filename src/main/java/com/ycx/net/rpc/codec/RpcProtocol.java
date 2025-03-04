package com.ycx.net.rpc.codec;

import com.ycx.net.rpc.serializer.Serializer;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.extension.decoder.FixedLengthFrameDecoder;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcProtocol<T> implements Protocol<T> {

    private final Serializer serializer;

    private final Class<T> klass;

    private final Map<AioSession, FixedLengthFrameDecoder> decoderMap = new ConcurrentHashMap<>();

    private long lastClearTime = System.currentTimeMillis();

    public RpcProtocol(Serializer serializer, Class<T> klass) {
        this.serializer = serializer;
        this.klass = klass;
    }

    @Override
    public T decode(ByteBuffer readBuffer, AioSession session) {
        if (System.currentTimeMillis() - lastClearTime > 5000) {
            decoderMap.keySet().stream().filter(AioSession::isInvalid).forEach(decoderMap::remove);
        }
        FixedLengthFrameDecoder decoder = decoderMap.get(session);
        // 已有数据写入
        if (decoder != null) {
            T data = decode0(readBuffer, decoder);
            if (data != null) {
                decoderMap.remove(session);
            }
            return data;
        }

        int remaining = readBuffer.remaining();
        if (remaining < Integer.BYTES) {
            // 小于 长度 字节数
            return null;
        }

        readBuffer.mark();
        int length = readBuffer.getInt();
        if (length + Integer.BYTES > readBuffer.capacity()) {
            FixedLengthFrameDecoder newDecoder = new FixedLengthFrameDecoder(length);
            decoderMap.put(session, newDecoder);
            return null;
        }

        if (length > readBuffer.remaining()) {
            readBuffer.reset();
            return null;
        }

        return convert(readBuffer, length);
    }

    private T decode0(ByteBuffer readBuffer, FixedLengthFrameDecoder decoder) {
        if (!decoder.decode(readBuffer)) {
            return null;
        }
        ByteBuffer buffer = decoder.getBuffer();
        return convert(buffer, buffer.capacity());
    }

    private T convert(ByteBuffer buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        try {
            return serializer.deserialize(bytes, klass);
        } catch (Exception e) {
            // TODO 异常处理
        }
        return null;
    }
}
