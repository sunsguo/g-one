package com.gy.servlet;


import com.gy.core.util.StringUtil;
import com.gy.server.Headers;
import com.gy.server.Request;
import com.gy.server.Response;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Proxy;
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

    private HttpServletResponse servletResponse;

    private Cookie[] cookies;

    private HashMap<String, Object> attrs = new HashMap<>();

    public HttpServletRequestImpl(Request request, HttpServletResponse servletResponse) {
        this.request = request;
        this.servletResponse = servletResponse;

        init();
    }

    private void init() {
        initCookie();
    }

    private void initCookie() {
        String cookieStr = request.getHeader("Cookie");

        if (StringUtil.isEmpty(cookieStr)) {
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
        return getRequestURI();
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextPath() {
        ApplicationContext context = container.findContext(this);

        if (context == null)
            return "";
        return context.getContextPath();
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
        return null;
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
        return getRequestURI().replace(getContextPath(), "");
    }

    @Override
    public HttpSession getSession(boolean b) {
        String path = request.getPath();
        int index = path.indexOf("/", 1);

        String contentPath = "";

        if (index > 0) contentPath = path.substring(0, index);


        ApplicationContext context = container.findContext(contentPath);

        if (context == null) throw new RuntimeException("not found");

        HttpSession session = context.getSession(getRequestedSessionId());

        if (session == null && b) {
            session = new HttpSessionImpl();

            String sessionId = UUID.randomUUID().toString();
            context.addSession(sessionId, session);

            Cookie cookie = new Cookie(Constants.SESSION_ID, sessionId);
            servletResponse.addCookie(cookie);
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
        return attrs.get(s);
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
        return request.getParameter(s);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return request.getParameter(s).split(",");
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
        attrs.put(s, o);
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

    /**
     * forward 和 include 调用相对于用户时透明的
     *
     * @param s
     * @return
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String s) {

        return new RequestDispatcher() {

            /**
             * request -> servlet1 -> forward -> servlet2 -> response2 -> user
             *
             * servlet1 的 response 不会显示给用户
             *
             * @param request
             * @param response
             * @throws ServletException
             * @throws IOException
             */
            @Override
            public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {

                HttpServletRequest newProxyRequest = (HttpServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                        new Class[]{HttpServletRequest.class}, (proxy, method, args) -> {
                            String methodName = method.getName();
                            if (methodName.equals("getRequestURI") || methodName.equals("getPathInfo")) {
                                return getContextPath() + s;
                            }

                            if (methodName.equals("getServletPath")) {
                                return s;
                            }

                            return method.invoke(request, args);
                        });

                HttpServlet servlet = container.findServlet(newProxyRequest);

                servlet.service(newProxyRequest, response);
            }

            /**
             * request -> servlet1 -> forward -> servlet2 -> response2 -> response1 -> user
             *
             * servlet2 的 response 包含在（正在发送给客户端的）servlet1 的 response 包中。
             *
             * @param request
             * @param response
             * @throws ServletException
             * @throws IOException
             */
            @Override
            public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
                throw new IllegalStateException("没有实现");
            }

        };
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

    public void setContainer(Container container) {
        this.container = container;
    }
}
