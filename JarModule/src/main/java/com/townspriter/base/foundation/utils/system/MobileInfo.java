package com.townspriter.base.foundation.utils.system;

import android.content.Context;
import android.os.Build.VERSION;

/******************************************************************************
 * @path MobileInfo
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class MobileInfo
{
    private static final String TAG="MobileInfo";
    @SuppressWarnings("unused")
    private static final int MAXxSIRIxRESULTxCOUNT=10;
    @SuppressWarnings("unused")
    private static final int ERRORxSERVICExNOTxAVAILABLE=19;
    private static final String DEFxROMxRELEASE="8";
    private static final String DEFxROMxVERSIONxCODE="2.2.2";
    private static final int ACxLOWESTxROMxRELEASE=14;
    private static final int ACxLOWESTxROMxVERSIONxCODE=4;
    private static MobileInfo mInstance=new MobileInfo();
    private static Context mContent;
    
    public MobileInfo()
    {}
    
    public static MobileInfo getInstance()
    {
        return mInstance;
    }
    
    public static void setContext(Context context)
    {
        mContent=context;
    }
    
    public static void destroy()
    {
        mInstance=null;
        mContent=null;
    }
    
    public static boolean isSystemVersionNotSmallerThan(int lowestVersion)
    {
        return VERSION.SDK_INT>=lowestVersion;
    }
    
    public static String getXUCBrowserUserAgent()
    {
        return "";
    }
    
    public static String getRomInfo()
    {
        String rom=VERSION.RELEASE;
        if(null!=rom&&rom.length()>0)
        {
            boolean isOSVersionValid=Character.getNumericValue(rom.trim().charAt(0))>=ACxLOWESTxROMxVERSIONxCODE;
            if(isOSVersionValid&&(!SystemUtil.isRomMainVersionAtLeast4()))
            {
                return DEFxROMxVERSIONxCODE;
            }
        }
        return(null!=rom?rom:"");
    }
    
    public static String getSMSNo()
    {
        return null;
    }
    
    public String getRomVersionCode()
    {
        if(VERSION.SDK_INT>=ACxLOWESTxROMxRELEASE&&(!SystemUtil.isRomMainVersionAtLeast4()))
        {
            return DEFxROMxRELEASE;
        }
        return String.valueOf(VERSION.SDK_INT);
    }
}
