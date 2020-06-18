package com.example.demo.util;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import sun.misc.HexDumpEncoder;

/**
 * @author yeqiang
 * @since 6/18/20 4:39 PM
 */
@Slf4j
class ByteBufferUtilTest {

    @Test
    public void testConvert() {
        int a = 1;
        long b = 1;
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();

        byte[] buffer = ByteBufferUtil.int2ByteArray(a);
        log.debug(hexDumpEncoder.encode(buffer));
        a = ByteBufferUtil.byteArray2Int(buffer);
        log.debug("{}", a);
        buffer = ByteBufferUtil.long2ByteArray(b);
        log.debug(hexDumpEncoder.encode(buffer));
        b = ByteBufferUtil.byteArray2Long(buffer);
        log.debug("{}", b);
    }
}