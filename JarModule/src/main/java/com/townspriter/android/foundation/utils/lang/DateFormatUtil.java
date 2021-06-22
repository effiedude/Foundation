package com.townspriter.android.foundation.utils.lang;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;

/******************************************************************************
 * @Path Foundation:DateFormatUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class DateFormatUtil
{
    private static final HashMap<String,SimpleDateFormat> mSimpleDateFormatCache=new HashMap<>();
    
    public static SimpleDateFormat getSimpleDateFormat(String format)
    {
        if(!ThreadManager.isMainThread())
        {
            return new SimpleDateFormat(format);
        }
        SimpleDateFormat simpleDateFormat=mSimpleDateFormatCache.get(format);
        if(simpleDateFormat==null)
        {
            simpleDateFormat=new SimpleDateFormat(format);
            mSimpleDateFormatCache.put(format,simpleDateFormat);
        }
        return simpleDateFormat;
    }
}
