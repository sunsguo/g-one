package com.gy.server;

import com.gy.core.util.LogFactory;
import com.gy.core.util.StreamUtil;
import com.gy.server.exception.BadRequest;

import javax.servlet.RequestDispatcher;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class HttpServer {

    Logger log = LogFactory.getLogger();

    private final int port;

    private volatile boolean stop = false;

    public HttpServer(int port) {
        this.port = port;
    }

    private RequestHandler handler;

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);
            log.info(String.format("server start on %s", port));

            while (!stop) {
                Socket client = serverSocket.accept();

                log.info("new client connected");

                new Thread(() -> process(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(Socket client) {
        try {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            // 解析 http request
            InputBuffer inputBuffer = new InputBuffer(inputStream);

            while (true) {
                try {
                    Request request = inputBuffer.getRequest();
                    Response response = new Response(outputStream);

                    log.info(request.getHeaders().toString());

                    RequestHandler handler = getHandler();
                    handler.handle(request, response);

                    response.flush();
                } catch (BadRequest e) {
                    log.warning("bad request: " + e.getMsg());
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private RequestHandler getHandler() {
        if (handler == null) {
            handler = new DefaultHandler();
        }

        return handler;
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }

}
