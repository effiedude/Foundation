package com.townspriter.base.foundation.utils.log;

import android.util.Log;
import androidx.annotation.NonNull;

/******************************************************************************
 * @path Foundation:LogImpl
 * @version 1.0.0.0
 * @describe 日志打印代理
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 2021-05-30 16:53:10
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class LogImpl implements ILogger
{
    private String mAppTag;
    
    public LogImpl(@NonNull String appTag)
    {
        mAppTag=appTag;
    }
    
    @Override
    public int log(int i,String tag,String msg)
    {
        switch(i)
        {
            case Log.INFO:
                Log.i(mAppTag+tag,msg);
                break;
            case Log.WARN:
                Log.w(mAppTag+tag,msg);
                break;
            case Log.ERROR:
                Log.e(mAppTag+tag,msg);
                break;
            case Log.DEBUG:
            default:
                Log.d(mAppTag+tag,msg);
                break;
        }
        return 0;
    }
    
    @Override
    public String getStackTraceString(Throwable throwable)
    {
        return throwable.getMessage();
    }
}
