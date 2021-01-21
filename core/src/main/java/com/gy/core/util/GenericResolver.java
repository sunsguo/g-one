package com.gy.core.util;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 泛型解析类
 */
public class GenericResolver {

    /**
     * 类型泛型映射
     *
     * Class:
     *  T1 -> RealType1
     *  T2 -> RealType2
     */
    private final Map<Class<?>, Map<String, Class<?>>> clsGenericMap = new HashMap<>();

    public GenericResolver(Class<?> rootClass) {
        init(rootClass);
    }

    /**
     * 向上遍历，解析所有泛型类型
     *
     * @param rootClass 基础类
     */
    private void init(Class<?> rootClass) {
        Class<?> cls = rootClass;

        while (cls != Object.class) {
            Class<?> superclass = cls.getSuperclass();
            Type genericSuperclass = cls.getGenericSuperclass();

            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) genericSuperclass;
                Type[] actualTypeArguments = type.getActualTypeArguments();

                TypeVariable<? extends Class<?>>[] typeParameters = superclass.getTypeParameters();

                Map<String, Class<?>> m = new HashMap<>();
                for (int i = 0; i < typeParameters.length; i++) {
                    TypeVariable<?> typeParameter = typeParameters[i];

                    Type typeArgument = actualTypeArguments[i];

                    if (typeArgument instanceof Class) {
                        m.put(typeParameter.getTypeName(), (Class<?>) typeArgument);
                    } else {
                        String typeName = typeArgument.getTypeName();

                        Class<?> aClass = clsGenericMap.get(cls).get(typeName);
                        m.put(typeParameter.getTypeName(), aClass);
                    }
                }

                clsGenericMap.put(superclass, m);
            }

            cls = superclass;
        }
    }

    /**
     * 获取字段泛型类型
     *
     * @param field 泛型字段
     * @return 实际类型
     */
    public Class<?> getGenericType(Field field) {
        Class<?> declaringClass = field.getDeclaringClass();
        Type genericType = field.getGenericType();

        return clsGenericMap.get(declaringClass).get(genericType.getTypeName());
    }

    /**
     * 方法参数类型
     *
     * @param method 泛型方法
     * @param index 第几个参数，从0开始
     * @return 实际类型
     */
    public Class<?> getGenericType(Method method, int index) {
        Type genericParameterType = method.getGenericParameterTypes()[index];

        if (genericParameterType instanceof Class<?>) {
            return (Class<?>) genericParameterType;
        }

        Class<?> declaringClass = method.getDeclaringClass();
        return clsGenericMap.get(declaringClass).get(genericParameterType.getTypeName());
    }


}
