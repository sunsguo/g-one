package com.gy.servlet;

import com.gy.server.HttpStatus;
import com.gy.server.Request;
import com.gy.server.RequestHandler;
import com.gy.server.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * http server 和 servlet 容器关联
 */
public class ServletHandler implements RequestHandler {

    Container container;

    public ServletHandler(Container container) {
        this.container = container;
    }

    @Override
    public void handle(Request request, Response response) {
        HttpServletResponse servletResponse = new HttpServletResponseImpl(response);
        HttpServletRequestImpl servletRequest = new HttpServletRequestImpl(request, servletResponse);
        servletRequest.setContainer(container);

        HttpServlet servlet = container.findServlet(servletRequest);

        try {
            if (servlet == null) {
                servletResponse.sendError(HttpStatus.NOT_FOUND.value());
                return;
            }

            // 没有 session 创建 session
            servletRequest.getSession();

            servlet.service(servletRequest, servletResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

}
