package com.townspriter.base.foundation.utils.system;

import android.app.Activity;
import android.content.Context;

/******************************************************************************
 * @path ISystemInfo
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface ISystemInfo
{
    void initialize(Context context);
    
    float getDensity();
    
    int getDensityDpi();
    
    int getDeviceWidth(Context context);
    
    int getDeviceHeight(Context context);
    
    String getIMEI();
    
    String getCpuArch();
    
    String getAndroidId();
    
    /**
     * 获取屏幕宽度
     * 
     * @param context
     * 建议传当前环境上下文或为空.为空取整个应用的环境上下文.某些情况下(折叠屏或屏幕尺寸变化)全局环境上下文获取到的不是真实值
     * @return
     */
    int getScreenWidth(Context context);
    
    /**
     * 获取屏幕高度
     * 
     * @param context
     * 建议传当前环境上下文或为空.为空取整个应用的环境上下文.某些情况下(折叠屏或屏幕尺寸变化)全局环境上下文获取到的不是真实值
     * @return
     */
    int getScreenHeight(Context context);
    
    int getStatusBarHeight();
    
    /**
     * @param activity
     * @param includeNavBar
     * 是否包含底部虚拟导航栏
     * @return
     */
    int getScreenRealHeight(Activity activity,boolean includeNavBar);
    
    String getIMSI();
    
    String getSMSNo();
    
    String getMacAddress();
    
    String getRomInfo();
    
    String getCpuInfoArch();
    
    String getCpuInfoVfp();
    
    int getCurrAccessPointType();
    
    String getCpuInfoArchit();
    
    int getCpuCoreCount();
    
    long getFreeMemory();
    
    long getTotalMemory();
    
    String getRomVersionCode();
    
    String getSubVersion();
    
    String getProduct();
    
    long getTotalSdSize();
    
    long getAvailableSize();
    
    boolean isScreenPortrait();
}
