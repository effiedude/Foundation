package com.townspriter.base.foundation.utils.net;

import static android.content.Context.WIFI_SERVICE;

import java.lang.reflect.Method;
import java.util.List;

import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.collection.CollectionUtil;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import com.townspriter.base.foundation.utils.text.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/******************************************************************************
 * @path NetworkUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
@SuppressLint({"DefaultLocale","MissingPermission"})
public class NetworkUtil
{
    public static final String NETWORKxTYPExWIFI="wifi";
    public static final String NETWORKxTYPExUNKNOWN="unknown";
    public static final String NETWORKxUNKNOWNxSSID="<unknown ssid>";
    // 接入点类型
    public static final int NETWORKxAPxTYPExMOBILExWAP=0;
    public static final int NETWORKxAPxTYPExMOBILExNET=1;
    public static final int NETWORKxAPxTYPExWIFI=2;
    public static final int NETWORKxAPxTYPExNOTxCONFIRM=99;
    public static final int NETWORKxERROR=0;
    public static final int NETWORKx2G=1;
    public static final int NETWORKx3G4G=2;
    public static final int NETWORKxWIFI=3;
    /*
     * 接入点类型：
     * 0：未知(默认)
     * 1：2G
     * 2：2.5G
     * 3：2.75G
     * 4：3G
     * 5：wifi接入点
     * 6：4G
     */
    public static final int VDCxAPxUNKNOWN=0;
    public static final int VDCxAPx2G=1;
    public static final int VDCxAPx2x5G=2;
    public static final int VDCxAPx2x75G=3;
    public static final int VDCxAPx3G=4;
    public static final int VDCxAPxWIFI=5;
    public static final int VDCxAPx4G=6;
    public static final String NETWORKxCLASSxNOxCACHED="-2";
    public static final String NETWORKxCLASSxNOxNETWORK="-1";
    public static final String NETWORKxCLASSxUNKNOWN="0";
    public static final String NETWORKxCLASSx2G="2G";
    public static final String NETWORKxCLASSx2x5G="2.5G";
    public static final String NETWORKxCLASSx2x75G="2.75G";
    public static final String NETWORKxCLASSx3G="3G";
    public static final String NETWORKxCLASSx4G="4G";
    private static final String NETWORKxTYPExNONE="no_network";
    // 这部分在低版本的SDK中没有定义但高版本有定义,
    // 由于取值不与其它常量不冲突因此直接在这里定义
    private static final int NETWORKxTYPExEVDOxB=12;
    private static final int NETWORKxTYPExEHRPD=14;
    private static final int NETWORKxTYPExHSPAP=15;
    private static final int NETWORKxTYPExLTE=13;
    private static boolean sCacheActiveNetworkIniting;
    private static NetworkInfo sCacheActiveNetwork;
    private static NetworkArgs mNetworkChangedNetworkArgs;
    
    public static int getNetworkAccessPoint()
    {
        return doGetNetworkAccessPoint(false);
    }
    
    public static int getNetworkAccessPointFromCache()
    {
        return doGetNetworkAccessPoint(true);
    }
    
    private static int doGetNetworkAccessPoint(boolean isGetFromCache)
    {
        int iapType=VDCxAPxUNKNOWN;
        if(doIsWiFiNetwork(isGetFromCache))
        {
            iapType=VDCxAPxWIFI;
        }
        else
        {
            String netType=doGetNetworkClass(isGetFromCache);
            if(null!=netType)
            {
                if(NetworkUtil.NETWORKxCLASSx2G.equalsIgnoreCase(netType))
                {
                    iapType=VDCxAPx2G;
                }
                else if(NetworkUtil.NETWORKxCLASSx2x5G.equalsIgnoreCase(netType))
                {
                    iapType=VDCxAPx2x5G;
                }
                else if(NetworkUtil.NETWORKxCLASSx2x75G.equalsIgnoreCase(netType))
                {
                    iapType=VDCxAPx2x75G;
                }
                else if(NetworkUtil.NETWORKxCLASSx3G.equalsIgnoreCase(netType))
                {
                    iapType=VDCxAPx3G;
                }
                else if(NetworkUtil.NETWORKxCLASSx4G.equalsIgnoreCase(netType))
                {
                    iapType=VDCxAPx4G;
                }
            }
        }
        return iapType;
    }

    public static int getNetworkType()
    {
        if(isWiFiNetwork())
        {
            return NETWORKxWIFI;
        }
        if(is2GNetwork())
        {
            return NETWORKx2G;
        }
        else if(is3GAboveNetwork())
        {
            return NETWORKx3G4G;
        }
        return NETWORKxERROR;
    }
    
    public static boolean isWiFiNetwork()
    {
        return doIsWiFiNetwork(false);
    }

    public static boolean isWiFiNetworkFromCache()
    {
        return doIsWiFiNetwork(true);
    }
    
    private static boolean doIsWiFiNetwork(boolean isGetFromCache)
    {
        if(mNetworkChangedNetworkArgs!=null)
        {
            synchronized(NetworkUtil.class)
            {
                if(mNetworkChangedNetworkArgs!=null)
                {
                    return mNetworkChangedNetworkArgs.mIsWifi;
                }
            }
        }
        String apnName=doGetAccessPointName(isGetFromCache);
        return NETWORKxTYPExWIFI.equals(apnName);
    }
    
    /**
     * 是否是移动网络
     */
    public static boolean isMobileNetwork()
    {
        if(mNetworkChangedNetworkArgs!=null)
        {
            synchronized(NetworkUtil.class)
            {
                if(mNetworkChangedNetworkArgs!=null)
                {
                    return mNetworkChangedNetworkArgs.mIsMobileNetwork;
                }
            }
        }
        String apnName=getAccessPointName();
        return (!NETWORKxTYPExWIFI.equals(apnName))&&(!NETWORKxTYPExUNKNOWN.equals(apnName))&&(!NETWORKxTYPExNONE.equals(apnName));
    }
    
    public static boolean is2GNetwork()
    {
        String networkClass=getNetworkClass();
        return NETWORKxCLASSx2G.equals(networkClass)||NETWORKxCLASSx2x5G.equals(networkClass)||NETWORKxCLASSx2x75G.equals(networkClass);
    }

    public static boolean is3GAboveNetwork()
    {
        String networkClass=getNetworkClass();
        return !NETWORKxCLASSxNOxNETWORK.equals(networkClass)&&!NETWORKxCLASSxUNKNOWN.equals(networkClass)&&!NETWORKxCLASSx2G.equals(networkClass)&&!NETWORKxCLASSx2x5G.equals(networkClass)&&!NETWORKxCLASSx2x75G.equals(networkClass);
    }

    public static boolean is4GAboveNetwork()
    {
        String networkClass=getNetworkClass();
        return !NETWORKxCLASSxNOxNETWORK.equals(networkClass)&&!NETWORKxCLASSxUNKNOWN.equals(networkClass)&&!NETWORKxCLASSx2G.equals(networkClass)&&!NETWORKxCLASSx2x5G.equals(networkClass)&&!NETWORKxCLASSx2x75G.equals(networkClass)&&!NETWORKxCLASSx3G.equals(networkClass);
    }

    public static int getCurrAccessPointType()
    {
        if(mNetworkChangedNetworkArgs!=null)
        {
            synchronized(NetworkUtil.class)
            {
                if(mNetworkChangedNetworkArgs!=null)
                {
                    return mNetworkChangedNetworkArgs.mCurrAccessPointType;
                }
            }
        }
        String apnName=getAccessPointName();
        if(NetworkUtil.NETWORKxCLASSxNOxNETWORK.equals(apnName)||NetworkUtil.NETWORKxCLASSxUNKNOWN.equals(apnName))
        {
            return NETWORKxAPxTYPExNOTxCONFIRM;
        }
        if(NETWORKxTYPExWIFI.equalsIgnoreCase(apnName))
        {
            return NETWORKxAPxTYPExWIFI;
        }
        return(hasProxyForCurApn()?NETWORKxAPxTYPExMOBILExWAP:NETWORKxAPxTYPExMOBILExNET);
    }
    
    public static String getAccessPointTypeName()
    {
        int apType=getCurrAccessPointType();
        String apTypeName="unknown";
        switch(apType)
        {
            case NETWORKxAPxTYPExMOBILExWAP:
                apTypeName="wap";
                break;
            case NETWORKxAPxTYPExMOBILExNET:
                apTypeName="net";
                break;
            case NETWORKxAPxTYPExWIFI:
                apTypeName="wifi";
                break;
            default:
                break;
        }
        return apTypeName;
    }

    public static int getCurrentNetworkType()
    {
        NetworkInfo info=getActiveNetworkInfo();
        if(null==info)
        {
            return -1;
        }
        return info.getType();
    }

    public static String getCurrentNetworkTypeName()
    {
        NetworkInfo info=getActiveNetworkInfo();
        String name=null;
        if(null!=info)
        {
            name=info.getTypeName();
        }
        if(null==name)
        {
            name="";
        }
        return name;
    }
    
    public static boolean isNetworkConnected()
    {
        if(mNetworkChangedNetworkArgs!=null)
        {
            synchronized(NetworkUtil.class)
            {
                if(mNetworkChangedNetworkArgs!=null)
                {
                    return mNetworkChangedNetworkArgs.mIsConnected;
                }
            }
        }
        NetworkInfo info=getActiveNetworkInfo();
        return null!=info&&info.isConnected();
    }
    
    public static void isNetworkConnectedAsync(final ThreadManager.RunnableEx callback)
    {
        ThreadManager.execute(new Runnable()
        {
            @Override
            public void run()
            {
                boolean connected=isNetworkConnected();
                callback.setArg(connected);
            }
        },callback);
    }
    
    public static String getCustomizedMacAddress()
    {
        String MacAddress=SystemInfo.INSTANCE.getMacAddress();
        if(TextUtils.isEmpty(MacAddress))
        {
            MacAddress="unknown";
        }
        else
        {
            MacAddress=MacAddress.replace(":","");
            MacAddress=MacAddress.replace("-","");
        }
        return MacAddress;
    }
    
    public static boolean isWiFiTurnOn()
    {
        try
        {
            WifiManager wifiManager=(WifiManager)Foundation.getApplication().getApplicationContext().getSystemService(WIFI_SERVICE);
            if(wifiManager==null)
            {
                return false;
            }
            if(wifiManager.isWifiEnabled())
            {
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean isMobileTurnOn()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)Foundation.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null)
        {
            return false;
        }
        String methodName="getMobileDataEnabled";
        Class<?> cmClass=connectivityManager.getClass();
        Boolean isOpen;
        try
        {
            Method method=cmClass.getMethod(methodName,(Class<?>)null);
            isOpen=(Boolean)method.invoke(connectivityManager);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
        if(isOpen!=null)
        {
            return isOpen;
        }
        return false;
    }
    
    public static NetworkInfo getActiveNetworkInfo()
    {
        return doGetActiveNetworkInfo(false);
    }
    
    private static NetworkInfo getActiveNetworkInfoFromCache()
    {
        return doGetActiveNetworkInfo(true);
    }
    
    private static NetworkInfo doGetActiveNetworkInfo(boolean isGetFromCache)
    {
        if(mNetworkChangedNetworkArgs!=null)
        {
            synchronized(NetworkUtil.class)
            {
                if(mNetworkChangedNetworkArgs!=null)
                {
                    return mNetworkChangedNetworkArgs.mActiveNetworkInfo;
                }
            }
        }
        if(isGetFromCache)
        {
            if(sCacheActiveNetwork!=null)
            {
                return sCacheActiveNetwork;
            }
            else
            {
                if(!sCacheActiveNetworkIniting)
                {
                    sCacheActiveNetworkIniting=true;
                    ThreadManager.post(ThreadManager.THREADxBACKGROUND,new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            doGetActiveNetworkInfo(false);
                        }
                    });
                }
                return null;
            }
        }
        NetworkInfo activeNetwork=null;
        try
        {
            ConnectivityManager connectivityManager=(ConnectivityManager)Foundation.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager==null)
            {
                return null;
            }
            // 获得当前使用网络的信息
            activeNetwork=connectivityManager.getActiveNetworkInfo();
            if(activeNetwork==null||!activeNetwork.isConnected())
            {
                // 当前无可用连接或者没有连接则尝试取所有网络再进行判断一次
                NetworkInfo[] allNetworks=connectivityManager.getAllNetworkInfo();
                for(NetworkInfo allNetwork:allNetworks)
                {
                    if(allNetwork!=null)
                    {
                        if(allNetwork.isConnected())
                        {
                            activeNetwork=allNetwork;
                            break;
                        }
                    }
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            sCacheActiveNetwork=activeNetwork;
            sCacheActiveNetworkIniting=false;
        }
        return activeNetwork;
    }
    
    /*
     * 获取网络信号强度（目前只做wifi）
     * @return 0~-100
     * 0~50:   信号最好
     * -50~-70:信号偏差
     * 小于-70: 最差(有可能连接不上或者掉线)
     */
    public static int getNetworkRssi(@NonNull Context context)
    {
        WifiManager wifiManager=(WifiManager)context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager!=null&&wifiManager.getConnectionInfo()!=null)
        {
            return wifiManager.getConnectionInfo().getRssi();
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * 判断当前无线是否有加密（安全）
     * 
     * @return true(加密网络) false(非加密网络)
     */
    public static boolean checkWiFiIsSecurity(@NonNull Context context)
    {
        try
        {
            WifiManager wifiManager=(WifiManager)context.getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiConfiguration wifiConfig=null;
            List<WifiConfiguration> wifiList=wifiManager.getConfiguredNetworks();
            if(wifiList!=null)
            {
                int listNum=wifiList.size();
                for(int i=0;i<listNum;i++)
                {
                    WifiConfiguration conf=wifiList.get(i);
                    if(conf.status==Status.CURRENT)
                    {
                        wifiConfig=conf;
                        break;
                    }
                }
                if(wifiConfig==null)
                {
                    for(int i=0;i<listNum;i++)
                    {
                        WifiConfiguration conf=wifiList.get(i);
                        String confStr=conf.toString();
                        String label="LinkAddresses: [";
                        int off=confStr.indexOf(label);
                        if(off>0)
                        {
                            off+=label.length();
                            if(confStr.indexOf("]",off)>off)
                            {
                                wifiConfig=conf;
                                break;
                            }
                        }
                    }
                }
            }
            if(wifiConfig!=null)
            {
                if(!StringUtil.isEmpty(wifiConfig.preSharedKey)||!StringUtil.isEmpty(wifiConfig.wepKeys[0])||!StringUtil.isEmpty(wifiConfig.wepKeys[1])||!StringUtil.isEmpty(wifiConfig.wepKeys[2])||!StringUtil.isEmpty(wifiConfig.wepKeys[3]))
                {
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static String getProxyHost()
    {
        String proxyHost=null;
        if(VERSION.SDK_INT>=11)
        {
            proxyHost=System.getProperty("http.proxyHost");
        }
        else
        {
            Context context=Foundation.getApplication();
            if(context==null)
            {
                return proxyHost;
            }
            proxyHost=android.net.Proxy.getHost(context);
            if(isWiFiNetwork()&&proxyHost!=null&&proxyHost.indexOf("10.0.0")!=-1)
            {
                proxyHost="";
            }
        }
        return proxyHost;
    }
    
    public static int getProxyPort()
    {
        int proxyPort=80;
        if(VERSION.SDK_INT>=11)
        {
            String portStr=System.getProperty("http.proxyPort");
            try
            {
                proxyPort=Integer.parseInt((portStr!=null?portStr:"-1"));
            }
            catch(Exception e)
            {
                proxyPort=-1;
            }
        }
        else
        {
            Context context=Foundation.getApplication();
            if(context==null)
            {
                return proxyPort;
            }
            String proxyHost=null;
            proxyHost=android.net.Proxy.getHost(context);
            proxyPort=android.net.Proxy.getPort(context);
            if(isWiFiNetwork()&&proxyHost!=null&&proxyHost.indexOf("10.0.0")!=-1)
            {
                proxyPort=-1;
            }
        }
        return proxyPort;
    }
    
    public static boolean hasProxyForCurApn()
    {
        Context context=Foundation.getApplication();
        if(context==null)
        {
            return false;
        }
        try
        {
            return null!=getProxyHost();
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public static String getAccessPointName()
    {
        return doGetAccessPointName(false);
    }
    
    public static String getAccessPointNameFromCache()
    {
        return doGetAccessPointName(true);
    }
    
    private static String doGetAccessPointName(boolean isGetFromCache)
    {
        if(mNetworkChangedNetworkArgs!=null)
        {
            synchronized(NetworkUtil.class)
            {
                if(mNetworkChangedNetworkArgs!=null)
                {
                    return mNetworkChangedNetworkArgs.mAccessPointName;
                }
            }
        }
        NetworkInfo activeNetwork;
        if(isGetFromCache)
        {
            activeNetwork=getActiveNetworkInfoFromCache();
            if(activeNetwork==null)
            {
                return "nocache";
            }
        }
        else
        {
            activeNetwork=getActiveNetworkInfo();
        }
        String apnName=NETWORKxTYPExUNKNOWN;
        if(null==activeNetwork)
        {
            apnName=NETWORKxTYPExNONE;
            return apnName;
        }
        int networkType=activeNetwork.getType();
        if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI)
        {
            return NETWORKxTYPExWIFI;
        }
        if(activeNetwork.getExtraInfo()!=null)
        {
            apnName=activeNetwork.getExtraInfo().toLowerCase();
        }
        if(networkType==ConnectivityManager.TYPE_MOBILE)
        {
            if(apnName.contains("cmwap"))
            {
                apnName="cmwap";
            }
            else if(apnName.contains("cmnet"))
            {
                apnName="cmnet";
            }
            else if(apnName.contains("uniwap"))
            {
                apnName="uniwap";
            }
            else if(apnName.contains("uninet"))
            {
                apnName="uninet";
            }
            else if(apnName.contains("3gwap"))
            {
                apnName="3gwap";
            }
            else if(apnName.contains("3gnet"))
            {
                apnName="3gnet";
            }
            else if(apnName.contains("ctwap"))
            {
                apnName="ctwap";
            }
            else if(apnName.contains("ctnet"))
            {
                apnName="ctnet";
            }
        }
        else
        {
            apnName=NETWORKxTYPExWIFI;
        }
        return apnName;
    }
    
    public static String getNetworkClass()
    {
        return doGetNetworkClass(false);
    }
    
    public static String getNetworkClassFromCache()
    {
        return doGetNetworkClass(true);
    }
    
    private static String doGetNetworkClass(boolean isGetFromCache)
    {
        NetworkInfo activeNetwork=null;
        if(isGetFromCache)
        {
            activeNetwork=getActiveNetworkInfoFromCache();
            if(activeNetwork==null)
            {
                return NETWORKxCLASSxNOxCACHED;
            }
        }
        else
        {
            activeNetwork=getActiveNetworkInfo();
        }
        if(activeNetwork==null)
        {
            return NETWORKxCLASSxNOxNETWORK;
        }
        int netSubType=activeNetwork.getSubtype();
        switch(netSubType)
        {
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORKxCLASSx2G;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return NETWORKxCLASSx2x5G;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return NETWORKxCLASSx2x75G;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case NETWORKxTYPExEVDOxB:
            case NETWORKxTYPExEHRPD:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case NETWORKxTYPExHSPAP:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return NETWORKxCLASSx3G;
            case NETWORKxTYPExLTE:
                return NETWORKxCLASSx4G;
            default:
                return NETWORKxCLASSxUNKNOWN;
        }
    }
    
    public static NetworkArgs getNetworkArgs()
    {
        return mNetworkChangedNetworkArgs;
    }
    
    public static void setNetworkArgs(NetworkArgs args)
    {
        synchronized(NetworkUtil.class)
        {
            mNetworkChangedNetworkArgs=args;
        }
        if(args!=null)
        {
            sCacheActiveNetwork=args.mActiveNetworkInfo;
        }
    }
    
    /**
     * 获取当前无线的名称
     */
    public static String getConnectedWiFiSSID(Activity activity)
    {
        String wifiName="";
        if(activity==null)
        {
            return wifiName;
        }
        WifiManager wifiManager=((WifiManager)activity.getApplicationContext().getSystemService(WIFI_SERVICE));
        if(wifiManager!=null)
        {
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            if(wifiInfo!=null)
            {
                wifiName=wifiInfo.getSSID();
                if(VERSION.SDK_INT>=28||TextUtils.isEmpty(wifiName)||wifiName.contains(NETWORKxUNKNOWNxSSID))
                {
                    int networkId=wifiInfo.getNetworkId();
                    List<WifiConfiguration> configuredNetworks=wifiManager.getConfiguredNetworks();
                    if(!CollectionUtil.isEmpty(configuredNetworks))
                    {
                        for(WifiConfiguration wifiConfiguration:configuredNetworks)
                        {
                            if(wifiConfiguration.networkId==networkId)
                            {
                                wifiName=wifiConfiguration.SSID;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(TextUtils.isEmpty(wifiName)||wifiName.equals(NETWORKxUNKNOWNxSSID))
        {
            wifiName="";
        }
        return wifiName;
    }
    
    /**
     * 判断当前无线网络是否连接
     */
    public static boolean isWiFiConnected(Context context)
    {
        if(context!=null)
        {
            ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(manager==null)
            {
                return false;
            }
            NetworkInfo activeNetInfo=manager.getActiveNetworkInfo();
            if(activeNetInfo!=null&&activeNetInfo.getType()==ConnectivityManager.TYPE_WIFI)
            {
                return activeNetInfo.isAvailable();
            }
        }
        return false;
    }
    
    public static class NetworkArgs
    {
        public NetworkInfo mActiveNetworkInfo;
        public String mCurrentIAPName;
        public boolean mIsWifi;
        public boolean mIsConnected;
        public int mCurrAccessPointType;
        public boolean mIsMobileNetwork;
        public String mAccessPointName;
    }
}
