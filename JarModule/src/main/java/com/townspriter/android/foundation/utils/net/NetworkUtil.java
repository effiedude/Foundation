package com.townspriter.android.foundation.utils.net;

import static android.content.Context.WIFI_SERVICE;
import java.lang.reflect.Method;
import java.util.List;
import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.collection.CollectionUtil;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager.RunnableEx;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.system.SystemInfo;
import com.townspriter.android.foundation.utils.text.StringUtil;
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

@SuppressLint({"DefaultLocale","MissingPermission"})
public class NetworkUtil
{
    /**
     * 网络类型是wifi
     */
    public static final String NETWORK_TYPE_WIFI="wifi";
    public static final String NETWORK_TYPE_UNKNOWN="unknown";
    public static final String NETWORK_UNKNOW_SSID="<unknown ssid>";
    // 接入点类型
    public static final int NETWORK_AP_TYPE_MOBILE_WAP=0;     // cmwap
    public static final int NETWORK_AP_TYPE_MOBILE_NET=1;     // cmnet
    public static final int NETWORK_AP_TYPE_WIFI=2;         // wifi
    public static final int NETWORK_AP_TYPE_NOT_CONFIRM=99;   // not confirm
    public static final int NETWORK_ERROR=0;
    public static final int NETWORK_2G=1;
    public static final int NETWORK_3G4G=2;
    public static final int NETWORK_WIFI=3;
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
    public static final int VDC_AP_UNKNOWN=0;
    public static final int VDC_AP_2G=1;
    public static final int VDC_AP_2_5G=2;
    public static final int VDC_AP_2_75G=3;
    public static final int VDC_AP_3G=4;
    public static final int VDC_AP_WIFI=5;
    public static final int VDC_AP_4G=6;
    public static final String NETWORK_CLASS_NO_CACHED="-2";
    public static final String NETWORK_CLASS_NO_NETWORK="-1";
    public static final String NETWORK_CLASS_UNKNOWN="0";
    public static final String NETWORK_CLASS_2G="2G";
    public static final String NETWORK_CLASS_2_5G="2.5G";
    public static final String NETWORK_CLASS_2_75G="2.75G";
    public static final String NETWORK_CLASS_3G="3G";
    public static final String NETWORK_CLASS_4G="4G";
    private static final String NETWORK_TYPE_NONE="no_network";
    // 这部分在低版本的SDK中没有定义,但高版本有定义,
    // 由于取值不与其它常量不冲突,因此直接在这里定义
    private static final int NETWORK_TYPE_EVDO_B=12;
    private static final int NETWORK_TYPE_EHRPD=14;
    private static final int NETWORK_TYPE_HSPAP=15;
    private static final int NETWORK_TYPE_LTE=13;
    private static boolean sCacheActiveNetworkIniting;
    private static NetworkInfo sCacheActiveNetwork;
    private static NetworkArgs mNetworkChangedNetworkArgs;
    
    public static int getNetworkAcessPoint()
    {
        return doGetNetworkAcessPoint(false);
    }
    
    public static int getNetworkAcessPointFromCache()
    {
        return doGetNetworkAcessPoint(true);
    }
    
    private static int doGetNetworkAcessPoint(boolean isGetFromCache)
    {
        // ap
        int iapType=VDC_AP_UNKNOWN;
        // 如果 AP 为wifi就直接返回 wifi类型
        if(doIsWifiNetwork(isGetFromCache))
        {
            iapType=VDC_AP_WIFI;
        }
        else
        {
            // 如果为mobile或其他类型，则判断网络制式
            String netType=doGetNetworkClass(isGetFromCache);
            if(null!=netType)
            {
                if(NetworkUtil.NETWORK_CLASS_2G.equalsIgnoreCase(netType))
                {
                    iapType=VDC_AP_2G;
                }
                else if(NetworkUtil.NETWORK_CLASS_2_5G.equalsIgnoreCase(netType))
                {
                    iapType=VDC_AP_2_5G;
                }
                else if(NetworkUtil.NETWORK_CLASS_2_75G.equalsIgnoreCase(netType))
                {
                    iapType=VDC_AP_2_75G;
                }
                else if(NetworkUtil.NETWORK_CLASS_3G.equalsIgnoreCase(netType))
                {
                    iapType=VDC_AP_3G;
                }
                else if(NetworkUtil.NETWORK_CLASS_4G.equalsIgnoreCase(netType))
                {
                    iapType=VDC_AP_4G;
                }
            }
        }
        return iapType;
    }
    
    /**
     * @see {@link #NETWORK_2G}, {@link #NETWORK_3G4G}, {@link #NETWORK_WIFI},
     * {@link #NETWORK_ERROR}
     */
    public static int getNetworkType()
    {
        if(isWifiNetwork())
        {
            return NETWORK_WIFI;
        }
        if(is2GNetwork())
        {
            return NETWORK_2G;
        }
        else if(is3GAboveNetwork())
        {
            return NETWORK_3G4G;
        }
        return NETWORK_ERROR;
    }
    
    public static boolean isWifiNetwork()
    {
        return doIsWifiNetwork(false);
    }
    
    /**
     * 判断wifi是IPC行为，在非必要判断的路径可以优先用fromCache
     */
    public static boolean isWifiNetworkFromCache()
    {
        return doIsWifiNetwork(true);
    }
    
    private static boolean doIsWifiNetwork(boolean isGetFromCache)
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
        return NETWORK_TYPE_WIFI.equals(apnName);
    }
    
    /**
     * 是否是移动网络
     *
     * @return
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
        return (!NETWORK_TYPE_WIFI.equals(apnName))&&(!NETWORK_TYPE_UNKNOWN.equals(apnName))&&(!NETWORK_TYPE_NONE.equals(apnName));
    }
    
    public static boolean is2GNetwork()
    {
        String networkClass=getNetworkClass();
        return NETWORK_CLASS_2G.equals(networkClass)||NETWORK_CLASS_2_5G.equals(networkClass)||NETWORK_CLASS_2_75G.equals(networkClass);
    }
    
    /**
     * 是否是3G或以上的移动网络
     *
     * @return
     */
    public static boolean is3GAboveNetwork()
    {
        String networkClass=getNetworkClass();
        return !NETWORK_CLASS_NO_NETWORK.equals(networkClass)&&!NETWORK_CLASS_UNKNOWN.equals(networkClass)&&!NETWORK_CLASS_2G.equals(networkClass)&&!NETWORK_CLASS_2_5G.equals(networkClass)&&!NETWORK_CLASS_2_75G.equals(networkClass);
    }
    
    /**
     * 是否是4G或以上的移动网络
     *
     * @return
     */
    public static boolean is4GAboveNetwork()
    {
        String networkClass=getNetworkClass();
        return !NETWORK_CLASS_NO_NETWORK.equals(networkClass)&&!NETWORK_CLASS_UNKNOWN.equals(networkClass)&&!NETWORK_CLASS_2G.equals(networkClass)&&!NETWORK_CLASS_2_5G.equals(networkClass)&&!NETWORK_CLASS_2_75G.equals(networkClass)&&!NETWORK_CLASS_3G.equals(networkClass);
    }
    
    /**
     * 获取接入点
     *
     * @return cmwap:0 cmnet:1 wfi:2: 99
     */
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
        if(NetworkUtil.NETWORK_CLASS_NO_NETWORK.equals(apnName)||NetworkUtil.NETWORK_CLASS_UNKNOWN.equals(apnName))
        {
            return NETWORK_AP_TYPE_NOT_CONFIRM;
        }
        if(NETWORK_TYPE_WIFI.equalsIgnoreCase(apnName))
        {
            return NETWORK_AP_TYPE_WIFI;
        }
        return(hasProxyForCurApn()?NETWORK_AP_TYPE_MOBILE_WAP:NETWORK_AP_TYPE_MOBILE_NET);
    }
    
    public static String getCurrAccessPointTypeName()
    {
        int apType=getCurrAccessPointType();
        String apTypeName="unknown";
        switch(apType)
        {
            case NETWORK_AP_TYPE_MOBILE_WAP:
                apTypeName="wap";
                break;
            case NETWORK_AP_TYPE_MOBILE_NET:
                apTypeName="net";
                break;
            case NETWORK_AP_TYPE_WIFI:
                apTypeName="wifi";
                break;
            default:
                break;
        }
        return apTypeName;
    }
    
    /**
     * @return 如果当前无activeNetwork, 返回 -1
     * @see NetworkInfo#getType()
     */
    public static int getCurrentNetworkType()
    {
        NetworkInfo info=getActiveNetworkInfo();
        if(null==info)
        {
            return -1;
        }
        return info.getType();
    }
    
    /**
     * @return 如果当前无activityNetwork，返回 ""
     * @see NetworkInfo#getTypeName()
     */
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
    
    public static void isNetworkConnectedAsync(final RunnableEx callback)
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
    
    public static boolean isWifiTurnOn()
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
        ConnectivityManager conMgr=(ConnectivityManager)Foundation.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMgr==null)
        {
            return false;
        }
        String methodName="getMobileDataEnabled";
        Class<?> cmClass=conMgr.getClass();
        Boolean isOpen=null;
        Class<?>[] temps=null;
        try
        {
            Method method=cmClass.getMethod(methodName,temps);
            isOpen=(Boolean)method.invoke(conMgr);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return isOpen;
    }
    
    /**
     * 获得当前使用网络的信息<br/>
     * 即是连接的网络，如果用系统的api得到的activeNetwork为null<br/>
     * 我们还会一个个去找，以适配一些机型上的问题
     */
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
                // 缓存过了，直接取值
                return sCacheActiveNetwork;
            }
            else
            {
                // 没缓存过，此次返回空，但是下次来就有值了
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
            ConnectivityManager cm=(ConnectivityManager)Foundation.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm==null)
            {
                // #if (showlog == true)
                Logger.w("ConnectivityStatus","isQuickNet,ConnectivityManager==null");
                // #endif
                return null;
            }
            activeNetwork=cm.getActiveNetworkInfo(); /* 获得当前使用网络的信息 */
            if(activeNetwork==null||!activeNetwork.isConnected())
            {// 当前无可用连接,或者没有连接,尝试取所有网络再进行判断一次
                NetworkInfo[] allNetworks=cm.getAllNetworkInfo();// 取得所有网络
                if(allNetworks!=null)
                {// 网络s不为null
                    for(int i=0;i<allNetworks.length;i++)
                    {// 遍历每个网络
                        if(allNetworks[i]!=null)
                        {
                            if(allNetworks[i].isConnected())
                            {// 此网络是连接的，可用的
                                activeNetwork=allNetworks[i];
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
     * @return 一个0到-100的区间值，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线。
     */
    public static int getNetworkRssi()
    {
        WifiManager wm=(WifiManager)Foundation.getApplication().getSystemService(WIFI_SERVICE);
        if(wm!=null&&wm.getConnectionInfo()!=null)
        {
            return wm.getConnectionInfo().getRssi();
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * 判断当前wifi是否有加密（安全）
     *
     * @return true(加密网络) or false(非加密网络)
     */
    public static boolean checkCurWifiIsSecurity()
    {
        try
        {
            WifiManager wifiService=(WifiManager)Foundation.getApplication().getSystemService(WIFI_SERVICE);
            WifiConfiguration wifiConfig=null;
            List<WifiConfiguration> wifiList=wifiService.getConfiguredNetworks();
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
        // Proxy
        if(VERSION.SDK_INT>=11)
        {
            // Build.VERSION_CODES.ICE_CREAM_SANDWICH IS_ICS_OR_LATER
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
            // wifi proxy is unreachable in Android2.3 or lower version
            if(isWifiNetwork()&&proxyHost!=null&&proxyHost.indexOf("10.0.0")!=-1)
            {
                proxyHost="";
            }
        }
        return proxyHost;
    }
    
    public static int getProxyPort()
    {
        int proxyPort=80;
        // Proxy
        if(VERSION.SDK_INT>=11)
        {
            // Build.VERSION_CODES.ICE_CREAM_SANDWICH IS_ICS_OR_LATER
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
            // wifi proxy is unreachable in Android2.3 or lower version
            if(isWifiNetwork()&&proxyHost!=null&&proxyHost.indexOf("10.0.0")!=-1)
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
        NetworkInfo activeNetwork=null;
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
        String apnName=NETWORK_TYPE_UNKNOWN;
        if(null==activeNetwork)
        {
            apnName=NETWORK_TYPE_NONE;
            return apnName;
        }
        int networkType=activeNetwork.getType();
        if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI)
        {
            return NETWORK_TYPE_WIFI;
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
            else
            {
                // apnName = apnName;
            }
        }
        else
        {
            apnName=NETWORK_TYPE_WIFI;
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
                return NETWORK_CLASS_NO_CACHED;
            }
        }
        else
        {
            activeNetwork=getActiveNetworkInfo();
        }
        if(activeNetwork==null)
        {
            return NETWORK_CLASS_NO_NETWORK;
        }
        int netSubType=activeNetwork.getSubtype();
        switch(netSubType)
        {
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2G;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return NETWORK_CLASS_2_5G;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return NETWORK_CLASS_2_75G;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return NETWORK_CLASS_3G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4G;
            default:
                return NETWORK_CLASS_UNKNOWN;
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
     * 获取当前WiFi的名称
     *
     * @return
     */
    public static String getConnectedWifiSSID(Activity activity)
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
                if(VERSION.SDK_INT>=28||TextUtils.isEmpty(wifiName)||wifiName.contains(NETWORK_UNKNOW_SSID))
                {
                    // 解决无法获取ssid问题
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
        if(TextUtils.isEmpty(wifiName)||wifiName.equals(NETWORK_UNKNOW_SSID))
        {
            wifiName="";
        }
        return wifiName;
    }
    
    /**
     * 判断当前WiFi网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context)
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
    
    // 以下代码是为了防止用户在networkchanged时候获取网络参数导致的Binder IPC卡死问题
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
