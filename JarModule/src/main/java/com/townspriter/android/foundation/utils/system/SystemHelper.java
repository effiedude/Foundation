package com.townspriter.android.foundation.utils.system;

import java.io.File;
import com.townspriter.android.foundation.utils.net.NetworkUtil;
import com.townspriter.android.foundation.utils.net.NetworkUtil.NetworkArgs;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/******************************************************************************
 * @Path Foundation:SystemHelper
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class SystemHelper
{
    private static SystemHelper sInstance;
    
    public static SystemHelper getInstance()
    {
        if(sInstance==null)
        {
            sInstance=new SystemHelper();
        }
        return sInstance;
    }
    
    /**
     * 获取当前接入点
     *
     * @return
     * @JNI
     */
    public String getCurrentIAPName()
    {
        NetworkArgs args=NetworkUtil.getNetworkArgs();
        if(args!=null&&args.mCurrentIAPName!=null)
        {
            return args.mCurrentIAPName;
        }
        NetworkInfo info=NetworkUtil.getActiveNetworkInfo();
        if(null!=info)
        {
            int netType=info.getType();
            if(netType==ConnectivityManager.TYPE_WIFI)
            {
                return NetworkUtil.NETWORK_TYPE_WIFI;
            }
            else if(netType==ConnectivityManager.TYPE_MOBILE)
            {
                String apnName=info.getExtraInfo();
                if(null==apnName)
                {
                    return "";
                }
                else
                {
                    return apnName;
                }
            }
        }
        return "";
    }
    
    public void sendBroadcast(Context context,String uriString)
    {
        if(context!=null)
        {
            String mediaScannerAction=Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
            /** 加上前缀三星相关机子可以提高速度 */
            try
            {
                File file=new File("/mnt/"+uriString);
                Uri uri;
                /** 如果不存在可能是添加前缀导致的.或者存储卡没有挂在在此前缀的路径下.去掉此前缀试试 */
                if(!file.exists())
                {
                    file=new File(uriString);
                    /** 如果还是不存在照着原来的路径进行解析 */
                    if(!file.exists())
                    {
                        uri=Uri.parse(uriString);
                    }
                    else
                    {
                        uri=Uri.fromFile(file);
                    }
                }
                else
                {
                    uri=Uri.fromFile(file);
                }
                Intent intentScanIntent=new Intent(mediaScannerAction,uri);
                context.sendBroadcast(intentScanIntent);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
}
