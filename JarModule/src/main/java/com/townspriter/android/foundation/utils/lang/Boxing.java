package com.townspriter.android.foundation.utils.lang;
/******************************************************************************
 * @Path Foundation:Boxing
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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
