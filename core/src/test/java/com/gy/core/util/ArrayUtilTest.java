package com.gy.core.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayUtilTest {

    @Test
    void indexOf() {
        byte[] buffer = new byte[]{1, 2, 3, 4};
        byte[] target = new byte[]{1, 2};

        assertEquals(0, ArrayUtil.indexOf(buffer, 0, buffer.length, target));
    }


    @Test
    void indexOf1() {
        byte[] buffer = new byte[]{1, 2, 3, 4};
        byte[] target = new byte[]{2};

        assertEquals(1, ArrayUtil.indexOf(buffer, 0, buffer.length, target));
    }

    @Test
    void indexOf2() {
        byte[] buffer = new byte[]{1, 2, 3, 4};
        byte[] target = new byte[]{2, 1};

        assertEquals(Integer.MIN_VALUE, ArrayUtil.indexOf(buffer, 0, buffer.length, target));
    }

    @Test
    void indexOf3() {
        byte[] buffer = new byte[]{1, 2, 3, 4};
        byte[] target = new byte[]{2, 3, 4};

        assertEquals(1, ArrayUtil.indexOf(buffer, 0, buffer.length, target));
    }

    @Test
    void indexOf4() {
        byte[] buffer = "hello world\r\n".getBytes(StandardCharsets.UTF_8);
        byte[] target = "\r\n".getBytes(StandardCharsets.UTF_8);

        int index = ArrayUtil.indexOf(buffer, 0, buffer.length, target);
        assertEquals(11, index);

        assertEquals("hello world", new String(buffer, 0, index));
    }

    @Test
    void indexOf5() {
        byte[] buffer = "hello world\r\n--WebAppBoundary--\r\n".getBytes(StandardCharsets.UTF_8);
        byte[] target = "\r\n--WebAppBoundary".getBytes(StandardCharsets.UTF_8);

        int index = ArrayUtil.indexOf(buffer, 0, buffer.length, target);
        assertEquals(11, index);

        assertEquals("hello world", new String(buffer, 0, index));
    }

    @Test
    void indexOf6() {
        byte[] buffer = "hello world\r\n--WebAppBound".getBytes(StandardCharsets.UTF_8);
        byte[] target = "\r\n--WebAppBoundary".getBytes(StandardCharsets.UTF_8);

        int index = ArrayUtil.indexOf(buffer, 0, buffer.length, target);
        assertEquals(-11, index);
    }


}