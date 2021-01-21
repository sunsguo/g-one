package com.gy.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParametersTest {

    Parameters parameters;

    String query = "name=gy1&age=21";

    String body = "sex=男&birthday=1995-09-17&name=gy2&id=";

    @BeforeEach
    public void init() {
        parameters = new Parameters(query, body);
    }

    @Test
    void getParameter() {
        String name = parameters.getParameter("name");
        assertEquals("gy1,gy2", name);

        String id = parameters.getParameter("id");
        assertEquals("", id);
    }

    @Test
    void test() {
        String str = "GET  /hello  HTTP/1.1";
        String[] s = str.split(" +");

        System.out.println(Arrays.toString(s));
    }

}