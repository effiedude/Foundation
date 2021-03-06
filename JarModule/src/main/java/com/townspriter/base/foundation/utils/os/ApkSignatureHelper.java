package com.townspriter.base.foundation.utils.os;

import com.townspriter.base.foundation.Foundation;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/******************************************************************************
 * @path ApkSignatureHelper
 * @describe 签名辅助类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ApkSignatureHelper
{
    public static String getApkFileSignature(String apkFile)
    {
        if(null==apkFile)
        {
            return null;
        }
        String ret=null;
        try
        {
            PackageInfo packageInfo=Foundation.getApplication().getPackageManager().getPackageArchiveInfo(apkFile,PackageManager.GET_SIGNATURES);
            if(null!=packageInfo)
            {
                Signature[] signs=packageInfo.signatures;
                if((null!=signs)&&(0<signs.length))
                {
                    ret=signs[0].toCharsString();
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return ret;
    }
    
    public static String getInstalledAppSignature(String packageName)
    {
        if(null==packageName)
        {
            return null;
        }
        String ret=null;
        PackageInfo packageInfo=PackageUtil.getInstance().getPackageInfo(packageName,PackageManager.GET_SIGNATURES);
        if(null!=packageInfo)
        {
            Signature[] signs=packageInfo.signatures;
            ret=signs[0].toCharsString();
        }
        return ret;
    }
}
