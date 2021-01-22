package com.gy.server;

import com.gy.core.util.LogFactory;
import com.gy.core.util.StreamUtil;
import com.gy.server.exception.BadRequest;

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

                    String path = request.getPath();

                    // 响应最后不需要添加换行符 上传文件时最后是有换行符的
                    if (path.endsWith("ico")) {
                        File file = new File("E:\\Pictures\\Elementary OS background\\img0.jpg");
                        FileInputStream img = new FileInputStream(file);

                        outputStream.write(("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: " + file.length() + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                        StreamUtil.copy(img, outputStream);
                    } else {
                        outputStream.write(("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: " + path.getBytes(StandardCharsets.UTF_8).length + "\r\n\r\n" + path).getBytes(StandardCharsets.UTF_8));
                    }

                    outputStream.flush();
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


}
