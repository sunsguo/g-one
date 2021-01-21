package com.gy.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SuppressWarnings({"InnerClassMayBeStatic", "ConstantConditions"})
public class GenericResolverTest {

    class A1 {
    }

    class A2 {
    }

    // refer K:     T6 -> Integer
    class M<T6> {

        public T6 t6;

    }

    // refer F:     T3 -> A1, T4 -> A2, T5 -> Integer
    class K<T3, T4, T5> extends M<T5> {
        public T3 t3;
        public T4 t4;
        public T5 t5;

        public void test(T3 t3, T4 t4, T5 t5) {
            System.out.println();
        }

    }

    // refer C:     T1 -> A1, T2 -> A2
    class F<T1, T2> extends K<T1, T2, Integer> {
        public T1 t1;
        public T2 t2;
    }

    class C extends F<A1, A2> {
    }

    class B extends C {
    }

    class D extends F<Integer, String> {
    }

    @Test
    void getGenericType1() {
        GenericResolver genericResolver = new GenericResolver(B.class);

        Field t1 = ReflectUtil.getField(B.class, "t1");
        assertEquals(A1.class, genericResolver.getGenericType(t1));

        Field t2 = ReflectUtil.getField(B.class, "t2");
        assertEquals(A2.class, genericResolver.getGenericType(t2));

        Field t6 = ReflectUtil.getField(B.class, "t6");
        assertEquals(Integer.class, genericResolver.getGenericType(t6));
    }

    @Test
    void getGenericType2() {
        GenericResolver genericResolver = new GenericResolver(D.class);

        Field t1 = ReflectUtil.getField(B.class, "t1");
        assertEquals(Integer.class, genericResolver.getGenericType(t1));

        Field t2 = ReflectUtil.getField(B.class, "t2");
        assertEquals(String.class, genericResolver.getGenericType(t2));
    }

}