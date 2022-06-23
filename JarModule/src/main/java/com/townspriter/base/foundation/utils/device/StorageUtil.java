package com.townspriter.base.foundation.utils.device;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.permission.IPermissionInjectInterface;
import com.townspriter.base.foundation.utils.permission.PermissionManager;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.text.TextUtils;

/******************************************************************************
 * @path StorageUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class StorageUtil
{
    private static final int NEWxAPIxLEVELxFORxSTATFS=VERSION_CODES.JELLY_BEAN_MR2;
    private static final String OLDxSDCARDxPATH="/sdcard";
    private static volatile StorageUtil mInstance;
    private final List<String> mListStorageAll;
    private final List<String> mListStorageAvaliable;
    private final List<String> mListStorageExternal;
    private final List<String> mListStorageInternal;
    private String mStoragePrimary;
    private boolean mExternalStorageWritable;
    
    private StorageUtil()
    {
        mListStorageAll=new ArrayList<>();
        mListStorageAvaliable=new ArrayList<>();
        mListStorageExternal=new ArrayList<>();
        mListStorageInternal=new ArrayList<>();
        mStoragePrimary="";
        getSysStorage();
    }
    
    public static StorageUtil getInstance()
    {
        if(mInstance==null)
        {
            synchronized(StorageUtil.class)
            {
                if(mInstance==null)
                {
                    mInstance=new StorageUtil();
                }
            }
        }
        return mInstance;
    }
    
    private static String getStorageState(Object volume,String path)
    {
        /** 4.4.2以后StorageVolume有getState的public方法 */
        if(VERSION.SDK_INT>=VERSION_CODES.KITKAT)
        {
            try
            {
                String storageVolumeClassName="android.os.storage.StorageVolume";
                Class storageVolumeClass=Class.forName(storageVolumeClassName);
                Method methodGetState=storageVolumeClass.getDeclaredMethod("getState");
                methodGetState.setAccessible(true);
                return (String)methodGetState.invoke(volume);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return getStorageState(path);
    }
    
    public static String getStorageState(String path)
    {
        /** 5.0以后有公开Environment.getExternalStorageState */
        if(VERSION.SDK_INT>=VERSION_CODES.LOLLIPOP)
        {
            try
            {
                return Environment.getExternalStorageState(new File(path));
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else
        {
            try
            {
                Object mountService=getMountService();
                if(mountService!=null)
                {
                    Method getVolumeState=mountService.getClass().getDeclaredMethod("getVolumeState",String.class);
                    getVolumeState.setAccessible(true);
                    return (String)getVolumeState.invoke(mountService,path);
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return Environment.MEDIA_REMOVED;
    }
    
    /** 存储卡是否已挂载 */
    public static final boolean isExternalStorageMounted()
    {
        try
        {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    /** 是否有可存储的介质 */
    public static final boolean hasStorage()
    {
        return isExternalStorageMounted()||getInstance().getStorageListAvailable().size()>1;
    }
    
    /** 外部存储目录 */
    public static File getExternalStorageDirectory()
    {
        return Environment.getExternalStorageDirectory();
    }
    
    /** 外部存储目录路径 */
    public static String getExternalStorageDirectoryPath()
    {
        return getExternalStorageDirectory().getAbsolutePath();
    }
    
    public static List<String> getSecondaryStorages()
    {
        List<String> available=getInstance().getStorageListAvailable();
        String storagePrimary=getInstance().getStoragePrimary();
        List<String> sndStorageList=new ArrayList<String>();
        for(String storage:available)
        {
            if(storage.equals(storagePrimary))
            {
                continue;
            }
            sndStorageList.add(storage);
        }
        return sndStorageList;
    }
    
    public static DiskInfo getDiskInfo(String path)
    {
        DiskInfo diskInfo=null;
        try
        {
            StatFs statFs=new StatFs(path);
            diskInfo=new DiskInfo();
            if(VERSION.SDK_INT>=NEWxAPIxLEVELxFORxSTATFS)
            {
                diskInfo.mAvailableSize=statFs.getBlockSizeLong()*statFs.getAvailableBlocksLong();
                diskInfo.mTotalSize=statFs.getBlockSizeLong()*statFs.getBlockCountLong();
            }
            else
            {
                /** 需要类型转换防止溢出.存储卡剩余容量超过两基时若下载出错.会提示存储卡空间不足 */
                diskInfo.mAvailableSize=(long)statFs.getBlockSize()*statFs.getAvailableBlocks();
                diskInfo.mTotalSize=(long)statFs.getBlockSize()*statFs.getBlockCount();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return diskInfo;
    }
    
    public static DiskInfo getTotalDiskInfo()
    {
        DiskInfo diskInfo=new DiskInfo();
        List<String> storageList=getInstance().getStorageListAvailable();
        for(String path:storageList)
        {
            DiskInfo diskInfo2=getDiskInfo(path);
            if(diskInfo2!=null)
            {
                diskInfo.mAvailableSize+=diskInfo2.mAvailableSize;
                diskInfo.mTotalSize+=diskInfo2.mTotalSize;
            }
        }
        return diskInfo;
    }
    
    public static List<String> getWritableSDCardList()
    {
        ArrayList<String> writableList=new ArrayList<String>();
        List<String> availableSDCardList=getInstance().getStorageListAvailable();
        for(String sdcardPath:availableSDCardList)
        {
            if(isStorageWritable(sdcardPath))
            {
                writableList.add(sdcardPath);
            }
        }
        return writableList;
    }
    
    public static String getWritableInternalSDCard()
    {
        List<String> writableSDCardList=getWritableSDCardList();
        List<String> internalSDCardList=getInstance().getStorageInternal();
        if(writableSDCardList==null||writableSDCardList.size()==0)
        {
            return null;
        }
        for(String internalSDCard:internalSDCardList)
        {
            if(writableSDCardList.contains(internalSDCard))
            {
                return internalSDCard;
            }
        }
        return null;
    }
    
    public static String getWritableExternalSDCard()
    {
        List<String> writableSDCardList=getWritableSDCardList();
        List<String> internalSDCardList=getInstance().getStorageInternal();
        if(writableSDCardList==null||writableSDCardList.size()==0)
        {
            return null;
        }
        if(internalSDCardList==null||internalSDCardList.size()==0)
        {
            return writableSDCardList.get(0);
        }
        for(String writableSDCard:writableSDCardList)
        {
            if(!internalSDCardList.contains(writableSDCard))
            {
                return writableSDCard;
            }
        }
        return null;
    }
    
    public static final boolean isStorageWritable(String path)
    {
        String state=getStorageState(path);
        return Environment.MEDIA_MOUNTED.equalsIgnoreCase(state);
    }
    
    public static long getFileTotalSize(File file) throws FileNotFoundException
    {
        if(file==null||!file.exists())
        {
            throw new FileNotFoundException();
        }
        DiskInfo diskInfo=getDiskInfo(file.getPath());
        return diskInfo!=null?diskInfo.mTotalSize:-1;
    }
    
    public static long getFileTotalSize(String filePath) throws FileNotFoundException
    {
        if(StringUtil.isEmpty(filePath))
        {
            throw new FileNotFoundException("File path illegal");
        }
        return getFileTotalSize(new File(filePath));
    }
    
    public static final long getFileAvailableSize(File file) throws FileNotFoundException
    {
        if(null==file||!file.exists())
        {
            throw new FileNotFoundException();
        }
        DiskInfo diskInfo=getDiskInfo(file.getPath());
        return diskInfo!=null?diskInfo.mAvailableSize:-1;
    }
    
    public static final long getFileAvailableSize(String filePath) throws FileNotFoundException
    {
        if(StringUtil.isEmpty(filePath))
        {
            throw new FileNotFoundException("File path illegal");
        }
        return getFileAvailableSize(new File(filePath));
    }
    
    public static final long getSDCardTotalSize() throws FileNotFoundException
    {
        if(!isExternalStorageMounted())
        {
            throw new FileNotFoundException("SDCard not exists");
        }
        File sdcardDir=Environment.getExternalStorageDirectory();
        return getFileTotalSize(sdcardDir);
    }
    
    public static final long getSDCardAvailableSize() throws FileNotFoundException
    {
        if(!isExternalStorageMounted())
        {
            throw new FileNotFoundException("SDCard not exists");
        }
        File sdcardDir=Environment.getExternalStorageDirectory();
        return getFileAvailableSize(sdcardDir);
    }
    
    public static final long getSystemTotalSize() throws FileNotFoundException
    {
        File root=Environment.getRootDirectory();
        return getFileTotalSize(root);
    }
    
    public static final long getSystemAvailableSize() throws FileNotFoundException
    {
        File root=Environment.getRootDirectory();
        return getFileAvailableSize(root);
    }
    
    /**
     * 将以OLDxSDCARDxPATH开始的文件路径转换成实际路径.主要场景是旧代码中传过来的路径是硬编码的OLDxSDCARDxPATH
     * 替换成Environment.getExternalStorageDirectory().getAbsolutePath()
     * 
     * @param filePath
     * @return
     */
    public static String convertOldSdcardPathToRealPath(String filePath)
    {
        if(StringUtil.isNotEmptyWithTrim(filePath)&&filePath.startsWith(OLDxSDCARDxPATH))
        {
            filePath=filePath.replaceFirst(OLDxSDCARDxPATH,Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return filePath;
    }
    
    public static String getAvailableStorage()
    {
        /** 如果主存储可用则使用主存储 */
        if(getInstance().isDefaultStorageWritable())
        {
            return getInstance().getStoragePrimary();
        }
        /** 主存储不可用选中一个非主存储 */
        if(getInstance().getStorageListAvailable().size()>0)
        {
            return getInstance().getStorageListAvailable().get(0);
        }
        /** 没有可用的存储介质.还是返回主存储的路径 */
        return getInstance().getStoragePrimary();
    }
    
    private static Object getMountService()
    {
        try
        {
            Method getService=Class.forName("android.os.ServiceManager").getMethod("getService",String.class);
            getService.setAccessible(true);
            IBinder binder=(IBinder)getService.invoke(null,"mount");
            Class<?> iMountService$Stub=Class.forName("android.os.storage.IMountService$Stub");
            Method asInterface=iMountService$Stub.getMethod("asInterface",IBinder.class);
            asInterface.setAccessible(true);
            Object iMountService=asInterface.invoke(null,binder);
            return iMountService;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }
    
    public static final boolean checkPathAvailable(String path)
    {
        boolean bAvailable=false;
        if(!TextUtils.isEmpty(path))
        {
            File es=Environment.getExternalStorageDirectory();
            if(es!=null)
            {
                if(path.contains(es.toString()))
                {
                    bAvailable=isExternalStorageMounted();
                    return bAvailable;
                }
            }
            List<String> storageList=getInstance().getStorageListAvailable();
            for(int i=0;i<storageList.size();i++)
            {
                String storage=storageList.get(i);
                if(path.contains(storage))
                {
                    if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(getStorageState(storage)))
                    {
                        bAvailable=true;
                    }
                }
            }
        }
        return bAvailable;
    }
    
    /** 判断是否应用私有目录.先简单使用路径对比(注意:未考虑内置外置存储卡都有的情况) */
    public static boolean isApplicationExternalDir(String path)
    {
        if(StringUtil.isEmptyWithTrim(path))
        {
            return false;
        }
        /** 替换历史写死的存储卡 */
        path=convertOldSdcardPathToRealPath(path);
        File dir=Foundation.getApplication().getExternalFilesDir(null);
        /** 如果获取不到应用目录认为非私有目录 */
        if(dir==null)
        {
            return false;
        }
        return path.startsWith(dir.getParent());
    }
    
    /** 获取存储卡应用文件根目录.一般不允许随意添加文件到根目录 */
    public static File getApplicationExternalFilesDir()
    {
        return getApplicationExternalFilesDir(null);
    }
    
    /**
     * 获取存储卡应用文件子目录
     * 用于存放需要落地的文件数据
     * 该目录无需权限
     * 用户在应用详情处清除数据会清空此目录
     * 
     * @param dir
     * 子目录
     * @return
     */
    public static File getApplicationExternalFilesDir(String dir)
    {
        return Foundation.getApplication().getExternalFilesDir(dir);
    }
    
    public static File[] getApplicationExternalFilesDirs()
    {
        return getApplicationExternalFilesDirs(null);
    }
    
    public static File[] getApplicationExternalFilesDirs(String dir)
    {
        if(VERSION.SDK_INT>=VERSION_CODES.KITKAT)
        {
            return Foundation.getApplication().getExternalFilesDirs(dir);
        }
        else
        {
            return new File[]{getApplicationExternalFilesDir(dir)};
        }
    }
    
    /**
     * 获取存储路径
     * 如果由于存储卡挂载原因获取不到则会返回一个根据规则拼装的路径
     * 适用于内核初始化等必须设置路径的逻辑
     * 
     * @param dir
     * @return
     */
    public static String getApplicationExternalFilesDirPath(String dir)
    {
        File file=getApplicationExternalFilesDir(dir);
        if(file!=null)
        {
            return file.getAbsolutePath();
        }
        /** 如果获取不到.一般是存储卡未挂载.返回一个拼装的路径 */
        String base=Environment.getExternalStorageDirectory()+File.separator+"Android"+File.separator+"data"+File.separator+Foundation.getApplication().getPackageName()+File.separator+"files";
        if(StringUtil.isEmptyWithTrim(dir))
        {
            return base;
        }
        return base+File.separator+dir;
    }
    
    /** 获取存储卡应用缓存根目录.一般不允许随意添加文件到根目录 */
    public static File getApplicationExternalCacheDir()
    {
        return getApplicationExternalCacheDir(null);
    }
    
    /**
     * 获取存储卡应用缓存子目录
     * 用于存放缓存数据
     * 该目录无需权限
     * 用户在应用详情处清除缓存会清空此目录
     * 
     * @param dir
     * @return
     */
    public static File getApplicationExternalCacheDir(String dir)
    {
        File root=Foundation.getApplication().getExternalCacheDir();
        if(StringUtil.isEmptyWithTrim(dir))
        {
            return root;
        }
        File file=new File(root,dir);
        if(!file.exists())
        {
            file.mkdirs();
        }
        return file;
    }
    
    private void getSysStorage()
    {
        /** 注意:这是用常规系统公开的方法获取存储空间.只能获取到一个 */
        if(VERSION.SDK_INT>=VERSION_CODES.HONEYCOMB_MR1)
        {
            getSysStorageByReflect(Foundation.getApplication());
        }
        else
        {
            getSysStorageByNormalAPI();
        }
    }
    
    /** 调用系统原生支持的接口去获取当前默认的存储器 */
    private void getSysStorageByNormalAPI()
    {
        setStoragePrimary();
        setPrimaryStorageIntoList();
    }
    
    /** 通过反射方式去获取存储器列表 */
    @TargetApi(VERSION_CODES.HONEYCOMB_MR1)
    private void getSysStorageByReflect(Context context)
    {
        try
        {
            /** 下面是用一些隐藏的借口获取存储空间.可以获得多个存储返回 */
            /** 这个类低版本的接口就有.但是其中的getVolumeList方法是API12以上才有 */
            Object storageManager=context.getSystemService("storage");
            /** 保存StorageVolume类型对象 */
            Class<?> storageVolumeClass;
            /** 这个类需要API12以上才有而且是@hide的 */
            String storageVolumeClassName="android.os.storage.StorageVolume";
            storageVolumeClass=Class.forName(storageVolumeClassName);
            Method methodGetVolumeList;
            /** 这个方法是StorageManager中的@hide方法 */
            methodGetVolumeList=storageManager.getClass().getDeclaredMethod("getVolumeList",(Class[])null);
            methodGetVolumeList.setAccessible(true);
            /** 通过getVolumeList方法取得所有StorageVolume的可用扇区 */
            Object[] storageVolume;
            storageVolume=(Object[])methodGetVolumeList.invoke(storageManager,new Object[]{});
            /** 取得一个扇区存储路径的方法 */
            Method methodGetPath=storageVolumeClass.getMethod("getPath");
            methodGetPath.setAccessible(true);
            /** 取得一个扇区是否可移动的方法 */
            Method methodIsremoveable=storageVolumeClass.getMethod("isRemovable");
            methodIsremoveable.setAccessible(true);
            for(int i=0;i<storageVolume.length;i++)
            {
                String path=(String)methodGetPath.invoke(storageVolume[i],new Object[]{});
                boolean removeable=(Boolean)methodIsremoveable.invoke(storageVolume[i],new Object[]{});
                mListStorageAll.add(path);
                if(removeable)
                {
                    mListStorageExternal.add(path);
                }
                else
                {
                    mListStorageInternal.add(path);
                }
                if(new File(path).canWrite())
                {
                    mListStorageAvaliable.add(path);
                }
            }
            setStoragePrimary();
            setPrimaryStorageIntoList();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            getSysStorageByNormalAPI();
        }
    }
    
    private void setPrimaryStorageIntoList()
    {
        if(!(mStoragePrimary==null||"".equalsIgnoreCase(mStoragePrimary)))
        {
            if(!mListStorageAll.contains(mStoragePrimary))
            {
                mListStorageAll.add(0,mStoragePrimary);
            }
            if(!mListStorageAvaliable.contains(mStoragePrimary))
            {
                mListStorageAvaliable.add(0,mStoragePrimary);
            }
            if(!mListStorageExternal.contains(mStoragePrimary))
            {
                /** 加到外置列表里 */
                mListStorageExternal.add(0,mStoragePrimary);
            }
            if(mListStorageInternal.contains(mStoragePrimary))
            {
                /** 如果在内置列表里面存在.将其去除 */
                mListStorageExternal.remove(mStoragePrimary);
            }
        }
    }
    
    /** 重新刷新存储器状态 */
    public void refreshStorageState()
    {
        mListStorageAll.clear();
        mListStorageAvaliable.clear();
        mListStorageExternal.clear();
        mListStorageInternal.clear();
        getSysStorage();
    }
    
    /** 获取所有存储器列表(包含模拟存储器) */
    public List<String> getStorageListAll()
    {
        return mListStorageAll;
    }
    
    /** 获取所有可用存储器列表(不包含模拟存储器) */
    public List<String> getStorageListAvailable()
    {
        return mListStorageAvaliable;
    }
    
    /** 返回所有外置可移动存储器列表 */
    public List<String> getStorageExternal()
    {
        return mListStorageExternal;
    }
    
    /** 返回所有内置不可移动存储器列表 */
    public List<String> getStorageInternal()
    {
        return mListStorageInternal;
    }
    
    /** 获取系统默认主存储器 */
    public String getStoragePrimary()
    {
        return mStoragePrimary;
    }
    
    /** 设置系统默认存储器内容 */
    private void setStoragePrimary()
    {
        IPermissionInjectInterface injectInterface=PermissionManager.getInstance().getPermissionInjectInterface();
        if(injectInterface!=null)
        {
            mExternalStorageWritable=injectInterface.isInjectExternalStorageAvailable();
        }
        mStoragePrimary=Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    
    /** 根据传入的路径.获取其对应的存储卡存储器所属根路径 */
    public String getStorageCategory(String path)
    {
        if(path==null)
        {
            return null;
        }
        if(path.startsWith("/sdcard/"))
        {
            String sdPath=getExternalStorageDirectoryPath();
            return sdPath;
        }
        String temp=path.trim().toLowerCase()+File.separatorChar;
        for(String item:mListStorageAvaliable)
        {
            if(temp.startsWith(item.toLowerCase()+File.separatorChar))
            {
                return item;
            }
        }
        return null;
    }
    
    /** 判断系统默认主存储器是否可读可写 */
    public boolean isDefaultStorageWritable()
    {
        return mExternalStorageWritable;
    }
    
    public static class DiskInfo
    {
        public long mTotalSize;
        public long mAvailableSize;
    }
}
