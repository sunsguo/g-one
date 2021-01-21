package com.gy.server;

class HttpServerTest {

    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server.start();
    }

}