package com.townspriter.android.foundation.utils.log;
/******************************************************************************
 * @path Foundation:ILogger
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 17:03:18
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface ILogger
{
    int log(int priority,String tag,String msg);
    
    String getStackTraceString(Throwable throwable);
}
