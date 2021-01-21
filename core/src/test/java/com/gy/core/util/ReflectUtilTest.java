package com.gy.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;



@SuppressWarnings("InnerClassMayBeStatic")
class ReflectUtilTest {

    class BaseClass {
        private String b1;

        protected String p1;
    }

    class SuperClass extends BaseClass {
        private String s1;
    }

    @Test
    void getDeclaredField1() {
        Field b1 = ReflectUtil.getDeclaredField(BaseClass.class, "b1");
        assertEquals(String.class, b1.getType());
    }

    @Test
    void getDeclaredField2() {
        Field b1 = ReflectUtil.getDeclaredField(SuperClass.class, "b1");
        assertEquals(String.class, b1.getType());
    }

    @Test
    void getDeclaredField3() {
        Field b1 = ReflectUtil.getField(SuperClass.class, "p1");
        assertNull(b1);
        b1 = ReflectUtil.getDeclaredField(SuperClass.class, "p1");
        assertEquals(String.class, b1.getType());
    }


}