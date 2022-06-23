package com.townspriter.base.foundation.utils.system;

import com.townspriter.base.foundation.utils.device.CPUUtil;
import com.townspriter.base.foundation.utils.device.DeviceUtil;
import com.townspriter.base.foundation.utils.device.DisplayUtil;
import com.townspriter.base.foundation.utils.device.MemoryUtil;
import com.townspriter.base.foundation.utils.device.StorageUtil;
import com.townspriter.base.foundation.utils.net.NetworkUtil;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/******************************************************************************
 * @path SystemInfo
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
enum SystemInfoImpl implements ISystemInfo
{
    INSTANCE
    {
        @Override
        public void initialize(Context context)
        {}
        
        @Override
        public float getDensity()
        {
            return DisplayUtil.getDensity();
        }
        
        @Override
        public int getDensityDpi()
        {
            return DisplayUtil.getDensityDpi();
        }
        
        @Override
        public int getDeviceWidth(Context context)
        {
            return getScreenWidth(context);
        }
        
        @Override
        public int getDeviceHeight(Context context)
        {
            return getScreenHeight(context);
        }
        
        @Override
        public String getIMEI()
        {
            return DeviceUtil.getIMEI();
        }
        
        @Override
        public int getScreenWidth(Context context)
        {
            return DisplayUtil.getScreenWidth(context);
        }
        
        @Override
        public int getScreenHeight(Context context)
        {
            return DisplayUtil.getScreenHeight(context);
        }
        
        @Override
        public int getStatusBarHeight()
        {
            return DisplayUtil.getStatusBarHeight();
        }
        
        @Override
        public String getIMSI()
        {
            return DeviceUtil.getIMSI();
        }
        
        @Override
        public String getSMSNo()
        {
            return MobileInfo.getSMSNo();
        }
        
        @Override
        public String getMacAddress()
        {
            return DeviceUtil.getMACAddress();
        }
        
        @Override
        public String getRomInfo()
        {
            return MobileInfo.getRomInfo();
        }
        
        @Override
        public String getCpuInfoArch()
        {
            return CPUUtil.getCPUInfoArch();
        }
        
        @Override
        public String getCpuInfoVfp()
        {
            return CPUUtil.getCPUInfoVFP();
        }
        
        @Override
        public int getCurrAccessPointType()
        {
            return NetworkUtil.getCurrAccessPointType();
        }
        
        @Override
        public String getCpuInfoArchit()
        {
            return CPUUtil.getCPUInfoArchit();
        }
        
        @Override
        public int getCpuCoreCount()
        {
            return CPUUtil.getCPUCoreCount();
        }
        
        @Override
        public String getCpuArch()
        {
            return CPUUtil.getCPUArch();
        }
        
        @Override
        public String getAndroidId()
        {
            return DeviceUtil.getAndroidID();
        }
        
        @Override
        public long getFreeMemory()
        {
            return MemoryUtil.getFreeMemory();
        }
        
        @Override
        public long getTotalMemory()
        {
            return MemoryUtil.getTotalMemory();
        }
        
        @Override
        public String getRomVersionCode()
        {
            return MobileInfo.getInstance().getRomVersionCode();
        }
        
        @Override
        public String getSubVersion()
        {
            return "";
        }
        
        @Override
        public String getProduct()
        {
            return "";
        }
        
        @Override
        public long getTotalSdSize()
        {
            try
            {
                return StorageUtil.getSDCardTotalSize();
            }
            catch(Exception exception)
            {
                return 0;
            }
        }
        
        @Override
        public long getAvailableSize()
        {
            try
            {
                return StorageUtil.getSDCardAvailableSize();
            }
            catch(Exception exception)
            {
                return 0;
            }
        }
        
        /** 获取屏幕的真实高度(包含导航栏高度) */
        @Override
        public int getScreenRealHeight(Activity activity,boolean includeNavBar)
        {
            return DisplayUtil.getScreenRealHeight(activity,includeNavBar);
        }
        
        @Override
        public boolean isScreenPortrait()
        {
            return DisplayUtil.getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT;
        }
    }
}
