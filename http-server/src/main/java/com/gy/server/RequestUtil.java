package com.gy.server;

public abstract class RequestUtil {

    public static boolean isMultipart(Request request) {
        String contentType = request.getHeader("Content-Type");
        return isMultipart(contentType);
    }

    public static boolean isMultipart(String contentType) {
        return contentType != null && contentType.startsWith("multipart/form-data");
    }

    public static boolean isJson(Request request) {
        String contentType = request.getHeader("Content-Type");
        return isJson(contentType);
    }

    public static boolean isJson(String contentType) {
        return contentType != null && contentType.startsWith("application/json");
    }

}
