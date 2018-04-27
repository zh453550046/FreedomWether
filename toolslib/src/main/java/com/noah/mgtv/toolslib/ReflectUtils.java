package com.noah.mgtv.toolslib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static Object invokeDeclaredMethod(Object obj, String methodName, Class<?>[] types, Object[] args) {
        Object object = null;
        try {
            object = invokeDeclaredMethodOrThrow(null, methodName, types, args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static Object invokeDeclaredSuperMethod(Object obj, String methodName, Class<?>[] types, Object[] args) {
        Object object = null;
        try {
            Class<?> clazz = obj.getClass();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                object = invokeDeclaredMethod(obj, methodName, types, args);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static Object invokeDeclaredMethodOrThrow(Object obj, String methodName, Class<?>[] types, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = obj.getClass().getDeclaredMethod(methodName, types);
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] types, Object[] args) {
        Object object = null;
        try {
            object = invokeMethodOrThrow(obj, methodName, types, args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static Object invokeMethodOrThrow(Object obj, String methodName, Class<?>[] types, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = obj.getClass().getMethod(methodName, types);
        return method.invoke(obj, args);
    }

    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Class<?>[] types, Object[] args) {
        Object object = null;
        try {
            object = invokeStaticMethodOrThrow(clazz, methodName, types, args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static Object invokeStaticMethodOrThrow(Class<?> clazz, String methodName, Class<?>[] types, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method method = clazz.getDeclaredMethod(methodName, types);
        method.setAccessible(true);
        return method.invoke(null, args);
    }

    public static Object getDeclaredSuperField(Object obj, String fieldName) {
        Object object = null;
        try {
            Class<?> clazz = obj.getClass();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    object = field.get(obj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static Object getDeclaredField(Object obj, String fieldName) {
        Object object = null;
        try {
            Class<?> clazz = obj.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            object = field.get(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static void invokeDeclaredField(Object obj, String fieldName, Class<?> type, Object arg) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (type == int.class) {
                field.setInt(obj, (Integer) arg);
            } else if (type == boolean.class) {
                field.setBoolean(obj, (Boolean) arg);
            } else if (type == char.class) {
                field.setChar(obj, (Character) arg);
            } else if (type == float.class) {
                field.setFloat(obj, (Float) arg);
            } else if (type == long.class) {
                field.setLong(obj, (Long) arg);
            } else if (type == short.class) {
                field.setShort(obj, (Short) arg);
            } else if (type == byte.class) {
                field.setByte(obj, (Byte) arg);
            } else if (type == double.class) {
                field.setDouble(obj, (Double) arg);
            } else {
                field.set(obj, arg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
