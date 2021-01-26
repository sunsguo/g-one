package com.gy.servlet;


import com.gy.core.util.StringUtil;
import com.gy.server.Headers;
import com.gy.server.Request;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class HttpServletRequestImpl implements HttpServletRequest {

    /**
     * 容器
     */
    private Container container;

    /**
     * 原生 http 请求
     */
    private Request request;

    private Cookie[] cookies;

    public HttpServletRequestImpl(Request request) {
        this.request = request;

        init();
    }

    private void init() {
        initCookie();
    }

    private void initCookie() {
        String cookieStr = request.getHeader("Cookie");

        if (StringUtil.isEmpty(cookies)) {
            cookies = new Cookie[0];
            return;
        }

        String[] items = cookieStr.split(";");

        List<Cookie> cookieList = new ArrayList<>();

        for (String item : items) {
            String[] part = item.split("=");

            cookieList.add(new Cookie(part[0], part[1]));
        }

        cookies = cookieList.toArray(new Cookie[0]);
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    @Override
    public long getDateHeader(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHeader(String s) {
        return request.getHeader(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        Headers headers = request.getHeaders();
        return headers.getHeaders(s);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Headers headers = request.getHeaders();
        return headers.getHeaderNames();
    }

    @Override
    public int getIntHeader(String s) {
        return Integer.parseInt(request.getHeader(s));
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getPathInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQueryString() {
        return request.getQuery();
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUserInRole(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestedSessionId() {
        for (Cookie cookie : cookies) {
            if (Constants.SESSION_ID.equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public String getRequestURI() {
        return request.getPath();
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer sb = new StringBuffer();
        return sb.append("http://")
                .append(request.getHost())
                .append(":")
                .append(request.getPort())
                .append(request.getPath());
    }

    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException();

    }

    @Override
    public HttpSession getSession(boolean b) {
        String path = request.getPath();
        int index = path.indexOf("/", 1);

        String contentPath = "";

        if (index > 0) contentPath = path.substring(1, index);


        ApplicationContext context = container.findContext(contentPath);

        if (context == null) throw new RuntimeException("not found");

        HttpSession session = context.getSession(getRequestedSessionId());

        if (session == null && b) {
            session = new HttpSessionImpl();

            String sessionId = UUID.randomUUID().toString();
            context.addSession(sessionId, session);
        }

        return session;
    }

    /**
     * 获取 session 没有则创建一个
     *
     * @return
     */
    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
