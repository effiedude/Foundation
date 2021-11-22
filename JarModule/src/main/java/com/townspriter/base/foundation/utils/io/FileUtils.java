package com.townspriter.base.foundation.utils.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.text.StringUtil;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

/******************************************************************************
 * @path Foundation:FileUtils
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 17:20:35
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class FileUtils
{
    private static final String TAG="FileUtils";
    private static final String URIxSCHEMAxPRIMARY="primary";
    private static final String URIxSCHEMAxIMAGE="image";
    private static final String URIxSCHEMAxVIDEO="video";
    private static final String URIxSCHEMAxAUDIO="audio";
    private static final String URIxSCHEMAxCONTENT="content";
    private static final String URIxSCHEMAxFILE="file";
    private static final String FILExPATHxQQxBROWSER="/QQBrowser";
    private static final String BY="B";
    private static final String KB="KB";
    private static final String MB="MB";
    private static final String GB="GB";
    private static final String TB="TB";
    /** 流量单位进率 */
    private static final long SIZExRATE=1024L;
    private static final long ONExKB=SIZExRATE;
    private static final long ONExMB=ONExKB*SIZExRATE;
    private static final long ONExGB=ONExMB*SIZExRATE;
    
    public static void openFileManagerFromActivity(@NonNull Activity activity,@NonNull String mime,@IntRange(from=0) int requestCode)
    {
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        /** 设置类型 */
        intent.setType(mime);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent,requestCode);
    }
    
    public static void openFileManagerFromFragment(@NonNull Fragment fragment,@NonNull String mime,@IntRange(from=0) int requestCode)
    {
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        /** 设置类型 */
        intent.setType(mime);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fragment.startActivityForResult(intent,requestCode);
    }
    
    public static @Nullable String tryGetPathFromFileManager(@NonNull Context context,int resultCode,@Nullable Intent intent)
    {
        if(resultCode==Activity.RESULT_OK)
        {
            if(intent==null)
            {
                Logger.d(TAG,"onActivityResult-data:NULL");
                return null;
            }
            Uri uri=intent.getData();
            if(uri==null)
            {
                Logger.d(TAG,"onActivityResult-uri:NULL");
                return null;
            }
            String filePath;
            // 使用第三方应用打开
            if(URIxSCHEMAxFILE.equalsIgnoreCase(uri.getScheme()))
            {
                filePath=uri.getPath();
                return filePath;
            }
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT)
            {
                // 4.4以后
                filePath=FileUtils.getPath(context,uri);
            }
            else
            {
                // 4.4以前
                filePath=FileUtils.getRealPathFromUri(context,uri);
            }
            return filePath;
        }
        return null;
    }
    
    public static String formatSize(String path)
    {
        return formatSize(new File(path).length());
    }
    
    public static String formatSizeByLog(long size)
    {
        if(size<=0)
        {
            return "0";
        }
        final String[] units=new String[]{BY,KB,MB,GB,TB};
        int digitGroups=(int)(Math.log(size)/Math.log(1024));
        DecimalFormat decimalFormat=new DecimalFormat("###.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        return decimalFormat.format(size/Math.pow(1024,digitGroups))+units[digitGroups];
    }
    
    public static Uri getUriFromFile(@NonNull Context context,@NonNull File file,@NonNull String applicationID)
    {
        if(context==null||file==null)
        {
            throw new NullPointerException();
        }
        Uri uri;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            uri=FileProvider.getUriForFile(context.getApplicationContext(),applicationID+".fileprovider",file);
        }
        else
        {
            uri=Uri.fromFile(file);
        }
        return uri;
    }
    
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context,final Uri uri)
    {
        final boolean isKitKat=Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
        if(isKitKat&&DocumentsContract.isDocumentUri(context,uri))
        {
            // 一些三方的文件浏览器会进入到这个方法中
            // 腾讯文件管理器不在此列
            if(isExternalStorageDocument(uri))
            {
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[] split=docId.split(":");
                final String type=split[0];
                if(URIxSCHEMAxPRIMARY.equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }
            }
            else if(isDownloadsDocument(uri))
            {
                final String id=DocumentsContract.getDocumentId(uri);
                final Uri contentUri=ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(id));
                return getDataColumn(context,contentUri,null,null);
            }
            else if(isMediaDocument(uri))
            {
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[] split=docId.split(":");
                final String type=split[0];
                Uri contentUri=null;
                if(URIxSCHEMAxIMAGE.equals(type))
                {
                    contentUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if(URIxSCHEMAxVIDEO.equals(type))
                {
                    contentUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if(URIxSCHEMAxAUDIO.equals(type))
                {
                    contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection="_id=?";
                final String[] selectionArgs=new String[]{split[1]};
                return getDataColumn(context,contentUri,selection,selectionArgs);
            }
        }
        else if(URIxSCHEMAxCONTENT.equalsIgnoreCase(uri.getScheme()))
        {
            if(isGooglePhotosUri(uri))
            {
                return uri.getLastPathSegment();
            }
            if(isQQMediaDocument(uri))
            {
                String path=uri.getPath();
                File fileDir=Environment.getExternalStorageDirectory();
                File file=new File(fileDir,path.substring(FILExPATHxQQxBROWSER.length()));
                return file.exists()?file.toString():null;
            }
            return getDataColumn(context,uri,null,null);
        }
        else if(URIxSCHEMAxFILE.equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }
        return null;
    }
    
    public static String getRealPathFromUri(Context context,Uri contentUri)
    {
        return getDataColumn(context,contentUri,null,null);
    }
    
    public static String getDataColumn(Context context,Uri uri,String selection,String[] selectionArgs)
    {
        Cursor cursor=null;
        final String column=MediaStore.MediaColumns.DATA;
        final String[] projection={column};
        try
        {
            cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
            if(cursor!=null&&cursor.moveToFirst())
            {
                final int columnIndex=cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        }
        finally
        {
            if(cursor!=null)
            {
                cursor.close();
            }
        }
        return null;
    }
    
    /** 计算文件夹大小 */
    public static long sizeOf(File file)
    {
        if(file==null||!file.exists())
        {
            return 0L;
        }
        if(file.isDirectory())
        {
            long totalSize=0L;
            File[] fileList=file.listFiles();
            if(null!=fileList)
            {
                for(File subItem:fileList)
                {
                    totalSize+=sizeOf(subItem);
                }
            }
            return totalSize;
        }
        else
        {
            return file.length();
        }
    }
    
    public static List<String> readLines(String fileName) throws IOException
    {
        return readLines(new File(fileName));
    }
    
    public static List<String> readLines(File file) throws IOException
    {
        FileInputStream inputStream=null;
        try
        {
            inputStream=new FileInputStream(file);
            return IOUtil.readLines(inputStream);
        }
        finally
        {
            IOUtil.safeClose(inputStream);
        }
    }
    
    /**
     * 覆盖文本到目标文件中
     *
     * @param file
     * 目标文件
     * @param lines
     * 数据源
     * @throws IOException
     * 输入或输出错误
     */
    public static void writeLines(File file,Collection<?> lines) throws IOException
    {
        writeLines(file,lines,null,false);
    }
    
    /**
     * 添加或覆盖文本到目标文件中
     *
     * @param file
     * 目标文件
     * @param lines
     * 数据源
     * @param isAppend
     * true: 添加数据源到文件尾
     * false:覆盖数据源到源文件
     * @throws IOException
     * 输入或输出错误
     */
    public static void writeLines(File file,Collection<?> lines,String lineEnding,boolean isAppend) throws IOException
    {
        File parentDir=file.getParentFile();
        if(parentDir!=null&&!parentDir.exists())
        {
            parentDir.mkdirs();
        }
        BufferedOutputStream bufferedOutputStream=null;
        FileOutputStream fileOutputStream=null;
        try
        {
            fileOutputStream=new FileOutputStream(file,isAppend);
            bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
            IOUtil.writeLines(lines,lineEnding,bufferedOutputStream);
            bufferedOutputStream.flush();
        }
        finally
        {
            IOUtil.safeClose(fileOutputStream);
            IOUtil.safeClose(bufferedOutputStream);
        }
    }
    
    /**
     * 拷贝源文件到目标文件目录下.如果目标文件目录下存在相同文件则覆盖
     *
     * @param srcFile
     * 源文件(不能为目录)
     * @param destDir
     * 目标文件目录
     * @throws IOException
     * 输入输出流错误
     */
    public static void copyFileToDirectory(@NonNull File srcFile,@NonNull File destDir) throws IOException
    {
        File destFile=new File(destDir,srcFile.getName());
        copyFile(srcFile,destFile);
    }
    
    /**
     * 拷贝源文件到目标文件.如果目标文件存在则覆盖
     *
     * @param srcFile
     * 源文件(不能为目录)
     * @param destFile
     * 目标文件(不能为目录)
     * @throws IOException
     * 输入输出流错误
     */
    public static void copyFile(@NonNull File srcFile,@NonNull File destFile) throws IOException
    {
        File parent=destFile.getParentFile();
        if(!parent.exists())
        {
            parent.mkdirs();
        }
        if(srcFile.isDirectory())
        {
            throw new IOException("源文件是一个目录");
        }
        if(destFile.isDirectory())
        {
            throw new IOException("目标文件是一个目录");
        }
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try
        {
            inputStream=new FileInputStream(srcFile);
            outputStream=new FileOutputStream(destFile);
            int read;
            byte[] buffer=new byte[64*1024];
            while((read=inputStream.read(buffer))!=-1)
            {
                outputStream.write(buffer,0,read);
            }
        }
        finally
        {
            IOUtil.safeClose(inputStream);
            IOUtil.safeClose(outputStream);
        }
    }
    
    /**
     * 将源文件夹复制到目标文件夹
     *
     * @param srcDir
     * 源文件夹
     * @param destDir
     * 目标文件夹(源文件夹的新名称及路径)
     * @throws IOException
     */
    public static void copyDirectory(File srcDir,File destDir) throws IOException
    {
        if(!srcDir.isDirectory())
        {
            throw new IOException("源文件不是一个目录");
        }
        File[] srcFiles=srcDir.listFiles();
        if(srcFiles!=null)
        {
            if(destDir.exists())
            {
                if(!destDir.isDirectory())
                {
                    throw new IOException("目标文件不是一个目录");
                }
            }
            else
            {
                if(!destDir.mkdirs()&&!destDir.isDirectory())
                {
                    throw new IOException("目标文件夹不能被创建");
                }
            }
            for(File srcFile:srcFiles)
            {
                File destFile=new File(destDir,srcFile.getName());
                if(srcFile.isDirectory())
                {
                    copyDirectory(srcFile,destFile);
                }
                else
                {
                    copyFile(srcFile,destFile);
                }
            }
        }
    }
    
    public static boolean delete(File file)
    {
        boolean result=false;
        if(file!=null)
        {
            if(file.isDirectory())
            {
                cleanDirectory(file);
            }
            try
            {
                result=file.delete();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return result;
    }
    
    /** 清空目录中的内容 */
    public static void cleanDirectory(File directory)
    {
        if(directory!=null&&directory.isDirectory())
        {
            File[] files=directory.listFiles();
            if(files!=null)
            {
                for(File file:files)
                {
                    delete(file);
                }
            }
        }
    }
    
    /**
     * 过滤出目标目录下所有符合条件的文件
     *
     * @param dir
     * 目标目录
     * @param filter
     * 过滤器
     * @param recursive
     * 是否递归过滤
     * @return 目录下所有符合条件的文件
     */
    public static List<File> listFiles(File dir,FileFilter filter,boolean recursive)
    {
        List<File> result=new ArrayList<>();
        if(!dir.exists()||dir.isFile())
        {
            return result;
        }
        File[] fArray=dir.listFiles(filter);
        if(fArray==null)
        {
            return result;
        }
        List<File> fList=Arrays.asList(fArray);
        if(!recursive)
        {
            return fList;
        }
        LinkedList<File> linkedList=new LinkedList<>(fList);
        while(!linkedList.isEmpty())
        {
            File file=linkedList.removeFirst();
            result.add(file);
            if(file.isDirectory())
            {
                File[] array=file.listFiles(filter);
                if(array==null)
                {
                    continue;
                }
                for(int i=0;i<array.length;i++)
                {
                    linkedList.addLast(array[i]);
                }
            }
        }
        return result;
    }
    
    public static boolean mkDirs(String filePath)
    {
        if(TextUtils.isEmpty(filePath))
        {
            return false;
        }
        File dir=new File(filePath);
        if(!dir.exists())
        {
            try
            {
                dir.mkdirs();
            }
            catch(SecurityException securityException)
            {
                AssertUtil.fail(securityException);
            }
        }
        return dir.isDirectory();
    }
    
    public static boolean writeBytes(String filePath,String fileName,byte[] data)
    {
        if(data==null)
        {
            return false;
        }
        return writeBytes(filePath,fileName,data,0,data.length);
    }
    
    /**
     * 注意:请尽量将这个函数放入后台线程执行.防止引入严重卡顿
     *
     * @param filePath
     * @param fileName
     * @param headData
     * @param bodyData
     * @param bodyOffset
     * @param bodyLen
     * @param forceFlush
     * 请慎重使用这个参数.如果设置为强制写入可能导致严重卡顿.甚至出现程序无响应.如果不是极为重要的数据.请不要设置为强制刷新数据
     * @return
     */
    public static boolean writeBytes(String filePath,String fileName,byte[] headData,byte[] bodyData,int bodyOffset,int bodyLen,boolean forceFlush) throws IOException
    {
        if(StringUtil.isEmpty(filePath)||StringUtil.isEmpty(fileName)||bodyData==null)
        {
            return false;
        }
        File file=createNewFile(filePath+fileName);
        boolean result=writeBytesBase(file,headData,bodyData,bodyOffset,bodyLen,forceFlush);
        return result;
    }
    
    public static boolean writeBytes(String filePath,String fileName,byte[] data,int offset,int len)
    {
        try
        {
            return writeBytes(filePath,fileName,null,data,offset,len,false);
        }
        catch(Exception exception)
        {
            return false;
        }
    }
    
    /**
     * 注意:请尽量将这个函数放入后台线程执行.防止引入严重卡顿
     *
     * @param headData
     * @param bodyData
     * @param bodyOffset
     * @param bodyLen
     * @param forceFlush
     * 请慎重使用这个参数.如果设置为强制写入可能导致严重卡顿.甚至出现程序无响应.如果不是极为重要的数据.请不要设置为强制刷新数据
     * @return
     */
    private static boolean writeBytesBase(File file,byte[] headData,byte[] bodyData,int bodyOffset,int bodyLen,boolean forceFlush) throws IOException
    {
        FileOutputStream fileOutput=null;
        try
        {
            fileOutput=new FileOutputStream(file);
            if(headData!=null)
            {
                fileOutput.write(headData);
            }
            fileOutput.write(bodyData,bodyOffset,bodyLen);
            fileOutput.flush();
            if(forceFlush)
            {
                FileDescriptor fileOutputFD=fileOutput.getFD();
                if(fileOutputFD!=null)
                {
                    /** 立刻刷新.保证文件可以正常写入 */
                    fileOutputFD.sync();
                }
            }
            return true;
        }
        finally
        {
            IOUtil.safeClose(fileOutput);
        }
    }
    
    /**
     * 注意:请尽量将这个函数放入后台线程执行.防止引入严重卡顿
     *
     * @param headData
     * @param bodyData
     * @param bodyOffset
     * @param bodyLen
     * @param forceFlush
     * 请慎重使用这个参数.如果设置为强制写入可能导致严重卡顿.甚至出现程序无响应.如果不是极为重要的数据.请不要设置为强制刷新数据
     * @return
     */
    public static boolean writeBytes(File file,byte[] headData,byte[] bodyData,int bodyOffset,int bodyLen,boolean forceFlush)
    {
        try
        {
            return writeBytesBase(file,headData,bodyData,bodyOffset,bodyLen,forceFlush);
        }
        catch(FileNotFoundException fileNotFoundException)
        {
            fileNotFoundException.printStackTrace();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        return false;
    }
    
    public static boolean writeBytes(File file,byte[] data,int offset,int len)
    {
        return writeBytes(file,null,data,offset,len,false);
    }
    
    public static File createNewFile(String path)
    {
        return createNewFile(path,false);
    }
    
    public static boolean rename(File file,String newName)
    {
        return file.renameTo(new File(newName));
    }
    
    public static boolean delete(String path)
    {
        return delete(new File(path));
    }
    
    /**
     * @param path
     * 目标文件路径
     * @param append
     * 若存在原始文件是否覆盖文件
     * @return File
     */
    public static File createNewFile(String path,boolean append)
    {
        File newFile=new File(path);
        if(!append)
        {
            if(newFile.exists())
            {
                newFile.delete();
            }
        }
        if(!newFile.exists())
        {
            try
            {
                File parent=newFile.getParentFile();
                if(parent!=null&&!parent.exists())
                {
                    parent.mkdirs();
                }
                newFile.createNewFile();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return newFile;
    }
    
    public static byte[] readBytes(String filePath)
    {
        if(StringUtil.isEmpty(filePath))
        {
            return null;
        }
        return readBytes(new File(filePath));
    }
    
    public static byte[] readBytes(File file)
    {
        FileInputStream fileInput=null;
        try
        {
            if(file.exists())
            {
                fileInput=new FileInputStream(file);
                return IOUtil.readBytes(fileInput);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(fileInput);
        }
        return null;
    }
    
    /**
     * 判读目录是否存在
     *
     * @param directory
     * 目录路径
     * @return
     */
    public static boolean isDirectoryExists(String directory)
    {
        if(!StringUtil.isEmpty(directory))
        {
            try
            {
                if(new File(directory).isDirectory())
                {
                    return true;
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return false;
    }
    
    public static boolean isFileExists(String strFile)
    {
        if(StringUtil.isEmpty(strFile))
        {
            return false;
        }
        try
        {
            File file=new File(strFile);
            return file.exists();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取传入路径下文件大小.不存在则文件大小为零
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path)
    {
        long size=0;
        File file=new File(path);
        if(file.exists())
        {
            size=file.length();
        }
        return size;
    }
    
    /**
     * 判断一个文件是否是一个软链接
     *
     * @param filePath
     * @return
     */
    public static boolean isSystemLink(String filePath)
    {
        if(StringUtil.isEmptyWithTrim(filePath))
        {
            return false;
        }
        File file=new File(filePath);
        File canon=null;
        if(file.getParent()==null)
        {
            canon=file;
        }
        else
        {
            File canonDir;
            try
            {
                canonDir=file.getParentFile().getCanonicalFile();
                canon=new File(canonDir,file.getName());
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
        try
        {
            return canon!=null&&!canon.getCanonicalFile().equals(canon.getAbsoluteFile());
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        return false;
    }
    
    public static boolean isFileInDirectory(File file,File directory)
    {
        if(null==file||null==directory)
        {
            return false;
        }
        boolean ret=false;
        try
        {
            String destFilePath=file.getCanonicalPath();
            String destDirPath=directory.getCanonicalPath();
            if(destDirPath.endsWith("/")==false)
            {
                destDirPath=destDirPath+"/";
            }
            ret=destFilePath.startsWith(destDirPath);
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        return ret;
    }
    
    public static byte[] readFile(InputStream inputStream)
    {
        if(inputStream==null)
        {
            return null;
        }
        int len=0;
        try
        {
            len=inputStream.available();
        }
        catch(IOException ioExceptionOne)
        {
            ioExceptionOne.printStackTrace();
        }
        if(len<=0)
        {
            return null;
        }
        byte[] byteArray=new byte[len];
        try
        {
            inputStream.read(byteArray);
        }
        catch(IOException ioExceptionTwo)
        {
            ioExceptionTwo.printStackTrace();
        }
        try
        {
            inputStream.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return byteArray;
    }
    
    public static String formatSize(long size)
    {
        String result;
        if(size>=ONExGB)
        {
            result=decimalFormat(size/ONExGB)+GB;
        }
        else if(size>=ONExMB)
        {
            result=decimalFormat(size/ONExMB)+MB;
        }
        else
        {
            result=decimalFormat(size/ONExKB)+KB;
        }
        return result;
    }
    
    /** 检测存储卡是否已挂载 */
    public static boolean isDiskMounted()
    {
        boolean isMounted=false;
        try
        {
            isMounted=Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return isMounted;
    }
    
    public static void broadcastScannerMedia(@NonNull String filePath)
    {
        Intent scanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        Foundation.getApplication().sendBroadcast(scanIntent);
    }
    
    /** 更新媒体库 */
    public static void broadcastMountedMedia(@NonNull String filePath)
    {
        if(TextUtils.isEmpty(filePath))
        {
            return;
        }
        /** 版本号的判断4.4为分水岭.发送广播更新媒体库 */
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            MediaScannerConnection.scanFile(Foundation.getApplication(),new String[]{filePath},null,null);
        }
        else
        {
            /** 注意:线上有些三星4.2.2的手机报出了没有发送该广播权限的崩溃.无法复现.增加一个异常规避 */
            try
            {
                Foundation.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.fromFile(new File(filePath))));
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
    
    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    
    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    
    public static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
    public static boolean isQQMediaDocument(Uri uri)
    {
        return "com.tencent.mtt.fileprovider".equals(uri.getAuthority());
    }
    
    public static boolean isGooglePhotosUri(Uri uri)
    {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    
    /** 仅保留一位小数 */
    private static String decimalFormat(double size)
    {
        return String.format("%.1f",size);
    }
}
