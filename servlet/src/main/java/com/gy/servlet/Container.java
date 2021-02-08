package com.gy.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * servlet 容器 作为顶级组件
 */
public class Container {

    /**
     * 一个容器有多个应用
     */
    private List<ApplicationContext> contexts = new ArrayList<>();

    /**
     * 启动一个容器
     */
    public void start() {
    }

    /**
     * 添加一个应用
     *
     * @param context
     */
    public void addContext(ApplicationContext context) {
        this.contexts.add(context);
    }

    public ApplicationContext findContext(String contextPath) {
        for (ApplicationContext context : contexts) {
            if (context.getContextPath().equalsIgnoreCase(contextPath)) {
                return context;
            }
        }

        return null;
    }

    public ApplicationContext findContext(HttpServletRequest request) {
        String uri = request.getRequestURI();

        for (ApplicationContext context : contexts) {
            if (uri.startsWith(context.getContextPath())) {
                return context;
            }
        }

        return null;
    }

    public HttpServlet findServlet(HttpServletRequest request) {
        String uri = request.getRequestURI();

        ApplicationContext applicationContext = null;

        for (ApplicationContext context : contexts) {
            if (uri.startsWith(context.getContextPath())) {
                applicationContext = context;
                break;
            }
        }

        if (applicationContext == null) return null;

        HttpServlet servlet = applicationContext.findServlet(request);

        if (servlet == null) {
            return findServlet(request.getContextPath(), "/");
        }

        return servlet;
    }

    public HttpServlet findServlet(String contextPath, String path) {

        ApplicationContext context = findContext(contextPath);

        if (context == null) return null;

        return context.findServlet(path);
    }

}
