package com.gy.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类扫描器
 */
public class ClassScanner {

    private String basePath;

    private Class<?> baseCls;

    public ClassScanner(String basePath) {
        this.basePath = basePath;
    }

    public ClassScanner(Class<?> baseCls) {
        this.baseCls = baseCls;
    }

    private String getBasePath() {
        if (StringUtil.isEmpty(basePath)) {
            basePath = baseCls.getResource("/").getPath();
        }

        return basePath;
    }

    private final List<ClassParser> classParsers = new ArrayList<>();

    public int scan() {
        AtomicInteger count = new AtomicInteger();

        FileUtil.walkClassFile(getBasePath(), cls -> {
            classParsers.forEach(classParser -> {
                if (classParser.support(cls))
                    classParser.parse(cls);
            });
            count.incrementAndGet();
        });

        return count.get();
    }


    public interface ClassParser {

        boolean support(Class<?> cls);

        void parse(Class<?> cls);

    }
}
