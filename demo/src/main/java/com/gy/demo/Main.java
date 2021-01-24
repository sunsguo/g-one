package com.gy.demo;

import com.gy.server.HttpServer;

public class Main {

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.start();
    }

}
