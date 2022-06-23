package com.townspriter.base.foundation.utils.lang;

import com.townspriter.base.foundation.utils.concurrent.ThreadManager;

/******************************************************************************
 * @path GCUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
