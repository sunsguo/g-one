package com.gy.server;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求头
 */
public class Headers {

    private List<Header> headers = new ArrayList<Header>(8);

    public String get(String name) {
        for (Header header : headers) {
            if (header.name.equalsIgnoreCase(name)) return header.value;
        }

        return null;
    }

    public void add(String name, String value) {
        Header header = new Header(name, value);
        headers.add(header);
    }

    public void add(String line) {
        if (line == null) return;

        String[] split = line.split(":");

        add(split[0], split.length > 1 ? split[1].trim() : "");
    }


    /**
     * 单行请求头
     */
    static class Header {

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        private String name;

        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
