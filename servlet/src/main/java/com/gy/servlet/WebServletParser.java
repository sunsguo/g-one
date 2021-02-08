package com.gy.servlet;

import com.gy.core.util.ClassScanner;
import com.gy.core.util.ReflectUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;

/**
 * 如果一开始就解析所有的类，此时 servlet 容器都没有初始化，怎么搞？
 */
public class WebServletParser implements ClassScanner.ClassParser {

    @Override
    public boolean support(Class<?> cls) {
        return cls.isAnnotationPresent(WebServlet.class);
    }

    @Override
    public void parse(Class<?> cls) {
        WebServlet webServlet = cls.getAnnotation(WebServlet.class);

        String[] paths = webServlet.value();
        HttpServlet servlet = (HttpServlet) ReflectUtil.instance(cls);

        Arrays.stream(paths).forEach(path -> {
            // applicationContext.addServlet(path, servlet);
            // todo 带实现
        });
    }

}
