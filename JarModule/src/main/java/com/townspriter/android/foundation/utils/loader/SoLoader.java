package com.townspriter.android.foundation.utils.loader;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.text.StringUtil;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import androidx.annotation.NonNull;

/******************************************************************************
 * @Path Foundation:SoLoader
 * @Describe 加载指定目录的库文件
 * 提供两种方法加载库文件
 * 方法一:加压安装目录下的安装包.获取库文件然后加载
 * 优点:不会增加原有APK包大小
 * 缺点:增加解压安装包操作
 * 方法二:在Assets中添加库文件加载该库文件
 * 优点:减少解压步骤
 * 缺点:增加安装包原有大小
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class SoLoader
{
    public static final String NORMALxLOADxERROR="NORMALxLOADxERROR";
    public static final String RExLOADxERROR="RExLOADxERROR";
    private static final String TAG="SoLoader";
    private static final int MAXxTRIES=5;
    private static final int COPYxBUFFERxSIZE=4096;
    private static final String MARKxRUNTIMExNORMALxLOAD="MARKxRUNTIMExNORMALxLOAD";
    private static final String MARKxSYSTEMxLOAD="MARKxSYSTEMxLOAD";
    private static final String MARKxINSTALLxLIBRARY="MARKxINSTALLxLIBRARY";
    private static final String MARKxEXTRACTxLIBRARY="MARKxEXTRACTxLIBRARY";
    private static final String MARKxFOUNDxAPK="MARKxFOUNDxAPK";
    private static final String DATAxFILExREADxWRITExERROR="DATAxFILExREADxWRITExERROR";
    
    public static void loadLibrary(@NonNull String library)
    {
        loadLibrary(library,null);
    }
    
    public static void loadLibrary(@NonNull String library,IRemoteTrace listener)
    {
        try
        {
            if(!TextUtils.isEmpty(library))
            {
                Runtime.getRuntime().loadLibrary(library);
            }
        }
        catch(Throwable throwable)
        {
            if(listener!=null)
            {
                String logContent=mergeLogContent(NORMALxLOADxERROR,MARKxRUNTIMExNORMALxLOAD,"lib"+library+".so",throwable.toString());
                listener.traceContent(logContent);
            }
            Application appContext=Foundation.getApplication();
            if(appContext!=null)
            {
                String[] armeabi=new String[]{"armeabi"};
                String appPackageName=appContext.getPackageName();
                final String soName="lib"+library+".so";
                String soPath="/data"+Environment.getDataDirectory().getAbsolutePath()+"/"+appPackageName+"/"+soName;
                installLibrary(appContext,armeabi,soName,new File(soPath),listener);
            }
        }
    }
    
    private static String[] sourceDirectories(final Context context)
    {
        final ApplicationInfo appInfo=context.getApplicationInfo();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP&&appInfo.splitSourceDirs!=null&&appInfo.splitSourceDirs.length!=0)
        {
            String[] apks=new String[appInfo.splitSourceDirs.length+1];
            apks[0]=appInfo.sourceDir;
            System.arraycopy(appInfo.splitSourceDirs,0,apks,1,appInfo.splitSourceDirs.length);
            return apks;
        }
        else
        {
            return new String[]{appInfo.sourceDir};
        }
    }
    
    private static ZipFileInZipEntry findAPKWithLibrary(final Context context,final String[] abis,final String mappedLibraryName)
    {
        ZipFile zipFile=null;
        for(String sourceDir:sourceDirectories(context))
        {
            int tries=0;
            while(tries++<MAXxTRIES)
            {
                try
                {
                    zipFile=new ZipFile(new File(sourceDir),ZipFile.OPEN_READ);
                    break;
                }
                catch(IOException ignored)
                {}
            }
            if(zipFile==null)
            {
                continue;
            }
            tries=0;
            while(tries++<MAXxTRIES)
            {
                String jniNameInApk;
                ZipEntry libraryEntry;
                for(final String abi:abis)
                {
                    jniNameInApk="lib"+File.separatorChar+abi+File.separatorChar+mappedLibraryName;
                    libraryEntry=zipFile.getEntry(jniNameInApk);
                    if(libraryEntry!=null)
                    {
                        return new ZipFileInZipEntry(zipFile,libraryEntry);
                    }
                }
            }
            try
            {
                zipFile.close();
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
        return null;
    }
    
    private static long copy(InputStream in,OutputStream out) throws IOException
    {
        long copied=0;
        byte[] buf=new byte[COPYxBUFFERxSIZE];
        while(true)
        {
            int read=in.read(buf);
            if(read==-1)
            {
                break;
            }
            out.write(buf,0,read);
            copied+=read;
        }
        out.flush();
        return copied;
    }
    
    private static void closeSilently(final Closeable closeable)
    {
        try
        {
            if(closeable!=null)
            {
                closeable.close();
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    
    // Loop over all APK's again in order to detect which ABI's are actually supported.
    // This second loop is more expensive than trying to find a specific ABI, so it should
    // only be ran when no matching libraries are found. This should keep the overhead of
    // the happy path to a minimum.
    private static String[] getSupportedABIs(Context context,String mappedLibraryName)
    {
        String p="lib"+File.separatorChar+"([^\\"+File.separatorChar+"]*)"+File.separatorChar+mappedLibraryName;
        Pattern pattern=Pattern.compile(p);
        ZipFile zipFile;
        Set<String> supportedABIs=new HashSet<>();
        for(String sourceDir:sourceDirectories(context))
        {
            try
            {
                zipFile=new ZipFile(new File(sourceDir),ZipFile.OPEN_READ);
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
                continue;
            }
            Enumeration<? extends ZipEntry> elements=zipFile.entries();
            while(elements.hasMoreElements())
            {
                ZipEntry el=elements.nextElement();
                Matcher match=pattern.matcher(el.getName());
                if(match.matches())
                {
                    supportedABIs.add(match.group(1));
                }
            }
        }
        String[] result=new String[supportedABIs.size()];
        return supportedABIs.toArray(result);
    }
    
    public static void installLibrary(final Context context,final String[] abis,final String mappedLibraryName,final File destination)
    {
        installLibrary(context,abis,mappedLibraryName,destination,null);
    }
    
    /**
     * 方法1、通过解压/data/目录下当前APP的apk包来获取so文件，然后进行load so。
     * 调用举例：
     * String [] abis = new String[]{"armeabi"};
     * String appPackageName = getAppContext().getPackageName();
     * final String soName = "libmultiscreen-jni.so";
     * String soPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
     * appPackageName + "/" + soName;
     * SoLoader.installLibrary(getAppContext(), abis, soName, new File(soPath));;
     * 
     * @param context
     * @param abis
     * @param mappedLibraryName
     * @param destination
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void installLibrary(final Context context,final String[] abis,final String mappedLibraryName,final File destination,IRemoteTrace listener)
    {
        ZipFileInZipEntry found=null;
        try
        {
            found=findAPKWithLibrary(context,abis,mappedLibraryName);
            if(found==null)
            {
                String[] supportedABIs;
                try
                {
                    supportedABIs=getSupportedABIs(context,mappedLibraryName);
                }
                catch(Exception exception)
                {
                    supportedABIs=new String[1];
                    supportedABIs[0]=exception.toString();
                }
                return;
            }
            int tries=0;
            while(tries++<MAXxTRIES)
            {
                try
                {
                    if(!destination.exists()&&!destination.createNewFile())
                    {
                        continue;
                    }
                }
                catch(IOException ioException)
                {
                    ioException.printStackTrace();
                    continue;
                }
                InputStream inputStream=null;
                FileOutputStream fileOut=null;
                try
                {
                    inputStream=found.zipFile.getInputStream(found.zipEntry);
                    fileOut=new FileOutputStream(destination);
                    final long written=copy(inputStream,fileOut);
                    fileOut.getFD().sync();
                    if(written!=destination.length())
                    {
                        continue;
                    }
                }
                catch(FileNotFoundException fileNotFoundException)
                {
                    fileNotFoundException.printStackTrace();
                    continue;
                }
                catch(IOException ioException)
                {
                    ioException.printStackTrace();
                    continue;
                }
                finally
                {
                    closeSilently(inputStream);
                    closeSilently(fileOut);
                }
                destination.setReadable(true,true);
                destination.setExecutable(true,true);
                destination.setWritable(true);
                try
                {
                    System.load(destination.getAbsolutePath());
                }
                catch(Throwable throwable)
                {
                    if(listener!=null)
                    {
                        String logContent=mergeLogContent(RExLOADxERROR,MARKxSYSTEMxLOAD,mappedLibraryName,throwable.toString());
                        listener.traceContent(logContent);
                    }
                    throwable.printStackTrace();
                }
                return;
            }
            if(listener!=null)
            {
                String logContent=mergeLogContent(RExLOADxERROR,MARKxEXTRACTxLIBRARY,mappedLibraryName,"加载库文件失败");
                listener.traceContent(logContent);
            }
        }
        catch(Exception exception)
        {
            if(listener!=null)
            {
                String logContent=mergeLogContent(RExLOADxERROR,MARKxINSTALLxLIBRARY,mappedLibraryName,exception.toString());
                listener.traceContent(logContent);
            }
        }
        finally
        {
            try
            {
                if(found!=null&&found.zipFile!=null)
                {
                    found.zipFile.close();
                }
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }
    
    private static String mergeLogContent(String tag,String mark,String soName,String e)
    {
        return StringUtil.merge(tag," soName:"+soName+"@"+mark+"@exception:"+e);
    }
    
    /**
     * 方法2、通过load已放置在asset的so文件来加载so。
     * 调用举例：
     * String appPackageName = getAppContext().getPackageName();
     * final String soName = "libmultiscreen-jni.so";
     * String soPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
     * appPackageName + "/" + soName;
     * SoLoader.reloadLibrary(getAppContext,soPath, soName)
     *
     * @param soPath
     * @param soName
     */
    private static void reloadLibrary(Context context,String soPath,String soName)
    {
        if(context==null)
        {
            return;
        }
        InputStream is=null;
        FileOutputStream fos=null;
        try
        {
            if(!(new File(soPath).exists()))
            {
                is=context.getResources().getAssets().open(soName);
                fos=new FileOutputStream(soPath);
                byte[] buffer=new byte[1024];
                int count;
                while((count=is.read(buffer))>0)
                {
                    fos.write(buffer,0,count);
                }
            }
        }
        catch(Exception exception)
        {
            Logger.e(TAG,DATAxFILExREADxWRITExERROR);
        }
        finally
        {
            if(is!=null)
            {
                try
                {
                    is.close();
                }
                catch(IOException ioException)
                {
                    Logger.e(TAG,DATAxFILExREADxWRITExERROR);
                }
            }
            if(fos!=null)
            {
                try
                {
                    fos.close();
                }
                catch(IOException ioException)
                {
                    Logger.e(TAG,DATAxFILExREADxWRITExERROR);
                }
            }
        }
        try
        {
            System.load(soPath);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
    
    private static class ZipFileInZipEntry
    {
        public ZipFile zipFile;
        public ZipEntry zipEntry;
        
        public ZipFileInZipEntry(ZipFile zipFile,ZipEntry zipEntry)
        {
            this.zipFile=zipFile;
            this.zipEntry=zipEntry;
        }
    }
}
