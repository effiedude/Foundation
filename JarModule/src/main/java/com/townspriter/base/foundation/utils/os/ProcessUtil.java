package com.townspriter.base.foundation.utils.os;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.io.IOUtil;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.text.TextUtils;

/******************************************************************************
 * @path ProcessUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ProcessUtil
{
    private static String sCurrentProcessName;
    private static Boolean sIsMainProcess;
    
    /** 获取当前进程名字 */
    public static String getCurrentProcessName()
    {
        /** 临时变量用于防止多次非同步读造成的可见性问题 */
        String processName=sCurrentProcessName;
        if(TextUtils.isEmpty(processName))
        {
            processName=getProcessName(android.os.Process.myPid());
            sCurrentProcessName=processName;
        }
        return processName;
    }
    
    public static String getProcessName(int pid)
    {
        String result=null;
        ActivityManager am=(ActivityManager)Foundation.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps=am.getRunningAppProcesses();
        if(runningApps!=null)
        {
            for(RunningAppProcessInfo procInfo:runningApps)
            {
                if(procInfo.pid==pid)
                {
                    result=procInfo.processName;
                    break;
                }
            }
        }
        if(StringUtil.isEmpty(result))
        {
            BufferedReader cmdlineReader=null;
            try
            {
                cmdlineReader=new BufferedReader(new InputStreamReader(new FileInputStream("/proc/"+pid+"/cmdline"),StandardCharsets.ISO_8859_1));
                result=cmdlineReader.readLine();
                if(result!=null)
                {
                    result=result.trim();
                }
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
            finally
            {
                IOUtil.safeClose(cmdlineReader);
            }
        }
        if(StringUtil.isEmpty(result))
        {
            result="unknown";
        }
        return result;
    }
    
    public static boolean isMainProcess()
    {
        /** 临时变量用于防止多次非同步读造成的竞争问题 */
        Boolean isMainProcess=sIsMainProcess;
        if(isMainProcess==null)
        {
            isMainProcess=Foundation.getApplication().getPackageName().equals(getCurrentProcessName());
            sIsMainProcess=isMainProcess;
        }
        return isMainProcess;
    }
    
    public static boolean isProcessAlive(String process)
    {
        ActivityManager am=(ActivityManager)Foundation.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps=am.getRunningAppProcesses();
        if(runningApps==null)
        {
            return false;
        }
        for(RunningAppProcessInfo procInfo:runningApps)
        {
            return StringUtil.equals(procInfo.processName,process);
        }
        return false;
    }
}
