package com.example.demo.util;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import sun.misc.HexDumpEncoder;

/**
 * @author yeqiang
 * @since 6/18/20 4:39 PM
 */
@Slf4j
class MemoryUtilTest {
    HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();

    @Test
    public void testConvert() {
        int a = 1;
        long b = 1;

        byte[] buffer = MemoryUtil.int2ByteArray(a);
        log.debug(hexDumpEncoder.encode(buffer));
        a = MemoryUtil.byteArray2Int(buffer);
        log.debug("{}", a);
        buffer = MemoryUtil.long2ByteArray(b);
        log.debug(hexDumpEncoder.encode(buffer));
        b = MemoryUtil.byteArray2Long(buffer);
        log.debug("{}", b);
    }

    @Test
    public void testbyteArray2HexString() {
        byte[] buffer = new byte[1024];
        for (int i = 0; i < 1024; i++) {
            buffer[i] = (byte)i;
        }
        log.debug(hexDumpEncoder.encode(buffer));
        log.debug(MemoryUtil.byteArray2HexString(buffer));
    }
}