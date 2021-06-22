package com.townspriter.android.foundation.utils.os;

import java.lang.reflect.Method;

/******************************************************************************
 * @Path Foundation:SystemProperties
 * @Describe 反射替代系统类
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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
