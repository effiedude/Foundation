package com.townspriter.base.foundation.utils.device;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.os.Build;

/******************************************************************************
 * @path Foundation:NotchScreenUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class NotchScreenUtil
{
    private static final String ROMxOPPO="com.oppo.feature.screen.heteromorphism";
    private static final String ROMxVIVO="android.util.FtFeature";
    private static final String ROMxHUAWEI="com.huawei.android.util.HwNotchSizeUtil";
    private static final String ROMxMIUI="android.os.SystemProperties";
    private static boolean sInit;
    private static boolean sHasNotchScreen;
    
    /** 设备是否是刘海屏 */
    public static boolean hasNotchScreen(Context activity)
    {
        if(sInit)
        {
            return sHasNotchScreen;
        }
        sHasNotchScreen=hasNotchInOPPO(activity)||hasNotchInVIVO(activity)||hasNotchInHuaWei(activity)||hasNotchInMIUI(activity);
        sInit=true;
        return sHasNotchScreen;
    }
    
    private static boolean hasNotchInOPPO(Context context)
    {
        if(context==null)
        {
            return false;
        }
        return context.getPackageManager().hasSystemFeature(ROMxOPPO);
    }
    
    private static boolean hasNotchInVIVO(Context context)
    {
        if(context==null)
        {
            return false;
        }
        boolean hasNotch=false;
        try
        {
            ClassLoader classLoader=context.getClassLoader();
            Class ftFeature=classLoader.loadClass(ROMxVIVO);
            Method[] methods=ftFeature.getDeclaredMethods();
            if(methods!=null)
            {
                for(int i=0;i<methods.length;i++)
                {
                    Method method=methods[i];
                    if(method.getName().equalsIgnoreCase("isFeatureSupport"))
                    {
                        hasNotch=(boolean)method.invoke(ftFeature,0x00000020);
                        break;
                    }
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            hasNotch=false;
        }
        return hasNotch;
    }
    
    private static boolean hasNotchInHuaWei(Context context)
    {
        if(context==null)
        {
            return false;
        }
        boolean hasNotch=false;
        try
        {
            ClassLoader classLoader=context.getClassLoader();
            Class HwNotchSizeUtil=classLoader.loadClass(ROMxHUAWEI);
            Method get=HwNotchSizeUtil.getMethod("hasNotchInScreen");
            hasNotch=(boolean)get.invoke(HwNotchSizeUtil);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return hasNotch;
    }
    
    private static boolean hasNotchInMIUI(Context activity)
    {
        if(activity==null)
        {
            return false;
        }
        if(!isXiaoMi())
        {
            return false;
        }
        int result=0;
        try
        {
            ClassLoader classLoader=activity.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties=classLoader.loadClass(ROMxMIUI);
            /** 参数类型 */
            @SuppressWarnings("rawtypes")
            Class[] paramTypes=new Class[2];
            paramTypes[0]=String.class;
            paramTypes[1]=int.class;
            Method getInt=SystemProperties.getMethod("getInt",paramTypes);
            /** 参数 */
            Object[] params=new Object[2];
            params[0]="ro.miui.notch";
            params[1]=new Integer(0);
            result=(Integer)getInt.invoke(SystemProperties,params);
        }
        catch(ClassNotFoundException classNotFoundException)
        {
            classNotFoundException.printStackTrace();
        }
        catch(NoSuchMethodException noSuchMethodException)
        {
            noSuchMethodException.printStackTrace();
        }
        catch(IllegalAccessException illegalAccessException)
        {
            illegalAccessException.printStackTrace();
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
            illegalArgumentException.printStackTrace();
        }
        catch(InvocationTargetException invocationTargetException)
        {
            invocationTargetException.printStackTrace();
        }
        return result==1;
    }
    
    private static boolean isXiaoMi()
    {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }
}
