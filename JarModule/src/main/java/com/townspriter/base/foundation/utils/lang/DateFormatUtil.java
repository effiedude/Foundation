package com.townspriter.base.foundation.utils.lang;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;

/******************************************************************************
 * @path DateFormatUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
