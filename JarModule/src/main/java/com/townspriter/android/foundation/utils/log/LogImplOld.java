package com.townspriter.android.foundation.utils.log;

import android.util.Log;

/******************************************************************************
 * @path Foundation:LogImplOld
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 16:58:41
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class LogImplOld implements ILogger
{
    @Override
    public int log(int priority,String tag,String msg)
    {
        if(tag==null||msg==null)
        {
            return 0;
        }
        return Log.println(priority,tag,msg);
    }
    
    @Override
    public String getStackTraceString(Throwable throwable)
    {
        return Log.getStackTraceString(throwable);
    }
}
