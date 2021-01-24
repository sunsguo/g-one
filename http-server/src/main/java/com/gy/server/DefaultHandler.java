package com.gy.server;

public class DefaultHandler implements RequestHandler {

    @Override
    public void handle(Request request, Response response) {
        response.write(request.getPath());
    }

}
