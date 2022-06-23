package com.townspriter.base.foundation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.townspriter.base.foundation.Foundation.Env.EnvType;
import com.townspriter.base.foundation.utils.lifecycle.AppLifeCycleMonitor;
import com.townspriter.base.foundation.utils.net.NetworkStateChangeReceiver;
import com.townspriter.base.foundation.utils.system.VersionUtil;
import android.app.Application;
import androidx.annotation.IntDef;

/******************************************************************************
 * @path Foundation
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年06月25日 16:50:55
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Foundation
{
    private static Application sApplication;
    private static volatile boolean sInit;
    private static boolean sDebug;
    private static boolean sMonkey;
    private static int sEnv;
    
    public static Application getApplication()
    {
        return sApplication;
    }
    
    /**
     * 是否正式包(非测试和猴子测试)
     *
     * @return 是否正式包
     */
    public static boolean isRelease()
    {
        return !sDebug&&!sMonkey;
    }
    
    public static boolean isDebug()
    {
        return sDebug;
    }
    
    public static void setDebug(boolean debug)
    {
        sDebug=debug;
    }
    
    public static boolean isMonkey()
    {
        return sMonkey;
    }
    
    public static void setMonkey(boolean monkey)
    {
        sMonkey=monkey;
    }
    
    public static void init(Application application)
    {
        if(sInit)
        {
            return;
        }
        sInit=true;
        sApplication=application;
        AppLifeCycleMonitor.init(application);
        NetworkStateChangeReceiver.getInstance().registerNetworkStateChangedReceiver(sApplication);
        VersionUtil.updateVersionInMainProcess();
    }
    
    public static int getEnv()
    {
        return sEnv;
    }
    
    public static void setEnv(@EnvType int env)
    {
        sEnv=env;
    }
    
    public static boolean isOnline()
    {
        return sEnv==Env.ENVxONLINE;
    }
    
    public static boolean isOffline()
    {
        return sEnv==Env.ENVxOFFLINE;
    }
    
    /**
     * 区分线上线下(这里我们暂时区分线上和线下)
     * 线上-主要是摩天轮打出来的包
     * 线下-就是开发者本地构建包
     */
    public static class Env
    {
        public static final int ENVxOFFLINE=0;
        public static final int ENVxONLINE=1;
        
        @IntDef({ENVxOFFLINE,ENVxONLINE})
        @Retention(RetentionPolicy.SOURCE)
        @interface EnvType
        {}
    }
}
