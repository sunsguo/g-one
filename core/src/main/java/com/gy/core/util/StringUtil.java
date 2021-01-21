package com.gy.core.util;

public abstract class StringUtil {

    /**
     * 将 byte[] 转换为字符串
     *
     * @param bytes 字节数组
     * @param from  开始位置(包含)
     * @param to    结束位置(不包含)
     * @return 字符串
     */
    public static String bytes2string(byte[] bytes, int from, int to) {
        return new String(bytes, from, to - from);
    }

    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(Object str) {
        if (str == null) return true;

        return "".equals(str);
    }

}
