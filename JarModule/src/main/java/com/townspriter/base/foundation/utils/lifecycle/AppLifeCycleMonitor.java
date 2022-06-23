package com.townspriter.base.foundation.utils.lifecycle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path AppLifeCycleMonitor
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class AppLifeCycleMonitor
{
    private static final List<AppStateListener> APPxSTATExLISTENERS=new ArrayList<>(8);
    private static int sActivityForegroundCount;
    private static WeakReference<Activity> mTopActivityRef;
    private static WeakReference<Activity> mStartedActivityRef;
    
    public static void init(Application application)
    {
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(@NonNull Activity activity,@Nullable Bundle savedInstanceState)
            {}
            
            @Override
            public void onActivityStarted(@NonNull Activity activity)
            {
                /** 考虑到Activity对象可能被复用Activity.onCreated()不会被再次回调.但这个Activity确实被调到最顶层.因此不能放onActivityCreated() */
                mTopActivityRef=new WeakReference<>(activity);
                mStartedActivityRef=new WeakReference<>(activity);
                if(isBackground())
                {
                    synchronized(APPxSTATExLISTENERS)
                    {
                        for(AppStateListener listener:APPxSTATExLISTENERS)
                        {
                            listener.onEnterForeground();
                        }
                    }
                }
                sActivityForegroundCount++;
            }
            
            @Override
            public void onActivityResumed(@NonNull Activity activity)
            {
                /** 如果在透明主题Activity.onDestroy()处调了getTopActivity.此时拿到的对象会是当前正在Activity.finish()的这个Activity.因为下一层Activity.onStart()不会被调用.因此要在Activity.onResume()的时候更新这个值 */
                if(getTopActivity()!=activity)
                {
                    mTopActivityRef=new WeakReference<>(activity);
                }
            }
            
            @Override
            public void onActivityPaused(@NonNull Activity activity)
            {}
            
            @Override
            public void onActivityStopped(@NonNull Activity activity)
            {
                sActivityForegroundCount=Math.max(--sActivityForegroundCount,0);
                if(isBackground())
                {
                    synchronized(APPxSTATExLISTENERS)
                    {
                        for(AppStateListener listener:APPxSTATExLISTENERS)
                        {
                            listener.onEnterBackground();
                        }
                    }
                }
            }
            
            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity,@NonNull Bundle outState)
            {}
            
            @Override
            public void onActivityDestroyed(@NonNull Activity activity)
            {}
        });
    }
    
    public static void registerAppStateListener(AppStateListener listener)
    {
        synchronized(APPxSTATExLISTENERS)
        {
            APPxSTATExLISTENERS.add(listener);
        }
    }
    
    public static void unregisterAppStateListener(AppStateListener listener)
    {
        synchronized(APPxSTATExLISTENERS)
        {
            APPxSTATExLISTENERS.remove(listener);
        }
    }
    
    public static boolean isBackground()
    {
        return sActivityForegroundCount==0;
    }
    
    public static boolean isForeground()
    {
        return !isBackground();
    }
    
    public static Activity getTopActivity()
    {
        return getActiveActivity(mTopActivityRef);
    }
    
    public static Activity getStartedActivity()
    {
        return getActiveActivity(mStartedActivityRef);
    }
    
    public static Activity getActiveActivity(WeakReference<Activity> activityRef)
    {
        if(activityRef!=null&&activityRef.get()!=null)
        {
            Activity activity=activityRef.get();
            return activity.isFinishing()?null:activity;
        }
        return null;
    }
}
