package com.gy.core.util;

import java.lang.reflect.Field;

/**
 * 反射工具类
 */
public abstract class ReflectUtil {

    /**
     * 反射获取 public 字段，
     *
     * @param root class
     * @param name 字段名
     * @return 字段类型
     */
    public static Field getField(Class<?> root, String name) {
        try {
            return root.getField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射获取字段，循环向上找
     *
     * @param root class
     * @param name 字段名
     * @return 字段类型
     */
    public static Field getDeclaredField(Class<?> root, String name) {
        Class<?> cls = root;

        while (cls != Object.class) {
            try {
                return cls.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }

        return null;
    }

    public static <T> T instance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
