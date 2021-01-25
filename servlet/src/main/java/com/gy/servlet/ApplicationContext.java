package com.gy.servlet;

import javax.servlet.Servlet;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个 web 应用一个上下文
 */
public class ApplicationContext {

    private String contextPath;

    public ApplicationContext(String contextPath) {
        this.contextPath = contextPath;
    }

    private Map<String, Servlet> servletMappings = new HashMap<>();

    private Map<String, HttpSession> sessionMap = new HashMap<>();

    public HttpSession getSession(String javaSessionId) {
        return sessionMap.get(javaSessionId);
    }

}
