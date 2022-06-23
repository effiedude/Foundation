package com.townspriter.base.foundation.utils.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.townspriter.base.foundation.utils.system.SystemUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TranslucentStatusBar
{
    public static void translucentActivity(Activity activity,boolean dark)
    {
        setTranslucentStatusIfNeed(activity,dark);
    }
    
    private static void setTranslucentStatusIfNeed(Activity activity,boolean dark)
    {
        if(activity==null)
        {
            return;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if(isMiUI())
        {
            setTranslucentStatusForMIUI(activity,dark);
            return;
        }
        if(isMeiZu())
        {
            setTranslucentStatusForFlyMe(activity,dark);
            return;
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            setTranslucentStatusAboveM(activity,dark);
            return;
        }
        if(isAndroidLOLLIPOP())
        {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.BLACK);
            activity.getWindow().getDecorView().requestLayout();
        }
    }
    
    @TargetApi(Build.VERSION_CODES.M)
    private static void setTranslucentStatusAboveM(Activity activity,boolean dark)
    {
        Window window=activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(dark)
        {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        else
        {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    
    private static void setTranslucentStatusForMIUI(Activity activity,boolean dark)
    {
        Window window=activity.getWindow();
        if(window!=null)
        {
            Class clazz=window.getClass();
            try
            {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1)
                {
                    setTranslucentStatusAboveM(activity,dark);
                    return;
                }
                Class layoutParams=Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field=layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag=field.getInt(layoutParams);
                Method extraFlagField=clazz.getMethod("setExtraFlags",int.class,int.class);
                if(dark)
                {
                    // 状态栏透明且黑色字体
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);
                }
                else
                {
                    // 清除黑色字体
                    extraFlagField.invoke(window,0,darkModeFlag);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private static void setTranslucentStatusForFlyMe(Activity activity,boolean dark)
    {
        Window window=activity.getWindow();
        if(window!=null)
        {
            try
            {
                WindowManager.LayoutParams lp=window.getAttributes();
                Field darkFlag=WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags=WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit=darkFlag.getInt(null);
                int value=meizuFlags.getInt(lp);
                if(dark)
                {
                    value|=bit;
                }
                else
                {
                    value&=~bit;
                }
                meizuFlags.setInt(lp,value);
                window.setAttributes(lp);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private static boolean isMiUI()
    {
        return SystemUtil.isMIUISystemV5OrAbove();
    }
    
    private static boolean isMeiZu()
    {
        return SystemUtil.isMeiZuMXSeries();
    }
    
    public static boolean isAndroidLOLLIPOP()
    {
        return Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP||Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP_MR1;
    }
}
