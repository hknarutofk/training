package com.example.demo.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author yeqiang
 * @since 6/18/20 4:28 PM
 */
public class ByteBufferUtil {
    public static byte[] int2ByteArray(int i) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.SIZE / 8);
        // C/C++内存中整形都是小端存储，ByteBuffer默认大端存储，认为指定
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(i);
        return byteBuffer.array();
    }

    public static byte[] long2ByteArray(long l) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.SIZE / 8);
        // C/C++内存中整形都是小端存储，ByteBuffer默认大端存储，认为指定
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putLong(l);
        return byteBuffer.array();
    }

    public static int byteArray2Int(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getInt(0);
    }

    public static long byteArray2Long(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getLong(0);
    }
}
