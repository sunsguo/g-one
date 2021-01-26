package com.gy.servlet;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个 web 应用一个上下文
 */
public class ApplicationContext {

    /**
     * 必须以斜线开头
     */
    private String contextPath;

    public ApplicationContext(String contextPath) {
        this.contextPath = contextPath;
    }

    private Map<String, Servlet> servletMappings = new HashMap<>();

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

        return null;
    }
}
