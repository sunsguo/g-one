package com.gy.servlet;

import com.gy.core.util.ArrayUtil;
import com.gy.server.HttpStatus;
import com.gy.server.Response;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class HttpServletResponseImpl implements HttpServletResponse {

    private Response response;

    private List<Cookie> cookies = new ArrayList<>();

    public HttpServletResponseImpl(Response response) {
        this.response = response;
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);

        response.setHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue());
    }

    @Override
    public boolean containsHeader(String name) {
        return response.getHeader(name) != null;
    }

    @Override
    public String encodeURL(String url) {
        throw new UnsupportedOperationException();

    }

    @Override
    public String encodeRedirectURL(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        response.sendError(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
        response.sendError(sc);
    }

    /**
     * 重定向是浏览器行为，在收到状态码为302的响应时，自动重发请求到 Location 响应头
     * 此时浏览器地址栏的 url 会发生改变
     *
     * @param location
     * @throws IOException
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        response.sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        response.setHeader(name, date + "");
    }

    @Override
    public void addDateHeader(String name, long date) {
        response.addHeader(name, date + "");
    }

    @Override
    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        response.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        setHeader(name, value + "");
    }

    @Override
    public void addIntHeader(String name, int value) {
        addHeader(name, value + "");
    }

    @Override
    public void setStatus(int sc) {
        response.setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        response.setStatus(sc, sm);
    }

    @Override
    public int getStatus() {
        return response.getStatus();
    }

    @Override
    public String getHeader(String name) {
        return response.getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        Enumeration<String> headers = response.getHeaders().getHeaders(name);

        return ArrayUtil.enumerationToCollection(headers);
    }

    @Override
    public Collection<String> getHeaderNames() {
        Enumeration<String> headerNames = response.getHeaders().getHeaderNames();
        return ArrayUtil.enumerationToCollection(headerNames);
    }

    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    @Override
    public String getContentType() {
        return response.getHeader("Content-Type");
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        OutputStream outputStream = response.getOutputStream();

        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterEncoding(String charset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentLength(int len) {
        setIntHeader("Content-Length", len);
    }

    @Override
    public void setContentLengthLong(long len) {
        setIntHeader("Content-Length", Long.valueOf(len).intValue());
    }

    @Override
    public void setContentType(String type) {
        setHeader("Content-Type", type);
    }

    @Override
    public void setBufferSize(int size) {
        response.setBufferSize(size);
    }

    @Override
    public int getBufferSize() {
        return response.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
        response.flush();
    }

    @Override
    public void resetBuffer() {
        response.resetBuffer();
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        response.resetBuffer();
        setStatus(HttpStatus.OK.value());
    }

    @Override
    public void setLocale(Locale loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }
}
