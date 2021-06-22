package com.townspriter.android.foundation.utils.system;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.townspriter.android.foundation.utils.device.StorageUtil;
import com.townspriter.android.foundation.utils.permission.IPermissionInjectInterface;
import com.townspriter.android.foundation.utils.permission.PermissionManager;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;

/******************************************************************************
 * @Path Foundation:PathManager
 * @Describe 负责获取各种路径
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public final class PathManager
{
    private static final Map<String,Boolean> mPathStatus=new HashMap<String,Boolean>();
    
    public static String getRootDir()
    {
        try
        {
            String rootPath=getAvailableStorage()+"/"+""+"/";
            ensureDir(rootPath);
            return rootPath;
        }
        catch(Exception exception)
        {
            return "";
        }
    }
    
    public static String getDownloadPath()
    {
        try
        {
            String path=getRootDir()+"download/";
            ensureDir(path);
            return path;
        }
        catch(Exception exception)
        {
            return "";
        }
    }
    
    public static String getInfoflowContentPath()
    {
        try
        {
            String path=getRootDir()+"content";
            ensureDir(path);
            return path;
        }
        catch(Exception exception)
        {
            return "";
        }
    }
    
    private static void ensureDir(String path)
    {
        if(!mPathStatus.containsKey(path))
        {
            if(!checkPathAvailable(path))
            {
                return;
            }
            if(checkIfDirectoryValid(path))
            {
                mPathStatus.put(path,true);
            }
        }
    }
    
    private static String getAvailableStorage()
    {
        /** 如果主存储可用则使用主存储 */
        if(StorageUtil.getInstance().isDefaultStorageWritable())
        {
            return StorageUtil.getInstance().getStoragePrimary();
        }
        /** 主存储不可用选中一个非主存储 */
        if(StorageUtil.getInstance().getStorageListAvailable().size()>0)
        {
            return StorageUtil.getInstance().getStorageListAvailable().get(0);
        }
        /** 没有可用的存储介质.还是返回主存储的路径 */
        return StorageUtil.getInstance().getStoragePrimary();
    }
    
    private static final Object getMountService()
    {
        try
        {
            Method method=Class.forName("android.os.ServiceManager").getMethod("getService",String.class);
            IBinder binder=(IBinder)method.invoke(null,"mount");
            Class<?> mIMountService=Class.forName("android.os.storage.IMountService");
            Class<?>[] classes=mIMountService.getClasses();
            if(classes==null||classes.length==0)
            {
                return null;
            }
            Class<?> Stub=classes[0];
            Method asInterface=Stub.getMethod("asInterface",IBinder.class);
            Object iMountService=asInterface.invoke(Stub,binder);
            return iMountService;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }
    
    private static final boolean checkPathAvailable(String path)
    {
        boolean bAvailable=false;
        if(TextUtils.isEmpty(path))
        {
            return bAvailable;
        }
        File es=Environment.getExternalStorageDirectory();
        if(es!=null)
        {
            if(path.contains(es.toString()))
            {
                IPermissionInjectInterface injectInterface=PermissionManager.getInstance().getPermissionInjectInterface();
                if(injectInterface!=null)
                {
                    bAvailable=injectInterface.isInjectExternalStorageAvailable();
                }
                return bAvailable;
            }
        }
        try
        {
            Object mountService=getMountService();
            if(mountService==null)
            {
                return true;
            }
            Method mGetVolumeState=mountService.getClass().getMethod("getVolumeState",String.class);
            List<String> storageList=StorageUtil.getInstance().getStorageListAvailable();
            for(int i=0;i<storageList.size();i++)
            {
                String storage=storageList.get(i);
                if(path.contains(storage))
                {
                    IPermissionInjectInterface injectInterface=PermissionManager.getInstance().getPermissionInjectInterface();
                    if(injectInterface!=null)
                    {
                        bAvailable=injectInterface.isInjectExternalStorageAvailable();
                    }
                    return bAvailable;
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            bAvailable=true;
        }
        return bAvailable;
    }
    
    public static boolean checkIfDirectoryValid(String path)
    {
        File file=new File(path);
        if(file.isFile())
        {
            file.delete();
        }
        if(!file.exists())
        {
            file.mkdirs();
        }
        return file.exists()&&file.isDirectory();
    }
}
