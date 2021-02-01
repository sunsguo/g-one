package com.gy.core.util;

import org.junit.jupiter.api.Test;

class FileUtilTest {

    @Test
    void walkClassFile() {
        FileUtil.walkClassFile(FileUtilTest.class, System.out::println);
    }

}