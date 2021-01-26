package com.gy.demo;

import com.gy.demo.servlet.HelloServlet;
import com.gy.server.HttpServer;
import com.gy.servlet.ApplicationContext;
import com.gy.servlet.Container;
import com.gy.servlet.ServletHandler;

public class Main {

    public static void main(String[] args) {
        Container container = new Container();

        ApplicationContext applicationContext = new ApplicationContext("/demo");

        container.addContext(applicationContext);

        HelloServlet helloServlet = new HelloServlet();

        applicationContext.addServlet("/hello", helloServlet);

        HttpServer httpServer = new HttpServer(8080);
        httpServer.setHandler(new ServletHandler(container));

        httpServer.start();
    }

}
