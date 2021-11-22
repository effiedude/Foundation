package com.townspriter.base.foundation.utils.system;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.io.IOUtil;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.os.HandlerEx;
import com.townspriter.base.foundation.utils.reflect.ReflectionHelper;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import androidx.annotation.IntRange;
import androidx.core.app.NotificationManagerCompat;

/******************************************************************************
 * @path Foundation:SystemUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class SystemUtil
{
    /** 程序运行状态 */
    public static final byte APPxSTATExFG=1;
    public static final byte APPxSTATExBG=2;
    public static final byte APPxSTATExUNACTIVE=0;
    public static final String SNAPSHOTxPREFIX="TMPSNAPSHOT";
    public static final int BUILDxOSxVERSIONxM=23;
    public static final int BUILDxOSxVERSIONxL=21;
    public static final int BUILDxOSxVERSIONxK=19;
    /** Intent添加这个标志后,该广播在应用被强制停止后.可以继续运作(Android3.1开始的版本使用.3.1之前不存在这问题) */
    public static final int FLAGxINCLUDExSTOPPEDxPACKAGES=32;
    private static final String TAG="SystemUtil";
    private static final boolean DEBUG=false;
    private static final int SDKxINTxMEIZUxFLYMEx2=16;
    private static final int SDKxINTxMEIZUxFLYMEx3=17;
    private static final String[] MEIZUxSMARTBARxDEVICExLIST={"M040","M045",};
    private static final int SUPPORTxMIxPUSH=-1;
    /** 不建议使用.因为小米九的屏幕最大亮度是4095 */
    private static final float BRIGHTNESSxMAXxVALUE=255L;
    private static final Runnable sGcRunnable=new GCRunnable();
    private static final HashMap<String,SimpleDateFormat> mSimpleDateFormatCache=new HashMap<>();
    public static BuildProperties mBuildProperties;
    /** 建议不要再依赖此变量.如果你需要获取系统状态栏高度请使用SystemUtil.getStatusBarHeight(Context) */
    @Deprecated
    public static int systemStatusBarHeight=-1;
    public static boolean mIsACWindowManager;
    public static long sUnusedIndex;
    private static Context sContext;
    private static String sUCClipboardText;
    private static String sLastSyncUCClipboardText;
    private static boolean sCanUseClipBoxSandBox;
    private static long sLastTimeOfUpdateClipBoard;
    private static int sStatusBarHeight;
    private static boolean sHasCheckStatusBarHeight;
    private static int sNaviBarHeight=-1;
    private static int sVerticalNaviBarHeight=-1;
    private static boolean sNeedDrawFakeStatusBarBgOnWindow=true;
    private static boolean sNeedDrawFakeStatusBarBgOnFullScreen=true;
    private static boolean gIsStatusBarHidden;
    private static boolean sHasCheckedIsRomMainVersionAtLeast4;
    private static boolean sIsRomMainVersionAtLeast4=true;
    private static boolean sHasCheckedMeizuMXSeries;
    private static boolean sIsMeizuMXSeries;
    private static boolean sHasCheckedMeizuFlyemeVersion;
    private static boolean sIsMeizuFlyme2;
    private static boolean sIsMeizuFlyme3;
    private static boolean sHasCheckedIfMIUISystemV5OrAbove;
    private static boolean sIsMIUISystemV5OrAbove;
    private static float sBrightnessLocalMaxValue=-1;
    private static Handler sGcHandler;
    
    /** 必须先初始化 */
    public static void initialize(Context context)
    {
        if(context!=null)
        {
            sContext=context.getApplicationContext();
        }
    }
    
    public static void destroy()
    {
        sContext=null;
    }
    
    private static void checkIfContextInitialized()
    {
        if(sContext==null)
        {
            throw new RuntimeException("必须先初始化");
        }
    }
    
    public static String getLastSyncUCClipboardText()
    {
        return sLastSyncUCClipboardText;
    }
    
    public static void setLastSyncUCClipboardText(String text)
    {
        sLastSyncUCClipboardText=text;
    }
    
    public static String getClipboardText()
    {
        if(isClipBoxSandBoxEnable())
        {
            return sUCClipboardText;
        }
        else
        {
            return getClipboardTextFromSystem();
        }
    }
    
    public static void setClipboardText(String text)
    {
        if(isClipBoxSandBoxEnable())
        {
            if(text!=null)
            {
                sUCClipboardText=text;
                sLastTimeOfUpdateClipBoard=System.currentTimeMillis();
            }
        }
        else
        {
            setClipboardTextToSystem(text);
        }
    }
    
    /**
     * 是否使用剪贴板沙盒
     * 实现:浏览器里面的剪贴都不会直接传给系统剪贴板.只有在切后台或者退出时才会把剪贴内容传给系统剪贴板
     */
    public static boolean isClipBoxSandBoxEnable()
    {
        return canUseClipBoxSandBox();
    }
    
    private static boolean canUseClipBoxSandBox()
    {
        return sCanUseClipBoxSandBox;
    }
    
    public static long getLastTimeOfUpdateClipBoard()
    {
        return sLastTimeOfUpdateClipBoard;
    }
    
    public static void setUseClipBoxSandBox(boolean use)
    {
        sCanUseClipBoxSandBox=use;
    }
    
    public static String getClipboardTextFromSystem()
    {
        checkIfContextInitialized();
        return getClipboardText(sContext);
    }
    
    public static void resetClipboardText()
    {
        sUCClipboardText=null;
    }
    
    public static void setClipboardTextToSystem(String text)
    {
        checkIfContextInitialized();
        setClipboardText(sContext,text);
    }
    
    public static @IntRange(from=0,to=255) int getSystemBrightness()
    {
        checkIfContextInitialized();
        return getSystemBrightness(sContext);
    }
    
    public static int getStatusBarHeight(Context context)
    {
        if(sHasCheckStatusBarHeight)
        {
            return sStatusBarHeight;
        }
        try
        {
            int resourceId=context.getResources().getIdentifier("status_bar_height","dimen","android");
            if(resourceId>0)
            {
                sStatusBarHeight=context.getResources().getDimensionPixelSize(resourceId);
            }
            else
            {
                sStatusBarHeight=guessStatusBarHeight(context);
            }
            sHasCheckStatusBarHeight=true;
        }
        catch(Exception exception)
        {
            sStatusBarHeight=guessStatusBarHeight(context);
            sHasCheckStatusBarHeight=true;
            exception.printStackTrace();
        }
        return sStatusBarHeight;
    }
    
    private static int guessStatusBarHeight(Context context)
    {
        try
        {
            if(context!=null)
            {
                int statusBarHeightDP=25;
                float density=context.getResources().getDisplayMetrics().density;
                return Math.round(density*statusBarHeightDP);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return 0;
    }
    
    public static int getNavigationBarHeight(Context context)
    {
        if(sNaviBarHeight>0)
        {
            return sNaviBarHeight;
        }
        try
        {
            Class<?> clazz=Class.forName("com.android.internal.R$dimen");
            Object object=clazz.newInstance();
            Field field=clazz.getField("navigation_bar_height");
            int x=(Integer)field.get(object);
            sNaviBarHeight=context.getResources().getDimensionPixelSize(x);
        }
        catch(Exception exception)
        {
            int dp=48;
            float density=context.getResources().getDisplayMetrics().density;
            sNaviBarHeight=Math.round(density*dp);
            exception.printStackTrace();
        }
        return sNaviBarHeight;
    }
    
    public static int getVerticalNavigationBarHeight(Context context)
    {
        if(sVerticalNaviBarHeight>0)
        {
            return sVerticalNaviBarHeight;
        }
        try
        {
            Class<?> c=Class.forName("com.android.internal.R$dimen");
            Object o=c.newInstance();
            Field field=c.getField("navigation_bar_width");
            int x=(Integer)field.get(o);
            sVerticalNaviBarHeight=context.getResources().getDimensionPixelSize(x);
        }
        catch(Exception exception)
        {
            int dp=42;
            float density=context.getResources().getDisplayMetrics().density;
            sVerticalNaviBarHeight=Math.round(density*dp);
            exception.printStackTrace();
        }
        return sVerticalNaviBarHeight;
    }
    
    public static boolean isResolutionHigherThanQHD(int width,int height)
    {
        return Math.max(width,height)>=960&&Math.min(width,height)>=540;
    }
    
    public static boolean isResolutionHigherThanWVGA(int width,int height)
    {
        return Math.max(width,height)>=800&&Math.min(width,height)>=480;
    }
    
    public static boolean needDrawFakeStatusBarBgOnWindow()
    {
        return sNeedDrawFakeStatusBarBgOnWindow;
    }
    
    public static void setNeedDrawFakeStatusBarBgOnWindow(boolean b)
    {
        sNeedDrawFakeStatusBarBgOnWindow=b;
    }
    
    public static void setNeedDrawFakeStatusBarBgOnFullScreen(boolean b)
    {
        sNeedDrawFakeStatusBarBgOnFullScreen=b;
    }
    
    public static boolean needDrawFakeStatusBarBgOnFullScreen()
    {
        return sNeedDrawFakeStatusBarBgOnFullScreen;
    }
    
    /** 系统通知栏是否隐藏 */
    public static boolean isStatusBarHidden()
    {
        return gIsStatusBarHidden;
    }
    
    public static void setStatusBarHidden(boolean isStatusBarHidden)
    {
        gIsStatusBarHidden=isStatusBarHidden;
    }
    
    public static boolean isSystemFullScreen(Activity activity)
    {
        if(activity==null)
        {
            return false;
        }
        boolean isSystemFullscreen=false;
        Window window=activity.getWindow();
        if(window!=null&&window.getAttributes()!=null)
        {
            int flag=window.getAttributes().flags;
            if((flag&LayoutParams.FLAG_FULLSCREEN)==LayoutParams.FLAG_FULLSCREEN)
            {
                isSystemFullscreen=true;
            }
            else if((flag&LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)==LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            {
                isSystemFullscreen=false;
            }
        }
        return isSystemFullscreen;
    }
    
    public static Bitmap getInstalledAppIcon(String packageName)
    {
        checkIfContextInitialized();
        return getInstalledAppIcon(sContext,packageName);
    }
    
    public static boolean installAPKFile(String filePath)
    {
        checkIfContextInitialized();
        return installApkFile(sContext,filePath);
    }
    
    public static boolean uninstallPackage(String packageName)
    {
        checkIfContextInitialized();
        return uninstallPackage(sContext,packageName);
    }
    
    /** Android5.0手机获取活动的进程 */
    public static String[] getTopProcess()
    {
        int PROCESS_STATE_TOP=2;
        try
        {
            Field processStateField=RunningAppProcessInfo.class.getDeclaredField("processState");
            ActivityManager manager=(ActivityManager)Foundation.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> processes=manager.getRunningAppProcesses();
            for(RunningAppProcessInfo process:processes)
            {
                if(process.importance<=RunningAppProcessInfo.IMPORTANCE_FOREGROUND&&process.importanceReasonCode==0)
                {
                    int state=processStateField.getInt(process);
                    if(state==PROCESS_STATE_TOP)
                    {
                        return process.pkgList;
                    }
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return new String[]{};
    }
    
    public static int currentTimeSeconds()
    {
        return (int)(System.currentTimeMillis()/1000);
    }
    
    public static long getTodayTimeMillis()
    {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTimeInMillis();
    }
    
    public static String getSystemCountry()
    {
        return Locale.getDefault().getCountry();
    }
    
    public static String getSystemLanguage()
    {
        return Locale.getDefault().getLanguage();
    }
    
    /**
     * 背景图片格式:JPEG PNG
     * 设置背景成功之后会收到ACTIONxWALLPAPERxCHANGED广播
     * 需要SETxWALLPAPER权限
     *
     * @return
     */
    static boolean setSystemWallpaper(Context context,String wallpaperPath)
    {
        FileInputStream fis=null;
        try
        {
            fis=new FileInputStream(wallpaperPath);
            WallpaperManager.getInstance(context).setStream(fis);
            fis.close();
            return true;
        }
        catch(Exception exceptionOne)
        {
            exceptionOne.printStackTrace();
            try
            {
                if(fis!=null)
                {
                    fis.close();
                }
            }
            catch(Exception exceptionTwo)
            {
                exceptionTwo.printStackTrace();
            }
        }
        return false;
    }
    
    public static boolean isAndroidOVersionOrAbove()
    {
        return VERSION.SDK_INT>=26||"O".equalsIgnoreCase(VERSION.RELEASE);
    }
    
    /** 判断当前系统版本是否是4.0以上 */
    public static boolean isRomMainVersionAtLeast4()
    {
        if(sHasCheckedIsRomMainVersionAtLeast4)
        {
            return sIsRomMainVersionAtLeast4;
        }
        boolean isOSVersionValid=Character.getNumericValue(VERSION.RELEASE.trim().charAt(0))>=4;
        int SDKVersion=VERSION.SDK_INT;
        if(!isOSVersionValid||SDKVersion<14)
        {
            sIsRomMainVersionAtLeast4=false;
        }
        else if(SDKVersion<20)
        {
            /** 判断android.view.DisplayList是否存在.不支持AC的android版本是没有这个类的 */
            Class<?> displayListClass=null;
            try
            {
                displayListClass=Class.forName("android.view.DisplayList");
            }
            catch(Throwable throwable)
            {}
            if(displayListClass==null)
            {
                sIsRomMainVersionAtLeast4=false;
            }
        }
        sHasCheckedIsRomMainVersionAtLeast4=true;
        return sIsRomMainVersionAtLeast4;
    }
    
    public static boolean isMeiZuMXSeries()
    {
        if(sHasCheckedMeizuMXSeries)
        {
            return sIsMeizuMXSeries;
        }
        if(Build.DISPLAY.contains("Flyme"))
        {
            String model=Build.MODEL;
            for(String knownModel:MEIZUxSMARTBARxDEVICExLIST)
            {
                if(knownModel.equals(model))
                {
                    sIsMeizuMXSeries=true;
                    break;
                }
            }
            if(!sIsMeizuMXSeries)
            {
                try
                {
                    Class<?> buildClass=Build.class;
                    Method hasSmartBarMethod=buildClass.getMethod("hasSmartBar");
                    sIsMeizuMXSeries=((Boolean)hasSmartBarMethod.invoke(null)).booleanValue();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
        sHasCheckedMeizuMXSeries=true;
        return sIsMeizuMXSeries;
    }
    
    private static void checkMeiZuFlyMeVersion()
    {
        if(Build.DISPLAY.contains("Flyme"))
        {
            if(VERSION.SDK_INT==SDKxINTxMEIZUxFLYMEx2)
            {
                sIsMeizuFlyme2=true;
            }
            else if(VERSION.SDK_INT==SDKxINTxMEIZUxFLYMEx3)
            {
                sIsMeizuFlyme3=true;
            }
        }
    }
    
    static boolean isMeiZuFlyMeTwo()
    {
        if(!sHasCheckedMeizuFlyemeVersion)
        {
            checkMeiZuFlyMeVersion();
            sHasCheckedMeizuFlyemeVersion=true;
        }
        return sIsMeizuFlyme2;
    }
    
    public static boolean isMeiZuFlyMeThree()
    {
        if(!sHasCheckedMeizuFlyemeVersion)
        {
            checkMeiZuFlyMeVersion();
            sHasCheckedMeizuFlyemeVersion=true;
        }
        return sIsMeizuFlyme3;
    }
    
    /** 是否是小米手机 */
    public static boolean isMIPhone()
    {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }
    
    /** 仅用Build.MANUFACTURER判断有部分机型(红米)不一定可以生效.增加这个以备无患 */
    public static boolean isMIBrand()
    {
        return "Xiaomi".equals(Build.BRAND);
    }
    
    /** 是否是锤子手机 */
    public static boolean isSmartisanBrand()
    {
        return StringUtil.equalsIgnoreCase("smartisan",Build.BRAND);
    }
    
    public static boolean isMotoGBrand()
    {
        return "motorola".equalsIgnoreCase(Build.BRAND)&&Build.MODEL.contains("MotoE2")&&VERSION.SDK_INT>=21;
    }
    
    public static boolean isHonor10()
    {
        return "HUAWEI".equalsIgnoreCase(Build.MANUFACTURER)&&"HONOR".equalsIgnoreCase(Build.BRAND)&&"COL-AL10".equalsIgnoreCase(Build.MODEL);
    }
    
    public static boolean isMiUIV6orAbove()
    {
        try
        {
            String KEY_MIUI_VERSION_NAME="ro.miui.ui.version.name";
            if(mBuildProperties==null)
            {
                mBuildProperties=BuildProperties.newInstance();
            }
            String name=mBuildProperties.getProperty(KEY_MIUI_VERSION_NAME,"");
            return isMiUiVersionNameAbove(6,name);
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
            return false;
        }
    }
    
    public static boolean isMiUIV7OrAbove()
    {
        try
        {
            String KEY_MIUI_VERSION_NAME="ro.miui.ui.version.name";
            if(mBuildProperties==null)
            {
                mBuildProperties=BuildProperties.newInstance();
            }
            String name=mBuildProperties.getProperty(KEY_MIUI_VERSION_NAME,"");
            return isMiUiVersionNameAbove(7,name);
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
            return false;
        }
    }
    
    public static boolean isMiUIVnumOrAbove(int num)
    {
        try
        {
            String KEY_MIUI_VERSION_NAME="ro.miui.ui.version.name";
            if(mBuildProperties==null)
            {
                mBuildProperties=BuildProperties.newInstance();
            }
            String name=mBuildProperties.getProperty(KEY_MIUI_VERSION_NAME,"");
            return isMiUiVersionNameAbove(num,name);
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
            return false;
        }
    }
    
    private static boolean isMiUiVersionNameAbove(int miuiVersionNum,String miuiVersionName)
    {
        boolean result=false;
        if(miuiVersionName==null)
        {
            return result;
        }
        miuiVersionName=miuiVersionName.toLowerCase();
        if(miuiVersionName.contains("v"))
        {
            String[] params=miuiVersionName.split("v");
            if(params.length<2||TextUtils.isEmpty(params[1]))
            {
                return result;
            }
            int miuiVersion=StringUtil.parseInt(params[1]);
            if(miuiVersion>=miuiVersionNum)
            {
                result=true;
            }
        }
        return result;
    }
    
    public static boolean isMIUISystemV5OrAbove()
    {
        if(sHasCheckedIfMIUISystemV5OrAbove)
        {
            return sIsMIUISystemV5OrAbove;
        }
        String version="";
        try
        {
            Class<?> classType=Class.forName("android.os.SystemProperties");
            Method getMethod=classType.getDeclaredMethod("get",String.class);
            version=(String)getMethod.invoke(classType,new Object[]{"ro.miui.ui.version.name"});
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        sIsMIUISystemV5OrAbove=!TextUtils.isEmpty(version);
        sHasCheckedIfMIUISystemV5OrAbove=true;
        if(DEBUG)
        {
            Logger.i(TAG,"isMIUISystemV5OrAbove: "+sIsMIUISystemV5OrAbove);
        }
        return sIsMIUISystemV5OrAbove;
    }
    
    public static boolean isMiUIROM(Context context)
    {
        boolean ret=false;
        try
        {
            Class<?> clazz=context.getClassLoader().loadClass("android.os.SystemProperties");
            Method get=clazz.getMethod("get",String.class);
            String versionName=(String)get.invoke(clazz,new Object[]{"ro.miui.ui.version.name"});
            if(!TextUtils.isEmpty(versionName))
            {
                ret=true;
            }
        }
        catch(Throwable throwable)
        {
            ret=false;
        }
        return ret;
    }
    
    public static boolean isSupportXMSF(Context context)
    {
        boolean ret=false;
        try
        {
            PackageInfo pkgInfo=context.getPackageManager().getPackageInfo("com.xiaomi.xmsf",PackageManager.GET_SERVICES);
            if(pkgInfo!=null&&pkgInfo.versionCode>=105)
            {
                ret=true;
            }
        }
        catch(Throwable throwable)
        {
            ret=false;
        }
        return ret;
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
    
    static String getClipboardText(Context context)
    {
        String text="";
        try
        {
            if(context!=null)
            {
                ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(null!=clipboardManager&&clipboardManager.hasText())
                {
                    text+=clipboardManager.getText();
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return text;
    }
    
    static void setClipboardText(Context context,String text)
    {
        if(context!=null&&text!=null)
        {
            ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            if(null!=clipboardManager)
            {
                try
                {
                    ClipData clipData=ClipData.newPlainText(null,text);
                    clipboardManager.setPrimaryClip(clipData);
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    public static int getSystemBrightness(Context context)
    {
        int brightness=0;
        ContentResolver contentResolver=context.getContentResolver();
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
    
    public static float getBrightnessMax()
    {
        try
        {
            if(sBrightnessLocalMaxValue>0)
            {
                return sBrightnessLocalMaxValue;
            }
            Resources system=Resources.getSystem();
            int resId=system.getIdentifier("config_screenBrightnessSettingMaximum","integer","android");
            if(resId!=0)
            {
                sBrightnessLocalMaxValue=system.getInteger(resId);
                return sBrightnessLocalMaxValue<=0?BRIGHTNESSxMAXxVALUE:sBrightnessLocalMaxValue;
            }
        }
        catch(Exception exception)
        {
            AssertUtil.fail(exception);
        }
        return BRIGHTNESSxMAXxVALUE;
    }
    
    public static float getWindowBrightness(Activity activity)
    {
        if(activity==null||activity.getWindow()==null)
        {
            return 0;
        }
        return activity.getWindow().getAttributes().screenBrightness*getBrightnessMax();
    }
    
    public static void setBrightness(Activity activity,int brightness)
    {
        setBrightness(activity,(float)brightness/getBrightnessMax());
    }
    
    /** 由于屏幕亮度值小于一时可能会引起屏幕关闭.输入参数在0~1.测试为4.x机型上的固件问题.对此进行适配 */
    public static boolean setBrightness(Activity activity,float brightness)
    {
        boolean result=false;
        try
        {
            Window window=activity.getWindow();
            if(window==null)
            {
                return false;
            }
            LayoutParams layoutParams=window.getAttributes();
            /** 小于零时表示要跟随系统亮度 */
            if(brightness<0)
            {
                layoutParams.screenBrightness=LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            else
            {
                if(brightness>1)
                {
                    brightness=1;
                }
                layoutParams.screenBrightness=brightness;
            }
            window.setAttributes(layoutParams);
            result=true;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return result;
    }
    
    static Bitmap getInstalledAppIcon(Context context,String packageName)
    {
        if(context==null||packageName==null||"".equals(packageName.trim()))
        {
            return null;
        }
        PackageManager packageManager=context.getPackageManager();
        try
        {
            Drawable drawable=packageManager.getApplicationIcon(packageName);
            if(drawable instanceof BitmapDrawable)
            {
                return ((BitmapDrawable)drawable).getBitmap();
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return null;
    }
    
    static boolean installApkFile(Context context,String filePath)
    {
        if(context==null||filePath==null||"".equals(filePath.trim()))
        {
            return false;
        }
        try
        {
            File apkFile=new File(filePath);
            if(!apkFile.exists())
            {
                return false;
            }
            Intent i=new Intent();
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
            context.startActivity(i);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
    
    static boolean uninstallPackage(Context context,String packageName)
    {
        if(context==null||packageName==null||"".equals(packageName.trim()))
        {
            return false;
        }
        try
        {
            Uri packageURI=Uri.parse("package:"+packageName);
            Intent i=new Intent(Intent.ACTION_DELETE,packageURI);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
    
    private static int getWindowWidth(Context context)
    {
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth=Math.min(displayMetrics.widthPixels,displayMetrics.heightPixels);
        return deviceWidth;
    }
    
    public static boolean checkSystemVersionSatisfied(int lowestVersion)
    {
        return VERSION.SDK_INT>=lowestVersion;
    }
    
    public static ButtonOrder getButtonOrder()
    {
        if(VERSION.SDK_INT<11)
        {
            return ButtonOrder.LEFTxTOxRIGHT;
        }
        else
        {
            return ButtonOrder.RIGHTxTOxLEFT;
        }
    }
    
    public static boolean isButtonOrderLeftToRight()
    {
        return getButtonOrder()==ButtonOrder.LEFTxTOxRIGHT;
    }
    
    public static boolean checkResolutionHigherThanQHD(int width,int height)
    {
        return Math.max(width,height)>=960&&Math.min(width,height)>=540;
    }
    
    public static void cancelGC()
    {
        createGCHandler();
        sGcHandler.removeCallbacks(sGcRunnable);
    }
    
    public static void createGCHandler()
    {
        if(sGcHandler==null)
        {
            sGcHandler=new HandlerEx(SystemUtil.class.getName()+218,ThreadManager.getBackgroundLooper());
        }
    }
    
    /** 获取键盘高度 */
    public static final int getIMEHeight(Context context)
    {
        View root=((Activity)context).getWindow().getDecorView();
        if(root==null)
        {
            return -1;
        }
        Rect rect=new Rect();
        root.getWindowVisibleDisplayFrame(rect);
        return root.getHeight()-rect.height();
    }
    
    public static void updateMultiMedia(Activity activity,String path,boolean isPicture)
    {
        if(activity==null||path==null||path.trim().length()==0)
        {
            return;
        }
        String prefix="file://";
        String separator="/";
        String updatePath;
        if(path.startsWith(prefix))
        {
            updatePath=path;
        }
        else
        {
            if(path.startsWith(separator))
            {
                updatePath=prefix+path;
            }
            else
            {
                updatePath=prefix+separator+path;
            }
        }
        if(isPicture)
        {
            ContentValues values=new ContentValues(2);
            values.put(Video.Media.MIME_TYPE,"image/*");
            values.put(Video.Media.DATA,path);
            values.put(Video.Media.DATE_ADDED,System.currentTimeMillis()/1000);
            values.put(Video.Media.DATE_MODIFIED,System.currentTimeMillis()/1000);
            activity.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,values);
        }
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse(updatePath)));
    }
    
    public static String getSnapshotFolder()
    {
        String snapshotFolder=PathManager.getRootDir()+"Screenshot/tmp/";
        return snapshotFolder;
    }
    
    /** 获取临时解压缩目录 */
    public static final String getDecompressTempFolder()
    {
        return PathManager.getRootDir()+".Decompress/tmp/";
    }
    
    /** 获取自定义头像剪切头像的存放位置 */
    public static final String getAccountCropImageFolder()
    {
        return PathManager.getRootDir()+".CropImage/";
    }
    
    public static String getSnapshotPath()
    {
        String snapshotFolder=getSnapshotFolder();
        String snapshotName=SNAPSHOTxPREFIX+System.currentTimeMillis()+".jpg";
        String snapshotPath=snapshotFolder+snapshotName;
        return snapshotPath;
    }
    
    public static void deleteSnapshotTempPicture(String currentSnapPath)
    {
        if(currentSnapPath==null)
        {
            return;
        }
        File file=new File(currentSnapPath);
        final String snapshotName=file.getName();
        final String snapshotFolder=getSnapshotFolder();
        if(!currentSnapPath.startsWith(snapshotFolder))
        {
            return;
        }
        ThreadManager.post(ThreadManager.THREADxWORK,new Runnable()
        {
            @Override
            public void run()
            {
                FileFilter filter=new FileFilter()
                {
                    @Override
                    public boolean accept(File file)
                    {
                        String filename=file.getName();
                        return filename!=null&&filename.startsWith(SNAPSHOTxPREFIX)&&!filename.equalsIgnoreCase(snapshotName);
                    }
                };
                File filedir=new File(snapshotFolder);
                File[] fileList=filedir.listFiles(filter);
                if(fileList!=null&&fileList.length>0)
                {
                    for(int i=0;i<fileList.length;i++)
                    {
                        fileList[i].delete();
                    }
                }
            }
        });
    }
    
    public static String storePageSnapshot(Bitmap bitmap)
    {
        return storePageSnapshot(bitmap,true);
    }
    
    public static String storePageSnapshot(Bitmap bitmap,String tempsnapshotPath)
    {
        return storePageSnapshot(bitmap,tempsnapshotPath,true);
    }
    
    public static String storePageSnapshot(Bitmap bitmap,boolean deleteOther)
    {
        return storePageSnapshot(bitmap,null,deleteOther);
    }
    
    public static String storePageSnapshot(Bitmap bitmap,String tempsnapshotPath,boolean deleteOther)
    {
        if(bitmap==null)
        {
            return null;
        }
        String snapshotPath=StringUtil.isEmpty(tempsnapshotPath)?getSnapshotPath():tempsnapshotPath;
        String snapshotFolder=getSnapshotFolder();
        if(deleteOther)
        {
            deleteSnapshotTempPicture(snapshotPath);
        }
        boolean success=false;
        FileOutputStream out=null;
        try
        {
            File folder=new File(snapshotFolder);
            if(!folder.exists())
            {
                folder.mkdirs();
            }
            File file=new File(snapshotPath);
            out=new FileOutputStream(file);
            if(bitmap.compress(CompressFormat.JPEG,100,out))
            {
                out.flush();
                success=true;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(out);
        }
        if(!success)
        {
            snapshotPath=null;
        }
        return snapshotPath;
    }
    
    public static void includeStoppedPackages(Intent intent)
    {
        if(VERSION.SDK_INT>=12&&intent!=null)
        {
            intent.addFlags(FLAGxINCLUDExSTOPPEDxPACKAGES);
        }
    }
    
    public static boolean IsACWindowManager()
    {
        return mIsACWindowManager;
    }
    
    public static void setACWindowManager(boolean isACWindowManager)
    {
        mIsACWindowManager=isACWindowManager;
    }
    
    /** 此方法仅用于显示有用的崩溃堆栈 */
    public static void debug()
    {
        sUnusedIndex++;
        if(sUnusedIndex==10)
        {
            sUnusedIndex=0;
        }
    }
    
    private static byte getRunningStateAfter21ApiLevel(Context context)
    {
        byte result=APPxSTATExUNACTIVE;
        if(null==context)
        {
            if(null==sContext)
            {
                return APPxSTATExUNACTIVE;
            }
            context=sContext;
        }
        boolean needJudgeFgOrBg=false;
        ActivityManager am=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am!=null)
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<Object> appTasks=(List<Object>)ReflectionHelper.invokeMethod(am,"getAppTasks",null,null);
                if(appTasks!=null&&appTasks.size()>0)
                {
                    RecentTaskInfo appRecentTaskInfo=null;
                    for(Object appTask:appTasks)
                    {
                        appRecentTaskInfo=(RecentTaskInfo)ReflectionHelper.invokeMethod(appTask,"getTaskInfo",null,null);
                        if(appRecentTaskInfo==null||!appRecentTaskInfo.baseIntent.getComponent().getPackageName().equals(context.getPackageName()))
                        {
                            continue;
                        }
                        else if(appRecentTaskInfo.id!=-1)
                        {
                            needJudgeFgOrBg=true;
                            result=APPxSTATExBG;
                            break;
                        }
                        else
                        {
                            result=APPxSTATExUNACTIVE;
                            break;
                        }
                    }
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        if(am!=null&&needJudgeFgOrBg)
        {
            List<RunningTaskInfo> runningTaskInfos=am.getRunningTasks(1);
            if(null!=runningTaskInfos&&runningTaskInfos.size()>0)
            {
                RunningTaskInfo runningTaskInfo=runningTaskInfos.get(0);
                ComponentName cn=runningTaskInfo.topActivity;
                ComponentName bn=runningTaskInfo.baseActivity;
                if(cn!=null&&bn!=null)
                {
                    String cnPackageName=cn.getPackageName();
                    if((StringUtil.isNotEmpty(cnPackageName)&&cnPackageName.equals(context.getPackageName())))
                    {
                        result=APPxSTATExFG;
                    }
                    else
                    {
                        result=APPxSTATExBG;
                    }
                }
            }
        }
        return result;
    }
    
    public static boolean isQiKU()
    {
        return TextUtils.equals("QiKU",Build.BRAND);
    }
    
    public static boolean hasAppOps(Context context)
    {
        return context.getSystemService("appops")!=null;
    }
    
    public static boolean checkAppOps(Context context,String opField)
    {
        try
        {
            Object mAppOps=context.getSystemService("appops");
            ApplicationInfo appInfo=context.getApplicationInfo();
            String pkg=context.getApplicationContext().getPackageName();
            int uid=appInfo.uid;
            Class appOpsClass=Class.forName("android.app.AppOpsManager");
            int value=ReflectionHelper.getIntFieldValue(appOpsClass,opField);
            Object object=ReflectionHelper.invokeMethod(mAppOps,"checkOpNoThrow",new Class[]{int.class,int.class,String.class},new Object[]{value,uid,pkg});
            return object!=null&&(Integer)object==0;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return false;
    }
    
    public static SimpleDateFormat getSimpleDateFormat(String format)
    {
        if(!ThreadManager.isMainThread())
        {
            return new SimpleDateFormat(format);
        }
        SimpleDateFormat sdf=mSimpleDateFormatCache.get(format);
        if(sdf==null)
        {
            sdf=new SimpleDateFormat(format);
            mSimpleDateFormatCache.put(format,sdf);
        }
        return sdf;
    }
    
    /** 系统通知开关状态 */
    public static boolean isSystemNotificationEnable(Context context)
    {
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }
    
    public enum ButtonOrder
    {
        LEFTxTOxRIGHT,RIGHTxTOxLEFT
    }
    private static class BuildProperties
    {
        private final Properties properties;
        
        private BuildProperties() throws IOException
        {
            properties=new Properties();
            FileInputStream is=new FileInputStream(new File(Environment.getRootDirectory(),"build.prop"));
            properties.load(is);
            IOUtil.safeClose(is);
        }
        
        public static BuildProperties newInstance() throws IOException
        {
            return new BuildProperties();
        }
        
        public boolean containsKey(Object key)
        {
            return properties.containsKey(key);
        }
        
        public boolean containsValue(Object value)
        {
            return properties.containsValue(value);
        }
        
        public Set<Entry<Object,Object>> entrySet()
        {
            return properties.entrySet();
        }
        
        public String getProperty(String name)
        {
            return properties.getProperty(name);
        }
        
        public String getProperty(String name,String defaultValue)
        {
            return properties.getProperty(name,defaultValue);
        }
        
        public boolean isEmpty()
        {
            return properties.isEmpty();
        }
        
        public Enumeration<Object> keys()
        {
            return properties.keys();
        }
        
        public Set<Object> keySet()
        {
            return properties.keySet();
        }
        
        public int size()
        {
            return properties.size();
        }
        
        public Collection<Object> values()
        {
            return properties.values();
        }
    }
    static class GCRunnable implements Runnable
    {
        @Override
        public void run()
        {
            System.gc();
        }
    }
}
