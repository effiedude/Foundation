package com.townspriter.base.foundation.utils.log;

import com.townspriter.base.foundation.utils.text.StringUtil;

import android.util.Log;

/******************************************************************************
 * @path Logger
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 16:59:59
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Logger
{
    public static final int LOGxVERBOSE=Log.VERBOSE;
    public static final int LOGxDEBUG=Log.DEBUG;
    public static final int LOGxINFO=Log.INFO;
    public static final int LOGxWARNING=Log.WARN;
    public static final int LOGxERROR=Log.ERROR;
    private static final char LF='\n';
    private static ILogger sLoggerImpl;
    
    public static void setLoggerImpl(ILogger logger)
    {
        sLoggerImpl=logger;
    }
    
    public static String getStackTraceString(Throwable tr)
    {
        ILogger impl=sLoggerImpl;
        if(impl!=null)
        {
            return impl.getStackTraceString(tr);
        }
        else
        {
            return null;
        }
    }
    
    public static int v(String tag,String msg)
    {
        return log(LOGxVERBOSE,tag,msg);
    }
    
    public static int v(String tag,String msg,Throwable tr)
    {
        return log(LOGxVERBOSE,tag,msg+LF+getStackTraceString(tr));
    }
    
    public static int d(String tag,String msg)
    {
        return log(LOGxDEBUG,tag,msg);
    }
    
    public static int d(String tag,String msg,Throwable tr)
    {
        return log(LOGxDEBUG,tag,msg+LF+getStackTraceString(tr));
    }
    
    public static int i(String tag,String msg)
    {
        return log(LOGxINFO,tag,msg);
    }
    
    public static int i(String tag,String msg,Throwable tr)
    {
        return log(LOGxINFO,tag,msg+LF+getStackTraceString(tr));
    }
    
    public static int w(String tag,String msg)
    {
        return log(LOGxWARNING,tag,msg);
    }
    
    public static int w(String tag,String msg,Throwable tr)
    {
        return log(LOGxWARNING,tag,msg+LF+getStackTraceString(tr));
    }
    
    public static int e(String tag,String msg)
    {
        return log(LOGxERROR,tag,msg);
    }
    
    public static int e(String tag,String msg,Throwable tr)
    {
        return log(LOGxERROR,tag,msg+LF+getStackTraceString(tr));
    }
    
    private static int log(int priority,String tag,String msg)
    {
        ILogger impl=sLoggerImpl;
        if(impl!=null)
        {
            return impl.log(priority,tag,msg);
        }
        return 0;
    }
    
    public static void logLongStr(String tag,String str)
    {
        if(StringUtil.isEmpty(str))
        {
            return;
        }
        int maxLength=4000;
        if(str.length()<=maxLength)
        {
            d(tag,str);
            return;
        }
        int index=0;
        String sub;
        while(index<str.length())
        {
            if(str.length()<=index+maxLength)
            {
                sub=str.substring(index);
            }
            else
            {
                sub=str.substring(index,index+maxLength);
            }
            index+=maxLength;
            d(tag,sub);
        }
    }
}
