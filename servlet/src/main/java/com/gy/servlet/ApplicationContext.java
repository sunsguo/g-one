package com.gy.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 每个 web 应用一个上下文
 */
public class ApplicationContext {

    /**
     * 必须以斜线开头，不是斜线结尾
     */
    private String contextPath;

    public ApplicationContext(String contextPath) {
        this.contextPath = contextPath;
    }

    private Map<String, HttpServlet> servletMappings = new HashMap<>();

    private Map<String, HttpSession> sessionMap = new HashMap<>();

    public HttpSession getSession(String javaSessionId) {
        return sessionMap.get(javaSessionId);
    }

    public String getContextPath() {
        return contextPath;
    }

    public void addSession(String sessionId, HttpSession session) {
        sessionMap.put(sessionId, session);
    }

    public void addServlet(String s, HttpServlet helloServlet) {
        servletMappings.put(s, helloServlet);
    }

    public HttpServlet findServlet(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(contextPath.length());

        return servletMappings.get(uri);
    }

    public void initServlet() {
        Set<HttpServlet> servlets = new HashSet<>(this.servletMappings.values());

        servlets.forEach(it -> {
            try {
                it.init(new ServletConfigImpl());
            } catch (ServletException e) {
                e.printStackTrace();
            }
        });
    }

    public HttpServlet findServlet(String path) {
        return servletMappings.get(path);
    }
}
