package com.gy.demo.controller;

import com.gy.demo.vo.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(String name) {
        return "hello world: " + name;
    }

    @RequestMapping("user")
    public User user(@RequestBody User user) {
        return user;
    }

    @RequestMapping("redirect")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://www.baidu.com");
    }

    @RequestMapping("forward")
    public void forward(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/hello");
        requestDispatcher.forward(request, response);
    }

}
