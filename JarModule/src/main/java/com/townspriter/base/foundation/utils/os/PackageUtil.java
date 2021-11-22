package com.townspriter.base.foundation.utils.os;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

/******************************************************************************
 * @path Foundation:PackageUtil
 * @describe 统一处理判断包是否安装的逻辑.减少获取包信息的IPC调用次数.提升性能
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class PackageUtil
{
    /** 应用安装位置 */
    public static final int INSTALLxLOCATIONxINTERNAL=0;
    public static final int INSTALLxLOCATIONxEXTERNAL=1;
    public static final int INSTALLxLOCATIONxADOPTABLExSTORAGE=2;
    public static final int INSTALLxLOCATIONxUNKNOWN=3;
    public static final int APKxSTATExNOTxINSTALL=0;
    public static final int APKxSTATExINSTALLED=1;
    public static final int APKxSTATExUPGRADE=2;
    private static final String ACTIONxPACKAGExADDED="android.intent.action.PACKAGE_ADDED";
    private static final String ACTIONxPACKAGExREMOVED="android.intent.action.PACKAGE_REMOVED";
    private static final String ANDROIDxCALENDARxPACKAGExNAME="com.android.calendar";
    private static final InstalledReceiver mInstalledReceiver=new InstalledReceiver();
    private static final Object mSyncObj=new Object();
    private static PackageUtil mInstance;
    private static List<PackageInfo> mPackageInfoList;
    
    public static synchronized PackageUtil getInstance()
    {
        if(mInstance==null)
        {
            mInstance=new PackageUtil();
            updateAllInstalledPackageInfo();
            registerReceiver(Foundation.getApplication(),mInstalledReceiver);
        }
        return mInstance;
    }
    
    private static void registerReceiver(Context context,BroadcastReceiver installedReceiver)
    {
        if(context==null||installedReceiver==null)
        {
            return;
        }
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        context.registerReceiver(installedReceiver,filter);
    }
    
    private static void updateAllInstalledPackageInfo()
    {
        PackageManager packageManager=Foundation.getApplication().getPackageManager();
        synchronized(mSyncObj)
        {
            try
            {
                mPackageInfoList=packageManager.getInstalledPackages(0);
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
    
    public static Bitmap getInstalledAppIcon(String packageName)
    {
        if(packageName==null||"".equals(packageName.trim()))
        {
            return null;
        }
        PackageManager pm=Foundation.getApplication().getPackageManager();
        try
        {
            Drawable drawable=pm.getApplicationIcon(packageName);
            if(drawable instanceof BitmapDrawable)
            {
                return ((BitmapDrawable)drawable).getBitmap();
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return null;
    }
    
    public static boolean installAPKFile(String filePath)
    {
        if(filePath==null||"".equals(filePath.trim()))
        {
            return false;
        }
        try
        {
            File apkFile=new File(filePath);
            if(!apkFile.exists())
            {
                return false;
            }
            Intent intent=new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
            Foundation.getApplication().startActivity(intent);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean uninstallPackage(String packageName)
    {
        if(packageName==null||"".equals(packageName.trim()))
        {
            return false;
        }
        try
        {
            Uri packageURI=Uri.parse("package:"+packageName);
            Intent i=new Intent(Intent.ACTION_DELETE,packageURI);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Foundation.getApplication().startActivity(i);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static int getInstallLocation()
    {
        ApplicationInfo info=Foundation.getApplication().getApplicationInfo();
        if((info.flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0)
        {
            return INSTALLxLOCATIONxINTERNAL;
        }
        if(info.dataDir.startsWith(Environment.getDataDirectory().getAbsolutePath()))
        {
            return INSTALLxLOCATIONxEXTERNAL;
        }
        if(info.dataDir.startsWith("/mnt/expand/"))
        {
            return INSTALLxLOCATIONxADOPTABLExSTORAGE;
        }
        return INSTALLxLOCATIONxUNKNOWN;
    }
    
    /** 判断安装包是否为测试包 */
    public static boolean isAPKInDebug()
    {
        try
        {
            ApplicationInfo info=Foundation.getApplication().getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        }
        catch(Exception exception)
        {
            AssertUtil.fail(exception);
            return false;
        }
    }
    
    public boolean isInstalled(String packageName)
    {
        return getPackageInfo(packageName)!=null;
    }
    
    public PackageInfo getPackageInfo(String packageName)
    {
        if(packageName==null||mPackageInfoList==null)
        {
            return null;
        }
        synchronized(mSyncObj)
        {
            for(int i=0;i<mPackageInfoList.size();i++)
            {
                PackageInfo info=mPackageInfoList.get(i);
                if(packageName.equals(info.packageName))
                {
                    return info;
                }
            }
        }
        return null;
    }
    
    public PackageInfo getPackageInfo(String packageName,int flags)
    {
        if(flags==0)
        {
            return getPackageInfo(packageName);
        }
        try
        {
            PackageManager packageManager=Foundation.getApplication().getPackageManager();
            PackageInfo info=packageManager.getPackageInfo(packageName,flags);
            return info;
        }
        catch(NameNotFoundException nameNotFoundException)
        {
            nameNotFoundException.printStackTrace();
        }
        return null;
    }
    
    public List<PackageInfo> getAllInstalledPackageInfo()
    {
        ArrayList<PackageInfo> list;
        synchronized(mSyncObj)
        {
            int size=mPackageInfoList!=null?mPackageInfoList.size():0;
            list=new ArrayList<>(size);
            if(mPackageInfoList!=null)
            {
                for(PackageInfo p:mPackageInfoList)
                {
                    if(p!=null)
                    {
                        list.add(p);
                    }
                }
            }
        }
        return list;
    }
    
    public int getVersionCode() throws NameNotFoundException
    {
        PackageInfo info=getPackageInfo(Foundation.getApplication().getPackageName());
        if(info==null)
        {
            throw new NameNotFoundException();
        }
        return info.versionCode;
    }
    
    public String getVersionName() throws NameNotFoundException
    {
        PackageInfo info=getPackageInfo(Foundation.getApplication().getPackageName());
        if(info==null)
        {
            throw new NameNotFoundException();
        }
        return info.versionName;
    }
    
    /** 获取首次安装的时间戳 */
    public long getFirstInstallTime()
    {
        PackageInfo info=getPackageInfo(Foundation.getApplication().getPackageName());
        if(info==null)
        {
            return -1;
        }
        return info.firstInstallTime;
    }
    
    public long getLastUpdateTime()
    {
        PackageInfo info=getPackageInfo(Foundation.getApplication().getPackageName());
        if(info==null)
        {
            return -1;
        }
        return info.lastUpdateTime;
    }
    
    public boolean isSystemPackage(PackageInfo packageInfo)
    {
        if(null==packageInfo)
        {
            return false;
        }
        PackageInfo calendarPackage=getPackageInfo(ANDROIDxCALENDARxPACKAGExNAME);
        if(calendarPackage!=null&&calendarPackage.firstInstallTime==packageInfo.firstInstallTime)
        {
            return true;
        }
        return packageInfo.applicationInfo!=null&&(packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0;
    }
    
    public int getAPKInstallState(String apkFilePath)
    {
        int state=APKxSTATExNOTxINSTALL;
        PackageManager packageManager=Foundation.getApplication().getPackageManager();
        PackageInfo packageInfo=packageManager.getPackageArchiveInfo(apkFilePath,0);
        if(packageInfo!=null)
        {
            String packageName=packageInfo.packageName;
            PackageInfo installPackageInfo=getPackageInfo(packageName);
            if(installPackageInfo!=null)
            {
                state=APKxSTATExINSTALLED;
                int tempVersionCode=packageInfo.versionCode;    // 文件解析出来的version code
                int installVersionCode=installPackageInfo.versionCode;
                if(installVersionCode<tempVersionCode)
                {
                    state=APKxSTATExUPGRADE;
                }
            }
        }
        return state;
    }
    
    private static class InstalledReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            if(ACTIONxPACKAGExADDED.equals(intent.getAction())||ACTIONxPACKAGExREMOVED.equals(intent.getAction()))
            {
                ThreadManager.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        updateAllInstalledPackageInfo();
                    }
                });
            }
        }
    }
}
