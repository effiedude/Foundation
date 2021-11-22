package com.townspriter.base.foundation.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.townspriter.base.foundation.utils.log.Logger;

/******************************************************************************
 * @path Foundation:ReflectionHelper
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class ReflectionHelper
{
    public static final int INVALIDxVALUE=-1;
    private static final String TAG="ReflectionHelper";
    
    /**
     * 通过构造器取得实例
     * 
     * @param className
     * 类的全限定名
     * @param intArgsClass
     * 构造函数的参数类型
     * @param intArgs
     * 构造函数的参数值
     * @return Object
     */
    public static Object getObjectByConstructor(String className,Class[] intArgsClass,Object[] intArgs)
    {
        Object returnObj=null;
        try
        {
            Class classType=Class.forName(className);
            /** 找到指定的构造方法 */
            Constructor constructor=classType.getDeclaredConstructor(intArgsClass);
            /** 设置安全检查.访问私有构造函数必须 */
            constructor.setAccessible(true);
            returnObj=constructor.newInstance(intArgs);
        }
        catch(InstantiationException instantiationException)
        {
            Logger.e(TAG,Logger.getStackTraceString(instantiationException));
        }
        catch(InvocationTargetException invocationTargetException)
        {
            Logger.e(TAG,Logger.getStackTraceString(invocationTargetException));
        }
        catch(NoSuchMethodException noSuchMethodException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchMethodException));
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(ClassNotFoundException classNotFoundException)
        {
            Logger.e(TAG,Logger.getStackTraceString(classNotFoundException));
        }
        return returnObj;
    }
    
    public static Object getStaticFieldValue(Class cls,String fieldName)
    {
        Field field;
        Object fieldValue=null;
        try
        {
            field=cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            fieldValue=field.get(null);
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(NoSuchFieldException noSuchFieldException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchFieldException));
        }
        return fieldValue;
    }
    
    /**
     * 访问类成员变量
     * 
     * @param object
     * 访问对象
     * @param filedName
     * 指定成员变量名
     * @return 取得的成员变量的值
     */
    public static int getIntFieldValue(Object object,String filedName)
    {
        Class classType=object.getClass();
        Field field;
        int fieldValue=INVALIDxVALUE;
        try
        {
            field=classType.getDeclaredField(filedName);
            field.setAccessible(true);
            fieldValue=field.getInt(object);
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(NoSuchFieldException noSuchFieldException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchFieldException));
        }
        return fieldValue;
    }
    
    /**
     * 从类访问类成员变量
     * 
     * @param cls
     * 访问对象
     * @param filedName
     * 指定成员变量名
     * @return 取得的成员变量的值
     */
    public static int getStaticIntFieldValue(Class cls,String filedName)
    {
        Field field;
        int filedValue=0;
        try
        {
            field=cls.getDeclaredField(filedName);
            field.setAccessible(true);
            filedValue=field.getInt(cls);
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(NoSuchFieldException noSuchFieldException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchFieldException));
        }
        return filedValue;
    }
    
    public static void setFieldValue(Object obj,String field,Object value)
    {
        try
        {
            Field fieldTemplate;
            try
            {
                fieldTemplate=obj.getClass().getDeclaredField(field);
            }
            catch(Exception exception)
            {
                fieldTemplate=obj.getClass().getField(field);
            }
            fieldTemplate.setAccessible(true);
            fieldTemplate.set(obj,value);
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(NoSuchFieldException noSuchFieldException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchFieldException));
        }
    }
    
    public static void setSuperFieldValue(Object obj,String field,Object value)
    {
        try
        {
            Field fieldTemplate=null;
            Class<?> curClass=obj.getClass().getSuperclass();
            for(;curClass!=null;)
            {
                try
                {
                    fieldTemplate=curClass.getDeclaredField(field);
                    if(fieldTemplate!=null)
                    {
                        break;
                    }
                }
                catch(Exception exception)
                {
                    curClass=curClass.getSuperclass();
                }
            }
            if(fieldTemplate!=null)
            {
                fieldTemplate.setAccessible(true);
                fieldTemplate.set(obj,value);
            }
        }
        catch(IllegalAccessException e)
        {
            Logger.e(TAG,Logger.getStackTraceString(e));
        }
    }
    
    public static Object getSuperFieldValue(Object obj,String field)
    {
        try
        {
            Field fieldTemplate=null;
            Class<?> curClass=obj.getClass().getSuperclass();
            for(;curClass!=null;)
            {
                try
                {
                    fieldTemplate=curClass.getDeclaredField(field);
                    if(fieldTemplate!=null)
                    {
                        break;
                    }
                }
                catch(Exception exception)
                {
                    curClass=curClass.getSuperclass();
                }
            }
            if(fieldTemplate!=null)
            {
                fieldTemplate.setAccessible(true);
                return fieldTemplate.get(obj);
            }
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        return null;
    }
    
    public static Object getFieldValue(Object obj,String field)
    {
        try
        {
            Field fieldTemplate;
            try
            {
                fieldTemplate=obj.getClass().getDeclaredField(field);
            }
            catch(Exception exception)
            {
                fieldTemplate=obj.getClass().getField(field);
            }
            fieldTemplate.setAccessible(true);
            return fieldTemplate.get(obj);
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(NoSuchFieldException noSuchFieldException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchFieldException));
        }
        return null;
    }
    
    public static Object invokeMethod(Object obj,String method)
    {
        try
        {
            Method methodTemplate;
            try
            {
                methodTemplate=obj.getClass().getDeclaredMethod(method);
            }
            catch(Exception exception)
            {
                methodTemplate=obj.getClass().getMethod(method);
            }
            methodTemplate.setAccessible(true);
            return methodTemplate.invoke(obj);
        }
        catch(NoSuchMethodException noSuchMethodException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchMethodException));
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(InvocationTargetException invocationTargetException)
        {
            Logger.e(TAG,Logger.getStackTraceString(invocationTargetException));
        }
        return null;
    }
    
    public static Object invokeMethod(Object o,String methodName,Class[] argsClass,Object[] args)
    {
        Object returnValue=null;
        try
        {
            Class<?> c=o.getClass();
            Method method;
            method=c.getMethod(methodName,argsClass);
            method.setAccessible(true);
            returnValue=method.invoke(o,args);
        }
        catch(NoSuchMethodException noSuchMethodException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchMethodException));
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(InvocationTargetException invocationTargetException)
        {
            Logger.e(TAG,Logger.getStackTraceString(invocationTargetException));
        }
        return returnValue;
    }
    
    /**
     * 反射调用静态方法
     * 
     * @param cls
     * @param methodName
     * @param argsClass
     * @param args
     * @return
     */
    public static Object invokeStaticMethod(Class<?> cls,String methodName,Class[] argsClass,Object[] args)
    {
        Object returnValue=null;
        try
        {
            Method method=cls.getMethod(methodName,argsClass);
            returnValue=method.invoke(null,args);
        }
        catch(NoSuchMethodException noSuchMethodException)
        {
            Logger.e(TAG,Logger.getStackTraceString(noSuchMethodException));
        }
        catch(IllegalAccessException illegalAccessException)
        {
            Logger.e(TAG,Logger.getStackTraceString(illegalAccessException));
        }
        catch(InvocationTargetException invocationTargetException)
        {
            Logger.e(TAG,Logger.getStackTraceString(invocationTargetException));
        }
        return returnValue;
    }
}
