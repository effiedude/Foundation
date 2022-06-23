package com.townspriter.base.foundation.utils.os;

import java.lang.reflect.Method;

/******************************************************************************
 * @path SystemProperties
 * @describe 反射替代系统类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class SystemProperties
{
    private static Class<?> sSystemProperties;
    private static Method sGetMethod;
    private static Method sGetBooleanMethod;
    
    private static Class ensureClassInited() throws Exception
    {
        if(sSystemProperties==null)
        {
            sSystemProperties=Class.forName("android.os.SystemProperties");
        }
        return sSystemProperties;
    }
    
    public static String get(String key)
    {
        return get(key,"");
    }
    
    public static String get(String key,String defValue)
    {
        try
        {
            ensureClassInited();
            if(sGetMethod==null)
            {
                sGetMethod=sSystemProperties.getDeclaredMethod("get",String.class,String.class);
            }
            return (String)sGetMethod.invoke(null,key,defValue);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return defValue;
        }
    }
    
    public static boolean getBoolean(String key,boolean defValue)
    {
        try
        {
            ensureClassInited();
            if(sGetBooleanMethod==null)
            {
                sGetBooleanMethod=sSystemProperties.getDeclaredMethod("getBoolean",String.class,boolean.class);
            }
            return (Boolean)sGetBooleanMethod.invoke(null,key,defValue);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return defValue;
        }
    }
}
