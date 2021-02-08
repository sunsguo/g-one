package com.gy.demo;

import com.gy.server.HttpServer;
import com.gy.servlet.ApplicationContext;
import com.gy.servlet.Container;
import com.gy.servlet.ServletHandler;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

    public static void main(String[] args) {

       /* XmlWebApplicationContext xmlWebApplicationContext = new XmlWebApplicationContext();

        BeanDefinitionReader reader = new XmlBeanDefinitionReader();

        reader.*/


        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        Container container = new Container();
        com.gy.servlet.ApplicationContext applicationContext = new ApplicationContext("/demo");
        container.addContext(applicationContext);

        applicationContext.addServlet("/", dispatcherServlet);

        applicationContext.initServlet();

        HttpServer httpServer = new HttpServer(8080);
        httpServer.setHandler(new ServletHandler(container));
        httpServer.start();
    }

}
