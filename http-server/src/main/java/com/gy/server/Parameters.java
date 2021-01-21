package com.gy.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gy.server.RequestUtil.isJson;

/**
 * 请求参数，用于 get 请求和 post 表单请求
 */
public class Parameters {

    private Request request;

    private String query;

    private String body;

    /**
     * 普通参数
     */
    private final Map<String, List<String>> parameters = new HashMap<>();

    /**
     * 文件上传
     */
    private final Map<String, List<File>> files = new HashMap<>();

    private boolean parsed = false;

    public Parameters(Request request) {
        this.request = request;
    }

    public Parameters(String query, String body) {
        this.query = query;
        this.body = body;
    }

    public String getParameter(String name) {
        if (!parsed) parse();

        List<String> list = parameters.get(name);

        if (list == null) return "";

        return String.join(",", list);
    }

    public Map<String, String> getParameters() {
        if (!parsed) parse();

        Map<String, String> m = new HashMap<>();
        this.parameters.keySet().forEach(k -> m.put(k, getParameter(k)));
        return m;
    }


    private void parse() {
        if (request != null) {
            this.query = request.getQuery();
            this.body = request.getBody();
        }

        doParse(this.query);

        if (request == null || !isJson(request)) doParse(this.body);

        parsed = true;
    }

    private void doParse(String str) {
        String[] fields = str.split("&");

        for (String filed : fields) {
            String[] split = filed.split("=");

            String name = split[0];
            String value = split.length > 1 ? split[1] : "";

            if (parameters.containsKey(name)) {
                parameters.get(name).add(value);
            } else {
                List<String> values = new ArrayList<>();
                values.add(value);
                parameters.put(name, values);
            }
        }
    }

    public void putArg(String name, File value) {
        putArg(name, value, this.files);
    }

    public void putArg(String name, String value) {
        putArg(name, value, this.parameters);
    }

    private <E> void putArg(String name, E value, Map<String, List<E>> map) {
        if (map.containsKey(name)) {
            map.get(name).add(value);
        } else {
            List<E> strings = new ArrayList<>();
            strings.add(value);
            map.put(name, strings);
        }
    }


}

