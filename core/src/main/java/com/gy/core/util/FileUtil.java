package com.gy.core.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 文件工具类
 */
public abstract class FileUtil {

    /**
     * 遍历所有文件
     *
     * @param file     需要遍历的文件
     * @param consumer 每个文件的处理器
     */
    public static void walkFile(String file, Consumer<File> consumer) {
        walkFile(new File(file), consumer);
    }

    /**
     * 遍历所有文件
     *
     * @param file     需要遍历的文件
     * @param consumer 每个文件的处理器
     */
    public static void walkFile(File file, Consumer<File> consumer) {
        walkFile(file, consumer, it -> true);
    }

    /**
     * 遍历所有文件 可以添加文件过滤器
     *
     * @param file     需要遍历的文件
     * @param consumer 每个文件的处理器
     */
    public static void walkFile(String file, Consumer<File> consumer, FileFilter filter) {
        walkFile(new File(file), consumer, filter);
    }

    public static void walkFile(File file, Consumer<File> consumer, FileFilter filter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles(filter);
            if (files != null)
                Arrays.stream(files).forEach(it -> walkFile(it, consumer));
        } else {
            consumer.accept(file);
        }
    }

    /**
     * 遍历 class 文件
     *
     * @param file     需要遍历的文件
     * @param consumer 每个文件的处理器
     */
    public static void walkClassFile(String file, Consumer<Class<?>> consumer) {
        ClassLoader classLoader = FileUtil.class.getClassLoader();

        if (StringUtil.isEmpty(file))
            file = classLoader.getResource("").getPath();

        final String basePath = file;

        walkFile(file, it -> {
            String classPath = it.getPath();
            String classFullName = classPath.substring(basePath.length() - 1, classPath.lastIndexOf("."))
                    .replace(File.separator, ".");
            try {
                Class<?> cls = classLoader.loadClass(classFullName);
                consumer.accept(cls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }, it -> it.isDirectory() || it.getName().endsWith(".class"));
    }

    /**
     * 遍历 class 文件
     *
     * @param cls      cls 类的同包及子包
     * @param consumer 每个文件的处理器
     */
    public static void walkClassFile(Class<?> cls, Consumer<Class<?>> consumer) {
        String classPath = cls.getResource("/").getPath();
        walkClassFile(classPath, consumer);
    }

}
