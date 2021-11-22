package com.townspriter.base.foundation.utils.device;

import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.reflect.ReflectionHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/******************************************************************************
 * @path Foundation:DisplayUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class DisplayUtil
{
    private static double sPhysicalScreenSize;
    private static boolean sHasInitScreenSize;
    
    public static int getDensityDpi()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().densityDpi;
    }
    
    public static float getDensity()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().density;
    }
    
    public static float getScaleDensity()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().scaledDensity;
    }
    
    public static float getXdpi()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().xdpi;
    }
    
    public static float getYdpi()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().ydpi;
    }
    
    /** 获取屏幕的宽度.不一定是手机的短边 */
    public static int getScreenWidth()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().widthPixels;
    }
    
    /** 获取屏幕的高度.不一定是手机的长边 */
    public static int getScreenHeight()
    {
        return Foundation.getApplication().getResources().getDisplayMetrics().heightPixels;
    }
    
    public static int getScreenOrientation()
    {
        return Foundation.getApplication().getResources().getConfiguration().orientation;
    }
    
    /** 获取屏幕的真实高度(用此方法可以保证带有虚拟导航栏的手机获取的高度包含状态栏高度) */
    public static int getScreenHeight(Context context)
    {
        int screenH;
        if(context!=null)
        {
            DisplayMetrics dm=new DisplayMetrics();
            WindowManager manager=(WindowManager)(context.getSystemService("window"));
            if(manager==null)
            {
                return getScreenHeight();
            }
            manager.getDefaultDisplay().getMetrics(dm);
            screenH=dm.heightPixels;
        }
        else
        {
            screenH=getScreenHeight();
        }
        return screenH;
    }
    
    /** 获取屏幕的宽度 */
    public static int getScreenWidth(Context context)
    {
        int screenW;
        if(context!=null)
        {
            DisplayMetrics dm=new DisplayMetrics();
            WindowManager manager=(WindowManager)(context.getSystemService("window"));
            if(manager==null)
            {
                return getScreenWidth();
            }
            try
            {
                manager.getDefaultDisplay().getMetrics(dm);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            screenW=dm.widthPixels;
        }
        else
        {
            screenW=getScreenWidth();
        }
        return screenW;
    }
    
    /**
     * 获取屏幕的真实高度(屏幕展示高度+底部导航栏)
     * 注意:用此方法可以保证带有虚拟导航栏的手机获取的高度总是包含状态栏高度
     * 
     * @param context
     * 环境上下文
     * @param includeNavBar
     * 是否包含底部虚拟导航栏
     */
    public static int getScreenRealHeight(Context context,boolean includeNavBar)
    {
        if(context!=null)
        {
            if(includeNavBar)
            {
                if(VERSION.SDK_INT>=VERSION_CODES.JELLY_BEAN_MR1)
                {
                    DisplayMetrics displayMetrics=new DisplayMetrics();
                    WindowManager manager=(WindowManager)(context.getSystemService("window"));
                    if(manager==null)
                    {
                        return getScreenHeight();
                    }
                    manager.getDefaultDisplay().getRealMetrics(displayMetrics);
                    return displayMetrics.heightPixels;
                }
                else
                {
                    return getScreenHeight();
                }
            }
            else
            {
                DisplayMetrics displayMetrics=new DisplayMetrics();
                WindowManager manager=(WindowManager)(context.getSystemService("window"));
                if(manager==null)
                {
                    return getScreenHeight();
                }
                manager.getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.heightPixels;
            }
        }
        else
        {
            return getScreenHeight();
        }
    }
    
    /** 获取屏幕亮度 */
    public static int getSystemBrightness()
    {
        int brightness=0;
        ContentResolver contentResolver=Foundation.getApplication().getContentResolver();
        try
        {
            brightness=Settings.System.getInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS,-1);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return brightness;
    }
    
    /** 获取设备的短边 */
    public static int getDeviceWidth()
    {
        DisplayMetrics displayMetrics=Foundation.getApplication().getResources().getDisplayMetrics();
        return Math.min(displayMetrics.widthPixels,displayMetrics.heightPixels);
    }
    
    /** 获取设备的长边 */
    public static int getDeviceHeight()
    {
        DisplayMetrics dm=Foundation.getApplication().getResources().getDisplayMetrics();
        return Math.max(dm.widthPixels,dm.heightPixels);
    }
    
    public static float getRefreshRate()
    {
        WindowManager wm=(WindowManager)Foundation.getApplication().getSystemService(Context.WINDOW_SERVICE);
        float rate=wm.getDefaultDisplay().getRefreshRate();
        if(Math.abs(rate-0.0f)<0.1f)
        {
            rate=60*1.0f;
        }
        return rate;
    }
    
    public static float convertSPToPixels(float sp)
    {
        return sp*getScaleDensity();
    }
    
    public static int convertDPToPixels(float dips)
    {
        return (int)(dips*getDensity()+0.5f);
    }
    
    /** 获取屏幕物理尺寸(单位:英寸) */
    public static double getScreenInchSize()
    {
        if(sHasInitScreenSize)
        {
            return sPhysicalScreenSize;
        }
        WindowManager wm=(WindowManager)Foundation.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        if(VERSION.SDK_INT>=VERSION_CODES.JELLY_BEAN_MR1)
        {
            display.getRealMetrics(displayMetrics);
        }
        else if(VERSION.SDK_INT>=VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            ReflectionHelper.invokeMethod(display,"getRealMetrics",new Class[]{DisplayMetrics.class},new Object[]{displayMetrics});
        }
        else
        {
            /** 工具库不支持4.0以下设备 */
            display.getMetrics(displayMetrics);
        }
        int widthPixel=displayMetrics.widthPixels;
        int heightPixel=displayMetrics.heightPixels;
        double screenWidth=(double)widthPixel/displayMetrics.xdpi;
        double screenHeight=(double)heightPixel/displayMetrics.ydpi;
        sPhysicalScreenSize=Math.sqrt(Math.pow(screenWidth,2)+Math.pow(screenHeight,2));
        sHasInitScreenSize=true;
        return sPhysicalScreenSize;
    }
    
    public static int getStatusBarHeight()
    {
        int height=0;
        int resourceId=Foundation.getApplication().getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0)
        {
            height=Foundation.getApplication().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
