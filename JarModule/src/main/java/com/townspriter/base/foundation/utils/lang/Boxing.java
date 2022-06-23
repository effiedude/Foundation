package com.townspriter.base.foundation.utils.lang;
/******************************************************************************
 * @path Boxing
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Boxing
{
    public static boolean unBoxing(Object object,boolean defautValue)
    {
        if(!(object instanceof Boolean))
        {
            return defautValue;
        }
        return (Boolean)object;
    }
    
    public static int unBoxing(Object object,int defautValue)
    {
        if(!(object instanceof Integer))
        {
            return defautValue;
        }
        return (Integer)object;
    }
    
    public static long unBoxing(Object object,long defautValue)
    {
        if(object instanceof Long)
        {
            return (long)object;
        }
        return defautValue;
    }
}
