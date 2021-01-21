package com.gy.server.exception;

public class BadRequest extends RuntimeException {

    private String msg;

    public BadRequest(String msg) {
        this.msg = msg;
    }

}
