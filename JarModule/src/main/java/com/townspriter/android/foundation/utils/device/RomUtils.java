package com.townspriter.android.foundation.utils.device;

import java.lang.reflect.Method;
import com.townspriter.android.foundation.utils.system.VersionUtil;
import android.os.Build;
import android.text.TextUtils;

/******************************************************************************
 * @Path Foundation:RomUtils
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class RomUtils
{
    private static final String TAG=RomUtils.class.getSimpleName();
    private static final String ROMxHUAWEI="ro.build.version.emui";
    private static final String ROMxXIAOMI="ro.miui.ui.version.name";
    private static final String ROMxOPPOxVx2="ro.build.version.opporom";
    private static final String ROMxOPPOxVx3="ro.rom.different.version";
    private static final String ROMxVIVO="ro.vivo.rom.version";
    private static final String ROMxLETV="ro.letv.release.version";
    private static final String ROMxSMARTISAM="ro.product.codename";
    private static final String ROMxMEIZU="ro.product.device";
    private static final String HUAWEIxEMOTIONxUIxROMxPREFIX="EmotionUI";
    
    public static boolean isHonorROM()
    {
        try
        {
            return "honor".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isOPPOROM()
    {
        try
        {
            return "oppo".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isVIVOROM()
    {
        try
        {
            return "vivo".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isMeiZuROM()
    {
        try
        {
            return "meizu".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isSamSungROM()
    {
        try
        {
            return "samsung".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isOnePlusROM()
    {
        try
        {
            return "oneplus".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isLeTvROM()
    {
        try
        {
            String brand=Build.BRAND.toLowerCase();
            if("le".equals(brand)||"letv".equals(brand)||"leeco".equals(brand))
            {
                return true;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return false;
    }
    
    public static boolean isSmartisanROM()
    {
        try
        {
            return "smartisan".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isMiUIROM()
    {
        if(!isXiaoMiROM()||!isRedMiROM())
        {
            return false;
        }
        return !TextUtils.isEmpty(getROMVersion());
    }
    
    public static String getROMVersion()
    {
        if(RomUtils.isHuaWeiROM()||isHonorROM())
        {
            return getRomVersionByProperty(ROMxHUAWEI);
        }
        else if(isXiaoMiROM()||isRedMiROM())
        {
            return getRomVersionByProperty(ROMxXIAOMI);
        }
        else if(isOPPOROM())
        {
            String romV3=getRomVersionByProperty(ROMxOPPOxVx3);
            if(TextUtils.isEmpty(romV3))
            {
                return getRomVersionByProperty(ROMxOPPOxVx2);
            }
            return romV3;
        }
        else if(isVIVOROM())
        {
            return getRomVersionByProperty(ROMxVIVO);
        }
        else if(isLeTvROM())
        {
            return getRomVersionByProperty(ROMxLETV);
        }
        else if(isSmartisanROM())
        {
            return getRomVersionByProperty(ROMxSMARTISAM);
        }
        else if(isMeiZuROM())
        {
            return getRomVersionByProperty(ROMxMEIZU);
        }
        return "";
    }
    
    public static String getRomVersionByProperty(String property)
    {
        if(TextUtils.isEmpty(property))
        {
            return "";
        }
        try
        {
            Class<?> classType=Class.forName("android.os.SystemProperties");
            Method getMethod=classType.getDeclaredMethod("get",String.class);
            String buildVersion=(String)getMethod.invoke(classType,new Object[]{property});
            return buildVersion;
        }
        catch(Exception exception)
        {
            return "";
        }
    }
    
    public static boolean isHuaWeiROM()
    {
        try
        {
            String brand=Build.BRAND.toLowerCase();
            return "huawei".equals(brand)||"honor".equals(brand);
        }
        catch(Throwable throwable)
        {
            return false;
        }
    }
    
    public static boolean isXiaoMiROM()
    {
        try
        {
            return "xiaomi".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable var1)
        {
            return false;
        }
    }
    
    public static boolean isRedMiROM()
    {
        try
        {
            return "redmi".equals(Build.BRAND.toLowerCase());
        }
        catch(Throwable var1)
        {
            return false;
        }
    }
    
    public static boolean isHuaWeiEmotionUI()
    {
        String romVersion=getROMVersion();
        return romVersion!=null&&romVersion.startsWith(HUAWEIxEMOTIONxUIxROMxPREFIX);
    }
    
    /** 是否是坚果手机 */
    public static boolean isJianGuoROM()
    {
        String romVersion=getROMVersion();
        return romVersion!=null&&"jianguo".equals(romVersion.toLowerCase());
    }
    
    public static boolean isMeiZuMx3()
    {
        String romVersion=getROMVersion();
        return romVersion!=null&&"mx3".equals(romVersion.toLowerCase());
    }
    
    /**
     * 版本号比较.只支持如下格式:
     * 包含x.x.x的字符串(其中x为数字.如:ROM2.5或EmotionUI5.1或V3.0
     * 大于基准版本:1
     * 小于基准版本:-1
     * 等于基准版本:0
     * 不支持解析版本号无效或解析失败:-2
     *
     * @param baseVer
     * 基准版本.输入格式:x.x.x 如2.5或2.5.1
     * @return
     */
    public static int compareROMVersion(String baseVer)
    {
        String verStr=getROMVersion();
        if(TextUtils.isEmpty(verStr))
        {
            return -2;
        }
        String ver=verStr.replaceAll("[^0-9.]","");
        if(TextUtils.isEmpty(ver))
        {
            return -2;
        }
        return VersionUtil.compareVersion(ver,baseVer);
    }
}
