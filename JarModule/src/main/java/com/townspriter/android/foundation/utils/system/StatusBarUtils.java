package com.townspriter.android.foundation.utils.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.townspriter.android.foundation.utils.reflect.ReflectionHelper;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

/******************************************************************************
 * @Path Foundation:StatusBarUtils
 * @Describe 状态栏的配置工具类.主要处理沉浸状态栏的相关逻辑
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class StatusBarUtils
{
    public static final int SYSTEMxUIxFLAGxLIGHTxSTATUSxBAR=0x00002000;
    public static final int FLAGxDRAWSxSYSTEMxBARxBACKGROUNDS=0x80000000;
    private static boolean sEnableTransparentStatusBar;
    private static boolean sHasCheckTransparentStatusBar;
    private static boolean sEnableChangeStatusBarFontColor;
    private static boolean sHasCheckChangeStatusBarFontColor;
    
    /**
     * @return
     * true 开启透明通知栏
     * false 不开启透明通知栏
     */
    public static boolean checkTransparentStatusBar(Context context)
    {
        if(sHasCheckTransparentStatusBar)
        {
            return sEnableTransparentStatusBar;
        }
        if(VERSION.SDK_INT>=19)
        {
            DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
            int screenWidth=displayMetrics.widthPixels;
            int screenHeight=displayMetrics.heightPixels;
            boolean isResolutionHigherThanWVGA=SystemUtil.isResolutionHigherThanWVGA(screenWidth,screenHeight);
            if(isResolutionHigherThanWVGA)
            {
                sEnableTransparentStatusBar=true;
            }
        }
        else
        {
            sEnableTransparentStatusBar=false;
        }
        sHasCheckTransparentStatusBar=true;
        sEnableTransparentStatusBar=sEnableTransparentStatusBar||VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxM;
        return sEnableTransparentStatusBar;
    }
    
    public static boolean checkChangeStatusBarColorEnable()
    {
        if(sHasCheckChangeStatusBarFontColor)
        {
            return sEnableChangeStatusBarFontColor;
        }
        sEnableChangeStatusBarFontColor=isSupportModifyStatusBarTextColor();
        sHasCheckChangeStatusBarFontColor=true;
        return sEnableChangeStatusBarFontColor;
    }
    
    public static boolean isSupportModifyStatusBarTextColor()
    {
        return SystemUtil.isMiUIV6orAbove()||isSupportStatusBarTextModifyFlyme()||isSupportStatusBarTextModifyLetv();
    }
    
    public static boolean isTransparentStatusBarEnable()
    {
        return true;
    }
    
    public static boolean isChangeStatusBarFontColorEnable()
    {
        return sEnableChangeStatusBarFontColor||VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxM;
    }
    
    public static void setChangeStatusBarFontColorEnable(boolean statusBarFontColorEnable)
    {
        sEnableChangeStatusBarFontColor=statusBarFontColorEnable;
    }
    
    private static void setDarkStatusIcon(Window window,boolean dark)
    {
        if(VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxM)
        {
            if(null!=window)
            {
                View decorView=window.getDecorView();
                if(decorView!=null)
                {
                    int vis=decorView.getSystemUiVisibility();
                    if(dark)
                    {
                        vis|=SYSTEMxUIxFLAGxLIGHTxSTATUSxBAR;
                    }
                    else
                    {
                        vis&=~SYSTEMxUIxFLAGxLIGHTxSTATUSxBAR;
                    }
                    decorView.setSystemUiVisibility(vis);
                }
            }
        }
    }
    
    public static void setStatusColor(Window window,int color)
    {
        if(VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxL)
        {
            if(null!=window)
            {
                ReflectionHelper.invokeMethod(window,"setStatusBarColor",new Class[]{int.class},new Object[]{color});
            }
        }
    }
    
    /**
     * 对于大部分安卓的系统.用谷歌的方法去修改状态栏图标的颜色
     * 小米等用他们自家的公布的方法
     */
    public static void updateStatusTextColor(Activity activity,boolean light)
    {
        if(activity==null)
        {
            return;
        }
        updateStatusTextColor(activity.getWindow(),light);
    }
    
    /**
     * 对于大部分安卓的系统.用谷歌的方法去修改状态栏图标的颜色
     * 小米等用他们自家的公布的方法
     */
    public static void updateStatusTextColor(Window window,boolean light)
    {
        if(!isTransparentStatusBarEnable())
        {
            return;
        }
        if(window==null)
        {
            return;
        }
        if(isStandardStatusBarAboveM())
        {
            setDarkStatusIcon(window,!light);
            setStatusColor(window,Color.TRANSPARENT);
        }
        else
        {
            boolean success=false;
            if(SystemUtil.isMiUIV6orAbove())
            {
                success=setMiUIV6StatusBarTextColor(window,!light);
            }
            else if(isSupportStatusBarTextModifyFlyme())
            {
                success=setMeiZuStatusBarTextColor(window,!light);
            }
            else if(isSupportStatusBarTextModifyLetv())
            {
                success=setLeShiStatusBarTextColor(window,!light);
            }
            if(!success)
            {
                setChangeStatusBarFontColorEnable(false);
            }
        }
    }
    
    @TargetApi(VERSION_CODES.KITKAT)
    public static void configTransparentStatusBar(Window window)
    {
        if(window==null)
        {
            return;
        }
        if(VERSION.SDK_INT<SystemUtil.BUILDxOSxVERSIONxM ||SystemUtil.isMiUIV7OrAbove()||isFlymeAboveM())
        {
            window.setFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS,LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else if(isStandardStatusBarAboveM())
        {
            window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(FLAGxDRAWSxSYSTEMxBARxBACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    
    @TargetApi(VERSION_CODES.KITKAT)
    public static void configTransparentStatusBar(LayoutParams lp)
    {
        if(lp!=null)
        {
            int flags=LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int mask=LayoutParams.FLAG_TRANSLUCENT_STATUS;
            lp.flags=(lp.flags&~mask)|(flags&mask);
        }
    }
    
    /** 是否支持原生的标准状态栏 */
    private static boolean isStandardStatusBarAboveM()
    {
        return VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxM &&!SystemUtil.isMiUIV6orAbove()&&!isSupportStatusBarTextModifyFlyme()&&!isSupportStatusBarTextModifyLetv();
    }
    
    public static void addStatusBarPaddingIfUsingTransparentStatusBar(View contentView,Context context)
    {
        if(isTransparentStatusBarEnable()&&contentView!=null&&context!=null)
        {
            int statusBarHeight=SystemUtil.getStatusBarHeight(context);
            if(contentView.getPaddingTop()!=statusBarHeight)
            {
                contentView.setPadding(contentView.getPaddingLeft(),statusBarHeight,contentView.getPaddingRight(),contentView.getPaddingBottom());
            }
        }
    }
    
    public static void removeStatusBarPaddingIfUsingTransparentStatusBar(View view)
    {
        if(isTransparentStatusBarEnable()&&view!=null&&view.getPaddingTop()!=0)
        {
            view.setPadding(view.getPaddingLeft(),0,view.getPaddingRight(),view.getPaddingBottom());
        }
    }
    
    /** 修改魅族状态栏文字色 */
    public static boolean setMeiZuStatusBarTextColor(Window window,boolean dark)
    {
        boolean result=false;
        if(window!=null)
        {
            try
            {
                LayoutParams lp=window.getAttributes();
                Field darkFlag=LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags=LayoutParams.class.getDeclaredField("meizuFlags");
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
                result=true;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 修改小米状态栏文字色
     */
    public static boolean setMiUIV6StatusBarTextColor(Window window,boolean dark)
    {
        boolean result=false;
        if(!SystemUtil.isMiUIV6orAbove())
        {
            return false;
        }
        Class<? extends Window> clazz=window.getClass();
        try
        {
            int tranceFlag;
            int darkModeFlag;
            Class<?> layoutParams=Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field=layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag=field.getInt(layoutParams);
            field=layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag=field.getInt(layoutParams);
            Method extraFlagField=clazz.getMethod("setExtraFlags",int.class,int.class);
            if(dark)
            {
                /** 状态栏透明且黑色字体 */
                extraFlagField.invoke(window,tranceFlag|darkModeFlag,tranceFlag|darkModeFlag);
            }
            else
            {
                /** 清除黑色字体 */
                extraFlagField.invoke(window,0,darkModeFlag);
            }
            result=true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /** 修改乐视状态栏文字色 */
    public static boolean setLeShiStatusBarTextColor(Window window,boolean dark)
    {
        boolean result=false;
        if(window!=null)
        {
            try
            {
                int color;
                if(dark)
                {
                    color=Color.BLACK;
                }
                else
                {
                    color=Color.WHITE;
                }
                ReflectionHelper.invokeMethod(window,"setStatusBarIconColor",new Class[]{int.class},new Object[]{color});
                result=true;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return result;
    }
    
    public static boolean isSupportStatusBarTextModifyFlyme()
    {
        return Build.DISPLAY.contains("Flyme")&&(19<=VERSION.SDK_INT);
    }
    
    /** 乐视手机.针对6.0以下 */
    public static boolean isSupportStatusBarTextModifyLetv()
    {
        boolean result=false;
        String androidVersion=VERSION.RELEASE;
        String brand=Build.BRAND;
        if(androidVersion==null||brand==null)
        {
            return result;
        }
        if("Letv".equalsIgnoreCase(brand))
        {
            result=true;
        }
        if(Build.MANUFACTURER!=null&&"Letv".equalsIgnoreCase(Build.MANUFACTURER))
        {
            result=true;
        }
        return result&&19<=VERSION.SDK_INT&&VERSION.SDK_INT<SystemUtil.BUILDxOSxVERSIONxM;
    }
    
    public static boolean isFlymeAboveM()
    {
        return Build.DISPLAY.contains("Flyme")&&(23<=VERSION.SDK_INT);
    }
}
