package com.gy.servlet;

import com.gy.server.Request;
import com.gy.server.RequestHandler;
import com.gy.server.Response;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHandler  implements RequestHandler {

    Container container;

    public ServletHandler(Container container) {
        this.container = container;
    }

    @Override
    public void handle(Request request, Response response) {
        HttpServletRequest servletRequest = new HttpServletRequestImpl(request);
        HttpServletResponse servletResponse = new HttpServletResponseImpl(response);

        HttpServlet servlet = container.findServlet(servletRequest);
    }

}
