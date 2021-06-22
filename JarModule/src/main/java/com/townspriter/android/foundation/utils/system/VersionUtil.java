package com.townspriter.android.foundation.utils.system;

import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;
import com.townspriter.android.foundation.utils.lang.AssertUtil;
import com.townspriter.android.foundation.utils.os.ProcessUtil;
import com.townspriter.android.foundation.utils.storage.PreferencesUtils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

/******************************************************************************
 * @Path Foundation:VersionUtil
 * @Describe 只能主进程使用
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-05
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class VersionUtil
{
    public static final String PRExKEYxHISTORYxVERSIONS="HistoryVersions";
    private static boolean sIsInit;
    private static boolean sIsReplaceInstall;
    private static boolean sIsNewInstall;
    
    public static void updateVersionInMainProcess()
    {
        if(ProcessUtil.isMainProcess())
        {
            ThreadManager.post(ThreadManager.THREADxBACKGROUND,new Runnable()
            {
                @Override
                public void run()
                {
                    /** 可以延时更新 */
                    updateHistoryVersions();
                }
            });
        }
    }
    
    public static int[] getHistoryVersions()
    {
        String str=PreferencesUtils.getGlobalInstance().getString(PRExKEYxHISTORYxVERSIONS,"");
        if(!TextUtils.isEmpty(str))
        {
            String[] resultStr=str.split(":");
            int[] result=new int[resultStr.length];
            for(int i=0;i<resultStr.length;i++)
            {
                try
                {
                    result[i]=Integer.valueOf(resultStr[i]);
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                    return new int[]{};
                }
            }
            return result;
        }
        return new int[]{};
    }
    
    public static int getCurrentVersion()
    {
        Context context=Foundation.getApplication();
        PackageManager manager=context.getPackageManager();
        try
        {
            PackageInfo info=manager.getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return 0;
        }
    }
    
    public static boolean isNewInstall()
    {
        initInstallState();
        return sIsNewInstall;
    }
    
    public static boolean isReplaceInstall()
    {
        initInstallState();
        return sIsReplaceInstall;
    }
    
    /**
     * 从某个版本及以上发生的覆盖安装
     *
     * @param version
     * 指定版本号
     */
    public static boolean isReplaceInstallFrom(int version)
    {
        /** 覆盖安装时版本号列表容量大于等于二 */
        int[] versions=getHistoryVersions();
        if(versions==null||versions.length<=1)
        {
            return false;
        }
        return versions[versions.length-2]>=version;
    }
    
    public static void updateHistoryVersions()
    {
        initInstallState();
        int currentVersion=getCurrentVersion();
        if(currentVersion>0)
        {
            int[] versions=getHistoryVersions();
            if(versions.length==0)
            {
                PreferencesUtils.getGlobalInstance().setString(PRExKEYxHISTORYxVERSIONS,String.valueOf(currentVersion));
            }
            else
            {
                if(versions[versions.length-1]!=currentVersion)
                {
                    String r=join(versions,":");
                    r=r+":"+currentVersion;
                    PreferencesUtils.getGlobalInstance().setString(PRExKEYxHISTORYxVERSIONS,r);
                }
            }
        }
    }
    
    private static String join(int[] versions,String s)
    {
        if(versions.length==0)
        {
            return "";
        }
        else
        {
            StringBuilder sb=new StringBuilder();
            sb.append(versions[0]);
            for(int i=1;i<versions.length;i++)
            {
                sb.append(s);
                sb.append(versions[i]);
            }
            return sb.toString();
        }
    }
    
    /**
     * 返回值说明如下
     * 大于当前版本:1
     * 小于当前版本:-1
     * 等于当前版本:0
     * 版本号无效或解析失败:-2
     *
     * @param ver
     * 版本号
     * @return 对比结果
     */
    public static int compareCurrentVersion(String ver)
    {
        String currentVer=getVerName(Foundation.getApplication());
        return compareVersion(ver,currentVer);
    }
    
    /**
     * 返回值说明如下:
     * 大于基准版本:1
     * 小于基准版本:-1
     * 等于基准版本:0
     * 版本号无效或解析失败:-2
     *
     * @param ver
     * 需要比较的版本
     * 输入格式:x.x.x
     * 例如:2.5 2.5.1
     * @param baseVer
     * 基准版本
     * 输入格式:x.x.x
     * 例如:2.5 2.5.1
     * @return 对比结果
     */
    public static int compareVersion(String ver,String baseVer)
    {
        if(TextUtils.isEmpty(ver)||TextUtils.isEmpty(baseVer))
        {
            return -2;
        }
        String[] baseVers=baseVer.split("\\.");
        String[] vers=ver.split("\\.");
        if(baseVers.length<vers.length)
        {
            String[] newArray=new String[vers.length];
            System.arraycopy(baseVers,0,newArray,0,baseVers.length);
            for(int i=baseVers.length;i<vers.length;i++)
            {
                newArray[i]="0";
            }
            baseVers=newArray;
        }
        else if(baseVers.length>vers.length)
        {
            String[] newArray=new String[baseVers.length];
            System.arraycopy(vers,0,newArray,0,vers.length);
            for(int i=vers.length;i<baseVers.length;i++)
            {
                newArray[i]="0";
            }
            vers=newArray;
        }
        try
        {
            for(int i=0;i<vers.length;i++)
            {
                if(TextUtils.isEmpty(vers[i]))
                {
                    vers[i]="0";
                }
                if(TextUtils.isEmpty(baseVers[i]))
                {
                    baseVers[i]="0";
                }
                int bitVer=Integer.valueOf(vers[i]);
                int bitBaseVer=Integer.valueOf(baseVers[i]);
                if(bitBaseVer<bitVer)
                {
                    return 1;
                }
                else if(bitBaseVer>bitVer)
                {
                    return -1;
                }
            }
        }
        catch(NumberFormatException numberFormatException)
        {
            numberFormatException.printStackTrace();
            return -2;
        }
        return 0;
    }
    
    private static synchronized void initInstallState()
    {
        if(!sIsInit)
        {
            AssertUtil.mustOk(ProcessUtil.isMainProcess());
            int[] versions=getHistoryVersions();
            int currentVersionCode=getCurrentVersion();
            int lastVersionCode=versions.length==0?0:versions[versions.length-1];
            if(lastVersionCode==0)
            {
                sIsNewInstall=true;
                sIsReplaceInstall=false;
            }
            else if(lastVersionCode!=currentVersionCode)
            {
                sIsNewInstall=false;
                sIsReplaceInstall=true;
            }
            else
            {
                sIsNewInstall=false;
                sIsReplaceInstall=false;
            }
            sIsInit=true;
        }
    }
    
    public static String getVerName(Context context)
    {
        if(context==null)
        {
            context=Foundation.getApplication();
        }
        String verName="";
        if(context!=null)
        {
            try
            {
                verName=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            }
            catch(NameNotFoundException nameNotFoundException)
            {
                nameNotFoundException.printStackTrace();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return verName;
    }
}
