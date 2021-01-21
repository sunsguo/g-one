package com.gy.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述一个请求，内部使用
 */
public class Request {

    /**
     * 请求协议
     */
    private String protocol = "HTTP/1.1";

    /**
     * 请求方式
     */
    private String method = "GET";

    /**
     * 请求路径
     */
    private String path = "";

    private String host;

    private int port;

    /**
     * 原生路径参数
     */
    private final StringBuilder query = new StringBuilder();

    /**
     * 原生请求体
     */
    private final StringBuilder body = new StringBuilder();

    private final Headers headers = new Headers();

    private final Parameters parameters;

    public Request() {
        parameters = new Parameters(this);
    }

    /**
     * 获取请求参数
     *
     * @param name 参数名
     * @return
     */
    public String getParameter(String name) {
        return parameters.getParameter(name);
    }

    public Map<String, String> getParameters() {
        return parameters.getParameters();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void addHeader(String name, String value) {
        headers.add(name, value);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query.toString();
    }

    public String getBody() {
        return body.toString();
    }

    public Headers getHeaders() {
        return headers;
    }

    public Request setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public Request setPath(String path) {
        this.path = path;
        return this;
    }

    public Request setQuery(String query) {
        this.query.append(query);
        return this;
    }

    public Request setBody(String body) {
        this.body.append(body);
        return this;
    }

    public Request setHost(String host) {
        this.host = host;
        return this;
    }

    public Request setPort(int port) {
        this.port = port;
        return this;
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    public Parameters parameters() {
        return parameters;
    }
}
