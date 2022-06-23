package com.townspriter.base.foundation.utils.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path SimpleActivityLifecycleCallback
 * @describe 当应用进入前后台以后触发此监听函数
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class SimpleActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks
{
    @Override
    public void onActivityCreated(@NonNull Activity activity,@Nullable Bundle savedInstanceState)
    {}
    
    @Override
    public void onActivityStarted(@NonNull Activity activity)
    {}
    
    @Override
    public void onActivityResumed(@NonNull Activity activity)
    {}
    
    @Override
    public void onActivityPaused(@NonNull Activity activity)
    {}
    
    @Override
    public void onActivityStopped(@NonNull Activity activity)
    {}
    
    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity,@NonNull Bundle outState)
    {}
    
    @Override
    public void onActivityDestroyed(@NonNull Activity activity)
    {}
}
