package com.townspriter.android.foundation.utils.lang;

import com.townspriter.android.foundation.utils.concurrent.ThreadManager;

/******************************************************************************
 * @Path Foundation:GCUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class GCUtil
{
    private static final Runnable sGcRunnable=new GCRunable();
    
    public static void GC(long delayMillis)
    {
        ThreadManager.postDelayed(ThreadManager.THREADxBACKGROUND,sGcRunnable,delayMillis);
    }
    
    public static void cancelGC()
    {
        ThreadManager.removeRunnable(sGcRunnable);
    }
    
    private static class GCRunable implements Runnable
    {
        @Override
        public void run()
        {
            System.gc();
        }
    }
}
