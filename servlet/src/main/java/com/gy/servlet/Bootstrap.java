package com.gy.servlet;

import com.gy.core.util.FileUtil;
import com.gy.core.util.ReflectUtil;
import com.gy.server.HttpServer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;

/**
 * 容器启动类
 */
public class Bootstrap {

    public static void main(String[] args) {
        new Bootstrap().start();
    }

    public void start() {
        Container container = new Container();

        ApplicationContext applicationContext = new ApplicationContext("/demo");

        container.addContext(applicationContext);

        HttpServer httpServer = new HttpServer(8080);
        httpServer.setHandler(new ServletHandler(container));

        FileUtil.walkClassFile("", cls -> {
            WebServlet webServlet = cls.getAnnotation(WebServlet.class);

            if (webServlet != null) {
                String[] paths = webServlet.value();
                HttpServlet servlet = (HttpServlet) ReflectUtil.instance(cls);

                Arrays.stream(paths).forEach(path -> {
                    applicationContext.addServlet(path, servlet);
                });
            }
        });


        httpServer.start();
    }

}
