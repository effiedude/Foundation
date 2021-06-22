package com.townspriter.android.foundation.utils.device;

import android.app.ActivityManager.MemoryInfo;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.townspriter.android.foundation.Foundation;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;

/******************************************************************************
 * @Path Foundation:DeviceInfo
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
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
