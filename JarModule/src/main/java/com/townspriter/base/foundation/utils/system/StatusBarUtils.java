package com.townspriter.base.foundation.utils.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.townspriter.base.foundation.utils.reflect.ReflectionHelper;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path StatusBarUtils
 * @describe 状态栏的配置工具类.主要处理沉浸状态栏的相关逻辑
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
    
    /*********************************
     * @function isSupportTransparentStatusBar
     * @since JDK 1.7.0-79
     * @describe 检测当前系统是否支持透明系统栏
     * @return
     * boolean <code>true:支持 false:不支持</code>
     * @date 2021-07-19 11:49:04
     * @version 1.0.0.0
     * ********************************
     */
    public static boolean isSupportTransparentStatusBar(Context context)
    {
        if(sHasCheckTransparentStatusBar)
        {
            return sEnableTransparentStatusBar;
        }
        if(VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxK)
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
    
    public static boolean isSupportChangeStatusBarTextColor()
    {
        if(sHasCheckChangeStatusBarFontColor)
        {
            return sEnableChangeStatusBarFontColor;
        }
        sEnableChangeStatusBarFontColor=isSupportChangeStatusBarTextColorInner();
        sHasCheckChangeStatusBarFontColor=true;
        return sEnableChangeStatusBarFontColor;
    }
    
    private static void setSupportChangeStatusBarTextColor(boolean statusBarFontColorEnable)
    {
        sEnableChangeStatusBarFontColor=statusBarFontColorEnable;
    }
    
    public static void updateColorStatusBar(@NonNull Activity activity,boolean dark,int color)
    {
        if(isStandardStatusBarAboveM())
        {
            setDarkStatusIcon(activity.getWindow(),dark);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
            activity.getWindow().setNavigationBarColor(color);
        }
        else
        {
            boolean success=false;
            if(SystemUtil.isMiUIV6orAbove())
            {
                success=setMiUIV6StatusBarTextColor(activity.getWindow(),dark);
            }
            else if(SystemUtil.isSupportStatusBarTextModifyFlyme())
            {
                success=setMeiZuStatusBarTextColor(activity.getWindow(),dark);
            }
            else if(SystemUtil.isSupportStatusBarTextModifyLetv())
            {
                success=setLeShiStatusBarTextColor(activity.getWindow(),dark);
            }
            else if(Build.VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxK)
            {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ViewGroup decorView=(ViewGroup)activity.getWindow().getDecorView();
                int count=decorView.getChildCount();
                if(count>0&&decorView.getChildAt(count-1) instanceof StatusBarView)
                {
                    decorView.getChildAt(count-1).setBackgroundColor(color);
                }
                else
                {
                    StatusBarView statusView=createStatusBarView(activity,color);
                    decorView.addView(statusView);
                }
                ViewGroup rootView=(ViewGroup)((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
                rootView.setFitsSystemWindows(true);
                rootView.setClipToPadding(true);
                success=true;
            }
            if(!success)
            {
                setSupportChangeStatusBarTextColor(false);
            }
        }
    }
    
    /**
     * 对于大部分安卓的系统.用谷歌的方法去修改状态栏图标的颜色
     * 小米等用他们自家的公布的方法
     */
    @Deprecated
    public static void updateTransparentStatusBar(@NonNull Activity activity,boolean dark)
    {
        if(activity==null)
        {
            return;
        }
        updateTransparentStatusBarInner(activity,dark);
    }
    
    @TargetApi(VERSION_CODES.KITKAT)
    public static void configTransparentStatusBar(@NonNull Window window)
    {
        if(VERSION.SDK_INT<SystemUtil.BUILDxOSxVERSIONxM||SystemUtil.isMiUIV7OrAbove()||SystemUtil.isFlymeAboveM())
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
    public static void configTransparentStatusBar(LayoutParams layoutParams)
    {
        if(layoutParams!=null)
        {
            int flags=LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int mask=LayoutParams.FLAG_TRANSLUCENT_STATUS;
            layoutParams.flags=(layoutParams.flags&~mask)|(flags&mask);
        }
    }
    
    @TargetApi(VERSION_CODES.KITKAT)
    public static void addStatusBarPaddingIfUsingTransparentStatusBar(@NonNull View contentView,@NonNull Context context)
    {
        if(isSupportTransparentStatusBarInner()&&contentView!=null&&context!=null)
        {
            int statusBarHeight=SystemUtil.getStatusBarHeight(context);
            if(contentView.getPaddingTop()!=statusBarHeight)
            {
                contentView.setPadding(contentView.getPaddingLeft(),statusBarHeight,contentView.getPaddingRight(),contentView.getPaddingBottom());
            }
        }
    }
    
    @TargetApi(VERSION_CODES.KITKAT)
    public static void removeStatusBarPaddingIfUsingTransparentStatusBar(@NonNull View view)
    {
        if(isSupportTransparentStatusBarInner()&&view!=null&&view.getPaddingTop()!=0)
        {
            view.setPadding(view.getPaddingLeft(),0,view.getPaddingRight(),view.getPaddingBottom());
        }
    }
    
    /**
     * 对于大部分安卓的系统.用谷歌的方法去修改状态栏图标的颜色
     * 如果是小米手机等厂商则使用他们自家的公布的方法
     */
    private static void updateTransparentStatusBarInner(@NonNull Activity activity,boolean dark)
    {
        if(!isSupportTransparentStatusBarInner())
        {
            return;
        }
        if(activity.getWindow()==null)
        {
            return;
        }
        if(isStandardStatusBarAboveM())
        {
            setDarkStatusIcon(activity.getWindow(),dark);
            setStatusBarColor(activity.getWindow(),Color.TRANSPARENT);
        }
        else
        {
            boolean success=false;
            if(SystemUtil.isMiUIV6orAbove())
            {
                success=setMiUIV6StatusBarTextColor(activity.getWindow(),dark);
            }
            else if(SystemUtil.isSupportStatusBarTextModifyFlyme())
            {
                success=setMeiZuStatusBarTextColor(activity.getWindow(),dark);
            }
            else if(SystemUtil.isSupportStatusBarTextModifyLetv())
            {
                success=setLeShiStatusBarTextColor(activity.getWindow(),dark);
            }
            else if(Build.VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxK)
            {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ViewGroup decorView=(ViewGroup)activity.getWindow().getDecorView();
                int count=decorView.getChildCount();
                if(count>0&&decorView.getChildAt(count-1) instanceof StatusBarView)
                {
                    decorView.getChildAt(count-1).setBackgroundColor(Color.TRANSPARENT);
                }
                else
                {
                    StatusBarView statusView=createStatusBarView(activity,Color.TRANSPARENT);
                    decorView.addView(statusView);
                }
                ViewGroup rootView=(ViewGroup)((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
                rootView.setFitsSystemWindows(true);
                rootView.setClipToPadding(true);
                success=true;
            }
            if(!success)
            {
                setSupportChangeStatusBarTextColor(false);
            }
        }
    }
    
    /** 修改魅族状态栏文字色 */
    private static boolean setMeiZuStatusBarTextColor(Window window,boolean dark)
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
    
    /** 修改小米状态栏文字色 */
    private static boolean setMiUIV6StatusBarTextColor(Window window,boolean dark)
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
    private static boolean setLeShiStatusBarTextColor(Window window,boolean dark)
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
    
    private static boolean isSupportTransparentStatusBarInner()
    {
        return true;
    }
    
    private static boolean isSupportChangeStatusBarTextColorInner()
    {
        return sEnableChangeStatusBarFontColor||VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxM||SystemUtil.isMiUIV6orAbove()||SystemUtil.isSupportStatusBarTextModifyFlyme()||SystemUtil.isSupportStatusBarTextModifyLetv();
    }
    
    private static void setStatusBarColor(Window window,int color)
    {
        if(VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxL)
        {
            if(null!=window)
            {
                ReflectionHelper.invokeMethod(window,"setStatusBarColor",new Class[]{int.class},new Object[]{color});
            }
        }
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
                    int visibilityFlag=decorView.getSystemUiVisibility();
                    if(dark)
                    {
                        visibilityFlag|=SYSTEMxUIxFLAGxLIGHTxSTATUSxBAR;
                    }
                    else
                    {
                        visibilityFlag&=~SYSTEMxUIxFLAGxLIGHTxSTATUSxBAR;
                    }
                    decorView.setSystemUiVisibility(visibilityFlag);
                }
            }
        }
    }
    
    /** 是否支持原生的标准状态栏 */
    private static boolean isStandardStatusBarAboveM()
    {
        return VERSION.SDK_INT>=SystemUtil.BUILDxOSxVERSIONxM&&!SystemUtil.isMiUIV6orAbove()&&!SystemUtil.isSupportStatusBarTextModifyFlyme()&&!SystemUtil.isSupportStatusBarTextModifyLetv();
    }
    
    private static StatusBarView createStatusBarView(Activity activity,int color)
    {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView=new StatusBarView(activity);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,SystemUtil.getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }
    
    private static class StatusBarView extends View
    {
        public StatusBarView(Context context)
        {
            super(context);
        }
        
        public StatusBarView(Context context,@Nullable AttributeSet attrs)
        {
            super(context,attrs);
        }
        
        public StatusBarView(Context context,@Nullable AttributeSet attrs,int defStyleAttr)
        {
            super(context,attrs,defStyleAttr);
        }
        
        public StatusBarView(Context context,@Nullable AttributeSet attrs,int defStyleAttr,int defStyleRes)
        {
            super(context,attrs,defStyleAttr,defStyleRes);
        }
    }
}
