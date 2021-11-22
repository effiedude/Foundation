package com.townspriter.base.foundation.utils.device;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.microedition.khronos.opengles.GL10;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.os.SystemProperties;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Window;

/******************************************************************************
 * @path Foundation:DeviceUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class DeviceUtil
{
    private static final String TAG="DeviceUtil";
    private static final boolean DEBUG=false;
    private static boolean sHasInitedAndroidId;
    private static String sAndroidId="";
    private static boolean sHasInitIMEI;
    private static String sIMEI="";
    private static boolean sHasInitMacAddress;
    private static String sMacAddress="";
    
    public static String getAndroidID()
    {
        if(sHasInitedAndroidId)
        {
            return sAndroidId;
        }
        try
        {
            sAndroidId=Secure.getString(Foundation.getApplication().getContentResolver(),Secure.ANDROID_ID);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(sAndroidId==null)
        {
            sAndroidId="";
        }
        sHasInitedAndroidId=true;
        if(DEBUG)
        {
            Logger.d(TAG,"getAndroidID:"+sAndroidId);
        }
        return sAndroidId;
    }
    
    public static String getIMEI()
    {
        if(sHasInitIMEI)
        {
            return sIMEI;
        }
        sIMEI=getIMEIDirect();
        /** 未授权的时候获取不到下次还要重试 */
        if(!StringUtil.isEmpty(sIMEI))
        {
            sHasInitIMEI=true;
        }
        if(DEBUG)
        {
            Logger.d(TAG,"getIMEI:"+sIMEI);
        }
        return sIMEI;
    }
    
    private static String getIMEIDirect()
    {
        String imei=null;
        try
        {
            TelephonyManager telephonyMgr=(TelephonyManager)Foundation.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            if(telephonyMgr!=null)
            {
                imei=telephonyMgr.getDeviceId();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return imei;
    }
    
    /**
     * 获取国际移动用户识别码
     * 由于此识别码一般依赖手机卡.所以会发生变化(不允许缓存)
     */
    public static String getIMSI()
    {
        String imsi=null;
        try
        {
            TelephonyManager telephonyMgr=(TelephonyManager)Foundation.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            if(null!=telephonyMgr)
            {
                imsi=telephonyMgr.getSubscriberId();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return imsi;
    }
    
    /** 获取支持的最大纹理尺寸 */
    public static int getGLMaxTextureSize()
    {
        int[] maxTextureSize=new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE,maxTextureSize,0);
        return maxTextureSize[0];
    }
    
    /** 获取系统是否已经破解了 */
    public static boolean hasRoot()
    {
        File file=new File("system/bin/su");
        if(file.exists())
        {
            return true;
        }
        file=new File("system/xbin/su");
        return file.exists();
    }
    
    public static WakeLock acquireCPUWakeLock(Context context,WakeLock wakeLock,String wakeLockTag,boolean refCounted)
    {
        return acquireCPUWakeLock(context,wakeLock,wakeLockTag,refCounted,0);
    }
    
    /**
     * 获取唤醒锁
     * 如果传入的唤醒锁参数为空则会创建一个并尝试获取锁.返回创建的对象
     * 如果传入的唤醒锁参数不为空.则尝试获取锁.并返回传入的唤醒锁参数
     * 注意:外部调用需要自己加同步锁防止创建多个对象
     */
    public static WakeLock acquireCPUWakeLock(Context context,WakeLock wakeLock,String wakeLockTag,boolean refCounted,long timeout)
    {
        if(wakeLock==null)
        {
            try
            {
                PowerManager powerManager=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
                wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,wakeLockTag);
                wakeLock.setReferenceCounted(refCounted);
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        if(wakeLock!=null)
        {
            if(DEBUG)
            {
                Logger.d(TAG,"acquireCPUWakeLock-wakeLockTag:"+wakeLockTag);
            }
            try
            {
                if(timeout>0)
                {
                    wakeLock.acquire(timeout);
                }
                else
                {
                    wakeLock.acquire();
                }
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        return wakeLock;
    }
    
    public static void releaseCPUWakeLock(WakeLock wakeLock,String wakeLockTag)
    {
        if(wakeLock!=null&&wakeLock.isHeld())
        {
            if(DEBUG)
            {
                Logger.d(TAG,"releaseCPUWakeLock-wakeLockTag:"+wakeLockTag);
            }
            try
            {
                wakeLock.release();
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
    
    public static WakeLock acquireScreenWakeLock(Context context,WakeLock wakeLock,String wakeLockTag)
    {
        if(wakeLock==null)
        {
            try
            {
                PowerManager pm=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
                wakeLock=pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE,wakeLockTag);
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        if(wakeLock!=null)
        {
            if(DEBUG)
            {
                Logger.d(TAG,"acquireScreenWakeLock-wakeLockTag:"+wakeLockTag);
            }
            try
            {
                wakeLock.acquire();
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        return wakeLock;
    }
    
    public static void releaseScreenWakeLock(WakeLock wakeLock,String wakeLockTag)
    {
        if(wakeLock!=null&&wakeLock.isHeld())
        {
            if(DEBUG)
            {
                Logger.v(TAG,"releaseScreenWakeLock-wakeLockTag:"+wakeLockTag);
            }
            try
            {
                wakeLock.release();
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
    
    public static String getMACAddress()
    {
        if(sHasInitMacAddress)
        {
            return sMacAddress;
        }
        if(VERSION.SDK_INT>=VERSION_CODES.M)
        {
            try
            {
                Enumeration<NetworkInterface> interfaces=NetworkInterface.getNetworkInterfaces();
                if(interfaces==null)
                {
                    return "";
                }
                String wifiInterface=SystemProperties.get("wifi.interface","wlan0");
                while(interfaces.hasMoreElements())
                {
                    NetworkInterface iF=interfaces.nextElement();
                    byte[] addr=iF.getHardwareAddress();
                    if(addr==null||addr.length==0)
                    {
                        continue;
                    }
                    String name=iF.getName();
                    if(!wifiInterface.equalsIgnoreCase(name))
                    {
                        continue;
                    }
                    StringBuilder buf=new StringBuilder();
                    for(byte b:addr)
                    {
                        buf.append(String.format("%02X:",b));
                    }
                    if(buf.length()>0)
                    {
                        buf.deleteCharAt(buf.length()-1);
                    }
                    String mac=buf.toString();
                    if(!StringUtil.isEmpty(mac))
                    {
                        sMacAddress=mac;
                        sHasInitMacAddress=true;
                        break;
                    }
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        if(StringUtil.isEmpty(sMacAddress))
        {
            try
            {
                WifiManager wifi=(WifiManager)Foundation.getApplication().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info=wifi.getConnectionInfo();
                sMacAddress=info.getMacAddress();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        if(sMacAddress==null)
        {
            sMacAddress="";
        }
        else if(!TextUtils.isEmpty(sMacAddress))
        {
            sHasInitMacAddress=true;
        }
        return sMacAddress;
    }
    
    public static boolean isXiaoMi()
    {
        try
        {
            return Build.BRAND.equalsIgnoreCase("xiaomi");
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            return false;
        }
    }
    
    @SuppressLint("PrivateApi")
    public static boolean setStatusBarFontForMIUI(Window window,boolean dark)
    {
        boolean result=false;
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
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return result;
    }
}
