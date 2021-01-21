package com.gy.core.util;

/**
 * 数组工具类
 */
public abstract class ArrayUtil {

    /**
     * 从 buffer 中查找目标字节数组
     *
     * @param buffer 源数组
     * @param start  开始查找位置
     * @param end    结束查找位置
     * @param target 目标数组
     * @return
     */
    public static int indexOf(byte[] buffer, int start, int end, byte[] target) {
        if (buffer == null) return -2;
        if (target == null || target.length == 0) return 0;

        outer:
        for (int i = start; i < end; i++) {
            if (buffer[i] != target[0]) continue;

            for (int j = 1; j < target.length; j++) {
                int bIndex = i + j;

                if (bIndex == end) return -i;

                if (buffer[bIndex] != target[j]) continue outer;
            }

            return i;
        }

        return Integer.MIN_VALUE;
    }

}
