package com.townspriter.base.foundation.utils.device;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.io.IOUtil;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.text.TextUtils;

/******************************************************************************
 * @path MemoryUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class MemoryUtil
{
    private static final String MEMORYxINFOxPATH="/proc/meminfo";
    private static final String MEMORYxOCUPIEDxINFOxPATH="/proc/self/status";
    private static final String MEMORYxINFOxTAGxVMxRSS="VmRSS:";
    private static final String MEMORYxINFOxTAGxVMxDATA="VmData:";
    private static boolean sHasInitTotalMemory;
    private static long sTotalMemory;
    
    public static long getTotalMemory()
    {
        if(sHasInitTotalMemory)
        {
            return sTotalMemory;
        }
        int bufferSize=8192;
        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        try
        {
            fileReader=new FileReader(MEMORYxINFOxPATH);
            bufferedReader=new BufferedReader(fileReader,bufferSize);
            String memory=bufferedReader.readLine();
            if(memory!=null)
            {
                String[] arrayOfString=memory.split("\\s+");
                if(arrayOfString!=null&&arrayOfString.length>1&&arrayOfString[1]!=null)
                {
                    sTotalMemory=Long.parseLong(arrayOfString[1].trim());
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(fileReader);
            IOUtil.safeClose(bufferedReader);
        }
        if(sTotalMemory<0)
        {
            sTotalMemory=0;
        }
        sHasInitTotalMemory=true;
        return sTotalMemory;
    }
    
    public static int getFreeMemory()
    {
        int memory=0;
        FileInputStream fileInputStream=null;
        try
        {
            File fpath=new File(MEMORYxINFOxPATH);
            if(!fpath.exists())
            {
                return memory;
            }
            int BUFFERxLENGTH=1024;
            byte[] buffer=new byte[BUFFERxLENGTH];
            fileInputStream=new FileInputStream(fpath);
            int length=fileInputStream.read(buffer);
            length=length>=BUFFERxLENGTH?BUFFERxLENGTH:length;
            buffer[length-1]='\0';
            String str=new String(buffer);
            memory+=getMemorySizeFromMemInfo(str,"MemFree:");
            memory+=getMemorySizeFromMemInfo(str,"Buffers:");
            memory+=getMemorySizeFromMemInfo(str,"Cached:");
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(fileInputStream);
        }
        return memory<0?0:memory;
    }
    
    private static int getMemorySizeFromMemInfo(String infoStr,String keyWord)
    {
        if(StringUtil.isEmptyWithTrim(infoStr)||StringUtil.isEmptyWithTrim(keyWord))
        {
            return 0;
        }
        int memSize=0;
        int idxStart=infoStr.indexOf(keyWord);
        if(idxStart>=0)
        {
            idxStart+=keyWord.length();
            int idxEnd=infoStr.indexOf("kB",idxStart);
            if(idxEnd>=0)
            {
                String mem=infoStr.substring(idxStart,idxEnd).trim();
                memSize=Integer.parseInt(mem);
            }
        }
        return memSize;
    }
    
    public static int getOcupiedRssMemory()
    {
        return getOcupiedMemory(MEMORYxINFOxTAGxVMxRSS);
    }
    
    public static int getOcupiedDataMemory()
    {
        return getOcupiedMemory(MEMORYxINFOxTAGxVMxDATA);
    }
    
    private static int getOcupiedMemory(String tag)
    {
        if(TextUtils.isEmpty(tag))
        {
            return 0;
        }
        int ocupied=0;
        FileInputStream fileInputStream=null;
        try
        {
            File file=new File(MEMORYxOCUPIEDxINFOxPATH);
            if(!file.exists())
            {
                return ocupied;
            }
            int BUFFERxLENGTH=1000;
            byte[] buffer=new byte[BUFFERxLENGTH];
            fileInputStream=new FileInputStream(file);
            int length=fileInputStream.read(buffer);
            buffer[length]='\0';
            String str=new String(buffer);
            int idxStart=str.indexOf(tag);
            if(idxStart>=0)
            {
                idxStart+=7;
                int idxEnd=str.indexOf("kB",idxStart);
                if(idxEnd>=0)
                {
                    String memory=str.substring(idxStart,idxEnd).trim();
                    ocupied=Integer.parseInt(memory);
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(fileInputStream);
        }
        return ocupied;
    }
    
    public static long getJavaHeapSize()
    {
        return Runtime.getRuntime().totalMemory();
    }
    
    public static long getAvailableMemory()
    {
        MemoryInfo memoryInfo=new MemoryInfo();
        ActivityManager activityManager=(ActivityManager)Foundation.getApplication().getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        long availableMegs=memoryInfo.availMem/1048576L;
        return availableMegs;
    }
}
