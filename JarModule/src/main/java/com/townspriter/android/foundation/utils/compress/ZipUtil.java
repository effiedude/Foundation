package com.townspriter.android.foundation.utils.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import com.townspriter.android.foundation.utils.io.FileUtils;
import com.townspriter.android.foundation.utils.io.IOUtil;
import com.townspriter.android.foundation.utils.text.StringUtil;

/******************************************************************************
 * @Path Foundation:ZipUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public final class ZipUtil
{
    /**
     * 这个方法会返回ZIP文件中所有的EntryName.注意是所有.如果ZIP里文件很多.会比较耗时
     * 
     * @param zipFilename
     * @param containFolder
     * 是否包含文件夹.某些ZIP压缩时会使用JunkPath.此时ZIP中不会有一些Folder结构
     * @param containFile
     * 是否包含文件
     * @return 不会返回空
     * @throws IOException
     */
    public static List<String> getEntryNames(String zipFilename,boolean containFolder,boolean containFile) throws IOException
    {
        ArrayList<String> entryNames=new ArrayList<String>(10);
        ZipFile zipFile=null;
        try
        {
            File parent=new File(zipFilename);
            zipFile=new ZipFile(zipFilename);
            Enumeration<? extends ZipEntry> zipEntries=zipFile.entries();
            while(zipEntries.hasMoreElements())
            {
                ZipEntry zipEntry=zipEntries.nextElement();
                String zipEntryName=zipEntry.getName();
                if(zipEntryName.contains("../"))
                {
                    continue;
                }
                File zipFileInner=new File(zipFilename+File.separator+zipEntryName);
                if(!FileUtils.isFileInDirectory(zipFileInner,parent))
                {
                    continue;
                }
                if(zipEntry.isDirectory())
                {
                    if(containFolder)
                    {
                        zipEntryName=zipEntryName.substring(0,zipEntryName.length()-1);
                        entryNames.add(zipEntryName);
                    }
                }
                else
                {
                    if(containFile)
                    {
                        entryNames.add(zipEntryName);
                    }
                }
            }
        }
        finally
        {
            IOUtil.safeClose(zipFile);
        }
        return entryNames;
    }
    
    public static boolean unZipFileEntry(String zipFilename,String entryName,File destFile)
    {
        ZipFile zipFile;
        try
        {
            zipFile=new ZipFile(zipFilename);
        }
        catch(IOException ioException)
        {
            return false;
        }
        boolean result=false;
        ZipEntry zipEntry=zipFile.getEntry(entryName);
        if(zipEntry!=null&&!zipEntry.isDirectory())
        {
            String parent=destFile.getParent();
            if(FileUtils.mkDirs(parent))
            {
                InputStream inputStream=null;
                FileOutputStream fileOut=null;
                result=true;
                try
                {
                    inputStream=zipFile.getInputStream(zipEntry);
                    fileOut=new FileOutputStream(destFile);
                    IOUtil.copy(inputStream,fileOut);
                }
                catch(IOException ioException)
                {
                    result=false;
                }
                finally
                {
                    IOUtil.safeClose(inputStream);
                    IOUtil.safeClose(fileOut);
                }
            }
        }
        IOUtil.safeClose(zipFile);
        return result;
    }
    
    /**
     * 递归解压压缩包中指定目录下的所有内容到一个目录中
     * 
     * @param sourceDirName
     * 指定压缩包中要解压出来的目录
     * @return
     * true:只要有任意内容解压出来
     * false:全部解压失败
     */
    public static boolean unZipDirectoryEntry(String zipFileName,String sourceDirName,String destDirName)
    {
        File destDir=new File(destDirName);
        if(!destDir.isDirectory()&&!destDir.mkdirs())
        {
            return false;
        }
        ZipFile zipFile;
        try
        {
            zipFile=new ZipFile(zipFileName);
        }
        catch(IOException ioException)
        {
            return false;
        }
        if(!sourceDirName.endsWith("/"))
        {
            sourceDirName+="/";
        }
        if(!destDirName.endsWith(File.separator))
        {
            destDirName+=File.separator;
        }
        boolean result=false;
        Enumeration<? extends ZipEntry> zipEntries=zipFile.entries();
        while(zipEntries.hasMoreElements())
        {
            ZipEntry zipEntry=zipEntries.nextElement();
            String entryName=zipEntry.getName();
            if(entryName.contains("../")||zipEntry.isDirectory()||!entryName.startsWith(sourceDirName))
            {
                continue;
            }
            String relativePath=entryName.substring(sourceDirName.length());
            String destFileName=destDirName+relativePath;
            if(relativePath.contains("/"))
            {
                File parentFile=new File(destFileName).getParentFile();
                if(!parentFile.isDirectory()&&!parentFile.mkdirs())
                {
                    continue;
                }
            }
            InputStream in=null;
            FileOutputStream out=null;
            try
            {
                in=zipFile.getInputStream(zipEntry);
                out=new FileOutputStream(destFileName);
                IOUtil.copy(in,out);
                result=true;
            }
            catch(IOException ioException)
            {}
            finally
            {
                IOUtil.safeClose(in);
                IOUtil.safeClose(out);
            }
        }
        IOUtil.safeClose(zipFile);
        return result;
    }
    
    /**
     * 解压一个压缩文档到指定位置.如果ZIP里的文件很多会很耗时
     *
     * @param zipFile
     * 压缩包的名字
     * @param destPath
     * 指定的路径
     * @throws IOException
     */
    public static void unZip(String zipFile,String destPath) throws IOException
    {
        ZipInputStream zipInputStream=null;
        try
        {
            zipInputStream=new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry;
            String entryName;
            while((zipEntry=zipInputStream.getNextEntry())!=null)
            {
                entryName=zipEntry.getName();
                if(entryName.contains("../"))
                {
                    continue;
                }
                File targetFile=new File(destPath,entryName);
                File destDir=new File(destPath);
                /** 判断创建的文件路径是否以指定的路径开始.否则拒绝解压 */
                if(!FileUtils.isFileInDirectory(targetFile,destDir))
                {
                    break;
                }
                if(zipEntry.isDirectory())
                {
                    File folder=new File(destPath,entryName);
                    folder.mkdirs();
                }
                else
                {
                    if(!targetFile.getParentFile().exists())
                    {
                        targetFile.getParentFile().mkdirs();
                    }
                    FileOutputStream out=null;
                    try
                    {
                        out=new FileOutputStream(targetFile);
                        IOUtil.copy(zipInputStream,out);
                    }
                    finally
                    {
                        IOUtil.safeClose(out);
                    }
                }
            }
        }
        finally
        {
            IOUtil.safeClose(zipInputStream);
        }
    }
    
    /**
     * 压缩文件或文件夹
     *
     * @param targetFile
     * 要压缩的文件/文件夹名字
     * @param destZipFile
     * 指定压缩的目的和名字
     * @return
     */
    public static boolean zip(String targetFile,String destZipFile)
    {
        /** 创建压缩包 */
        boolean ret=false;
        ZipOutputStream outZip=null;
        FileOutputStream fos=null;
        try
        {
            fos=new FileOutputStream(destZipFile);
            outZip=new ZipOutputStream(fos);
            /** 打开要输出的文件 */
            File file=new File(targetFile);
            /** 压缩 */
            ret=zipFiles(file.getParent()+File.separator,file.getName(),outZip);
            outZip.finish();
        }
        catch(Exception exception)
        {
            ret=false;
        }
        finally
        {
            /** 完成关闭 */
            IOUtil.safeClose(fos);
            IOUtil.safeClose(outZip);
        }
        return ret;
    }
    
    public static boolean zip(List<String> srcFiles,String zipFileString)
    {
        boolean ret=true;
        ZipOutputStream outZip=null;
        FileOutputStream fos=null;
        try
        {
            fos=new FileOutputStream(zipFileString);
            outZip=new ZipOutputStream(fos);
            for(String filePath:srcFiles)
            {
                if(StringUtil.isEmpty(filePath))
                {
                    continue;
                }
                File file=new File(filePath);
                boolean singleRes=zipFiles(file.getParent()+File.separator,file.getName(),outZip);
                if(!singleRes)
                {
                    ret=false;
                }
            }
            outZip.finish();
        }
        catch(Exception exception)
        {
            ret=false;
        }
        finally
        {
            IOUtil.safeClose(fos);
            IOUtil.safeClose(outZip);
        }
        return ret;
    }
    
    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @ret
     */
    private static boolean zipFiles(String folderString,String fileString,ZipOutputStream zipOutputSteam)
    {
        if(zipOutputSteam==null)
        {
            return false;
        }
        boolean ret=false;
        File file=new File(folderString+fileString);
        ZipEntry zipEntry=null;
        FileInputStream inputStream=null;
        /** 判断是不是文件 */
        try
        {
            if(file.isFile())
            {
                zipEntry=new ZipEntry(fileString);
                inputStream=new FileInputStream(file);
                zipOutputSteam.putNextEntry(zipEntry);
                IOUtil.copy(inputStream,zipOutputSteam);
            }
            else
            {
                /** 文件夹的方式.获取文件夹下的子文件 */
                String[] fileList=file.list();
                /** 如果没有子文件.则添加进去即可 */
                if(fileList==null||fileList.length<=0)
                {
                    zipEntry=new ZipEntry(fileString+File.separator);
                    zipOutputSteam.putNextEntry(zipEntry);
                }
                else
                {
                    /** 如果有子文件.遍历子文件 */
                    for(int i=0;i<fileList.length;i++)
                    {
                        zipFiles(folderString,fileString+File.separator+fileList[i],zipOutputSteam);
                    }
                }
            }
            ret=true;
        }
        catch(Exception exceptionOne)
        {
            ret=false;
        }
        finally
        {
            if(zipOutputSteam!=null)
            {
                try
                {
                    zipOutputSteam.closeEntry();
                }
                catch(Exception exceptionTwo)
                {}
            }
            IOUtil.safeClose(inputStream);
        }
        return ret;
    }
    
    public static byte[] gzip(byte[] data)
    {
        if(null==data||0==data.length)
        {
            return null;
        }
        byte[] result=null;
        ByteArrayOutputStream baos=null;
        GZIPOutputStream gos=null;
        try
        {
            baos=new ByteArrayOutputStream();
            gos=new GZIPOutputStream(baos);
            gos.write(data);
            gos.finish();
            gos.flush();
            baos.flush();
            result=baos.toByteArray();
        }
        catch(IOException ioException)
        {}
        finally
        {
            IOUtil.safeClose(baos);
            IOUtil.safeClose(gos);
        }
        return result;
    }
    
    public static byte[] unGzip(byte[] data)
    {
        if(null==data||0==data.length)
        {
            return null;
        }
        byte[] result=null;
        ByteArrayOutputStream baos=null;
        ByteArrayInputStream bais=null;
        GZIPInputStream gis=null;
        try
        {
            baos=new ByteArrayOutputStream();
            bais=new ByteArrayInputStream(data);
            gis=new GZIPInputStream(bais);
            IOUtil.copy(gis,baos);
            result=baos.toByteArray();
        }
        catch(IOException ioException)
        {}
        finally
        {
            IOUtil.safeClose(bais);
            IOUtil.safeClose(baos);
            IOUtil.safeClose(gis);
        }
        return result;
    }
}