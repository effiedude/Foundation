package com.townspriter.android.foundation.utils.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.TextUtils;

/******************************************************************************
 * @Path Foundation:PhoneTypeUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class PhoneTypeUtil
{
    private static final int SDKxINTxMEIZUxFLYMEx2=16;
    private static final int SDKxINTxMEIZUxFLYMEx3=17;
    private static final String[] MEIZUxSMARTBARxDEVICExLIST={"M040","M045"};
    private static final String FLYME="Flyme";
    private static final String XIAOMI="Xiaomi";
    private static final String MOTOROLA="motorola";
    private static final String MOTOE2="MotoE2";
    private static final String[] MANUFACTURERSxNOTxUSExNEWxGUIDE={"OPPO"};
    private static boolean sHasCheckedMeizuFlyemeVersion;
    private static boolean sIsMeizuFlyme2;
    private static boolean sIsMeizuFlyme3;
    private static boolean sHasCheckedIfMIUISystemV5OrAbove;
    private static boolean sIsMIUISystemV5OrAbove;
    private static boolean sHasCheckedMeizuMXSeries;
    private static boolean sIsMeizuMXSeries;
    private static String sMIUIVer;
    private static boolean mIsFirstInitPhoneType=true;
    private static PhoneType mPhoneType=PhoneType.UNKNOWN;
    
    public static boolean isMeiZuMXSeries()
    {
        if(sHasCheckedMeizuMXSeries)
        {
            return sIsMeizuMXSeries;
        }
        if(Build.DISPLAY.contains(FLYME))
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
    
    public static boolean isJianGuoPro()
    {
        return "OD103".equals(Build.MODEL);
    }
    
    public static boolean isFlyMeROM()
    {
        return Build.DISPLAY!=null&&Build.DISPLAY.contains(FLYME);
    }
    
    private static void checkMeiZuFlyMeVersion()
    {
        if(Build.DISPLAY.contains(FLYME))
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
    
    public static boolean isMotoGBrand()
    {
        return MOTOROLA.equalsIgnoreCase(Build.BRAND)&&Build.MODEL.contains(MOTOE2)&&VERSION.SDK_INT>=21;
    }
    
    static boolean isMeiZuFlymeTow()
    {
        if(!sHasCheckedMeizuFlyemeVersion)
        {
            checkMeiZuFlyMeVersion();
            sHasCheckedMeizuFlyemeVersion=true;
        }
        return sIsMeizuFlyme2;
    }
    
    public static boolean isMeiZuFlymeThree()
    {
        if(!sHasCheckedMeizuFlyemeVersion)
        {
            checkMeiZuFlyMeVersion();
            sHasCheckedMeizuFlyemeVersion=true;
        }
        return sIsMeizuFlyme3;
    }
    
    public static boolean isMIPhone()
    {
        return Build.MANUFACTURER.equals(XIAOMI);
    }
    
    public static boolean isMIBrand()
    {
        return XIAOMI.equals(Build.BRAND);
    }
    
    public static boolean isMiUIVer6OrAbove()
    {
        try
        {
            String KEY_MIUI_VERSION_NAME="ro.miui.ui.version.name";
            BuildProperties prop=BuildProperties.getInstance();
            String name=prop.getProperty(KEY_MIUI_VERSION_NAME,"");
            String numberString=name.substring(1);
            int number=Integer.valueOf(numberString);
            return number>=6;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
    }
    
    public static boolean isMIUIVerEqualOrLargeThan(String ver)
    {
        if(sMIUIVer==null)
        {
            try
            {
                BuildProperties prop=BuildProperties.getInstance();
                sMIUIVer=prop.getProperty("ro.build.version.incremental","");
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        if(TextUtils.isEmpty(sMIUIVer))
        {
            return false;
        }
        return isVersionEqualOrLargeThan(sMIUIVer,ver);
    }
    
    /**
     * 判断版本一是否大于等于版本二
     * 注意:此方法效率不高.请判断后自行缓存判断结果.避免每次都比较
     *
     * @param versionA
     * 格式:7.7.6
     * @param versionB
     * 格式:7.7.6
     */
    private static boolean isVersionEqualOrLargeThan(String versionA,String versionB)
    {
        if(versionA.equals(versionB))
        {
            return true;
        }
        try
        {
            versionA=versionA.replace("V","");
            versionA=versionA.replace("v","");
            String splitChar="\\.";
            String[] numStrArrayA=versionA.split(splitChar);
            String[] numStrArrayB=versionB.split(splitChar);
            if(numStrArrayA==null||numStrArrayB==null)
            {
                return false;
            }
            int firstNumA=Integer.valueOf(numStrArrayA[0].trim());
            int secondNumA=Integer.valueOf(numStrArrayA[1].trim());
            int thirdNumA=Integer.valueOf(numStrArrayA[2].trim());
            int firstNumB=Integer.valueOf(numStrArrayB[0].trim());
            int secondNumB=Integer.valueOf(numStrArrayB[1].trim());
            int thirdNumB=Integer.valueOf(numStrArrayB[2].trim());
            if(firstNumA<firstNumB)
            {
                return false;
            }
            else if(firstNumA>firstNumB)
            {
                return true;
            }
            if(secondNumA<secondNumB)
            {
                return false;
            }
            else if(secondNumA>secondNumB)
            {
                return true;
            }
            if(thirdNumA<thirdNumB)
            {
                return false;
            }
            else if(thirdNumA>thirdNumB)
            {
                return true;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return false;
    }
    
    public static boolean isMIUISystemVer5OrAbove()
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
        return sIsMIUISystemV5OrAbove;
    }
    
    public static boolean isSupportUserGuideFadeoutAnim4AcVersion()
    {
        String manu=Build.MANUFACTURER;
        for(String m:MANUFACTURERSxNOTxUSExNEWxGUIDE)
        {
            if(m.equals(manu))
            {
                return false;
            }
        }
        return true;
    }
    
    public static PhoneType getCurrentPhoneType()
    {
        if(!mIsFirstInitPhoneType)
        {
            return mPhoneType;
        }
        mIsFirstInitPhoneType=false;
        mPhoneType=PhoneType.getPhoneTypeByBrand(Build.BRAND);
        return mPhoneType;
    }
    
    public static boolean isEMUI()
    {
        File file=new File(Environment.getRootDirectory(),"build.prop");
        if(file.exists())
        {
            Properties properties=new Properties();
            FileInputStream fileInputStream=null;
            try
            {
                fileInputStream=new FileInputStream(file);
                properties.load(fileInputStream);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            finally
            {
                if(fileInputStream!=null)
                {
                    try
                    {
                        fileInputStream.close();
                    }
                    catch(IOException ioException)
                    {
                        ioException.printStackTrace();
                    }
                }
            }
            return properties.containsKey("ro.build.hw_emui_api_level");
        }
        return false;
    }
    
    /** 是否为华为企业智慧屏设备(超大屏) */
    public static boolean isHuaWeiIdeaHub()
    {
        String manufacture=Build.MANUFACTURER;
        String model=Build.MODEL;
        return DeviceConst.HUAWEI.equals(manufacture)&&DeviceConst.IDEAHUB.equals(model);
    }
    
    public static class BuildProperties
    {
        private static BuildProperties sBuildProperties;
        private final Properties properties;
        
        private BuildProperties() throws IOException
        {
            properties=new Properties();
            InputStream is=new FileInputStream(new File(Environment.getRootDirectory(),"build.prop"));
            properties.load(is);
            is.close();
        }
        
        public static BuildProperties getInstance() throws IOException
        {
            if(sBuildProperties==null)
            {
                BuildProperties properties=new BuildProperties();
                sBuildProperties=properties;
            }
            return sBuildProperties;
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
}
