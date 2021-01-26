package com.gy.server;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求头
 */
public class Headers {

    private List<Header> headers = new ArrayList<Header>(8);

    public String get(String name) {
        Header header = getHeader(name);

        if (header == null) return null;

        return header.getValue();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        this.headers.forEach(it -> {
            sb.append(it.getName()).append(": ").append(it.getValue()).append("\r\n");
        });

        return sb.toString();
    }

    public Enumeration<String> getHeaders(String s) {
        List<Header> list = this.headers.stream().filter(header -> s.equalsIgnoreCase(header.getName()))
                .collect(Collectors.toList());

        return new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < list.size();
            }

            @Override
            public String nextElement() {
                return list.get(index++).getValue();
            }
        };
    }

    public Enumeration<String> getHeaderNames() {
        return new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < headers.size();
            }

            @Override
            public String nextElement() {
                return headers.get(index++).getName();
            }
        };
    }

    public Header getHeader(String name) {
        for (Header header : headers) {
            if (header.name.equalsIgnoreCase(name)) return header;
        }

        return null;
    }

    public void set(String name, String value) {
        Header header = getHeader(name);
        if (header != null) {
            header.setValue(value);
            return;
        }

        add(name, value);
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
