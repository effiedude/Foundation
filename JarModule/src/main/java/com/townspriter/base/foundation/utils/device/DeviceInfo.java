package com.townspriter.base.foundation.utils.device;

import com.townspriter.base.foundation.Foundation;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/******************************************************************************
 * @path Foundation:DeviceInfo
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class DeviceInfo
{
    public static final int DEVICEINFOxUNKNOWN=-1;
    
    public static int getNumberOfCPUCores()
    {
        if(VERSION.SDK_INT<=VERSION_CODES.GINGERBREAD_MR1)
        {
            return 1;
        }
        return CPUUtil.getCPUCoreCount();
    }
    
    public static int getCPUMaxFreqKHZ()
    {
        int freq=CPUUtil.getMaxCPUFrequence();
        return freq==CPUUtil.CPUxFREQUENCExUNKNOWN?DEVICEINFOxUNKNOWN:freq;
    }
    
    @TargetApi(VERSION_CODES.JELLY_BEAN)
    public static long getTotalMemory()
    {
        long totalMemory=MemoryUtil.getTotalMemory();
        if(totalMemory<=0)
        {
            Context context=Foundation.getApplication();
            if(context!=null&&VERSION.SDK_INT>=VERSION_CODES.JELLY_BEAN)
            {
                MemoryInfo memInfo=new MemoryInfo();
                ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                if(activityManager!=null)
                {
                    activityManager.getMemoryInfo(memInfo);
                    return memInfo.totalMem;
                }
            }
        }
        else
        {
            return totalMemory*1024;
        }
        return DEVICEINFOxUNKNOWN;
    }
}
