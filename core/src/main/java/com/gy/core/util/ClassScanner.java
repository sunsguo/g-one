package com.gy.core.util;

import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    private String basePath;

    private List<Consumer> consumers = new ArrayList<>();


    public int scan() {


        return 0;
    }


    interface Consumer {

        boolean support(Class<?> cls);

        void deal(Class<?> cls);

    }
}
