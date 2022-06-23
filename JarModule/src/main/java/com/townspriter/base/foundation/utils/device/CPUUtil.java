package com.townspriter.base.foundation.utils.device;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import com.townspriter.base.foundation.utils.io.IOUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;

/******************************************************************************
 * @path CPUUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class CPUUtil
{
    public static final int CPUxFREQUENCExUNKNOWN=-1;
    private static final String TAG="CPUUtil";
    private static final boolean DEBUG=false;
    private static final String CPUxINFOxCORExCOUNTxFILExPATH="/sys/devices/system/cpu/";
    private static final Object sGetCPUArchLock=new Object();
    private static boolean sHasInitCPUCoreCount;
    private static int sCPUCoreCount=1;
    private static boolean sHasInitMaxCPUFrequence;
    private static int sMaxCPUFrequence=CPUxFREQUENCExUNKNOWN;
    private static String sCPUArch;
    private static boolean sHasInitCPUInfo;
    private static String sCPUInfoArch="";
    private static String sCPUInfoVFP="";
    private static String sCPUArchit="";
    
    public static int getCPUCoreCount()
    {
        if(sHasInitCPUCoreCount)
        {
            return sCPUCoreCount;
        }
        final class CPUFilter implements FileFilter
        {
            @Override
            public boolean accept(File pathName)
            {
                String path=pathName.getName();
                if(path.startsWith("cpu"))
                {
                    for(int i=3;i<path.length();i++)
                    {
                        if(!Character.isDigit(path.charAt(i)))
                        {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        try
        {
            File dir=new File(CPUxINFOxCORExCOUNTxFILExPATH);
            File[] files=dir.listFiles(new CPUFilter());
            sCPUCoreCount=files.length;
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        if(sCPUCoreCount<=1)
        {
            sCPUCoreCount=Runtime.getRuntime().availableProcessors();
        }
        sHasInitCPUCoreCount=true;
        if(DEBUG)
        {
            Logger.d(TAG,"getCpuCoreCount:"+sCPUCoreCount);
        }
        return sCPUCoreCount;
    }
    
    public static int getMaxCPUFrequence()
    {
        if(sHasInitMaxCPUFrequence)
        {
            return sMaxCPUFrequence;
        }
        int coreCount=getCPUCoreCount();
        for(int i=0;i<coreCount;i++)
        {
            File cpuInfoMaxFreqFile=new File("/sys/devices/system/cpu/cpu"+i+"/cpufreq/cpuinfo_max_freq");
            if(cpuInfoMaxFreqFile.exists())
            {
                FileReader fileReader=null;
                BufferedReader bufferedReader=null;
                try
                {
                    fileReader=new FileReader(cpuInfoMaxFreqFile);
                    bufferedReader=new BufferedReader(fileReader);
                    String text=bufferedReader.readLine();
                    int freqBound=Integer.parseInt(text);
                    if(freqBound>sMaxCPUFrequence)
                    {
                        sMaxCPUFrequence=freqBound;
                    }
                }
                catch(NumberFormatException numberFormatException)
                {
                    numberFormatException.printStackTrace();
                }
                catch(IOException ioException)
                {
                    ioException.printStackTrace();
                }
                finally
                {
                    IOUtil.safeClose(bufferedReader);
                    IOUtil.safeClose(fileReader);
                }
            }
        }
        if(sMaxCPUFrequence<0)
        {
            sMaxCPUFrequence=0;
        }
        sHasInitMaxCPUFrequence=true;
        return sMaxCPUFrequence;
    }
    
    /**
     * Processor : ARMv7 Processor rev 0 (v7l)
     * processor : 0
     * BogoMIPS : 996.14
     * processor : 1
     * BogoMIPS : 996.14
     * Features : swp half thumb fastmult vfp edsp vfpv3 vfpv3d16
     * CPU implementer : 0x41
     * CPU architecture: 7
     * CPU variant : 0x1
     * CPU part : 0xc09
     * CPU revision : 0
     * Hardware : star
     * Revision : 0000
     * Serial : 0000000000000000
     */
    private static void initCPUInfo()
    {
        if(sHasInitCPUInfo)
        {
            return;
        }
        BufferedReader bufferedReader=null;
        try
        {
            bufferedReader=new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
            HashMap<String,String> cpuInfoMap=new HashMap<String,String>();
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                line=line.trim();
                if(line.length()>0)
                {
                    String[] pairs=line.split(":");
                    if(pairs.length>1)
                    {
                        cpuInfoMap.put(pairs[0].trim(),pairs[1].trim());
                    }
                }
            }
            String processor=cpuInfoMap.get("Processor");
            if(processor!=null)
            {
                int firstIndex=processor.indexOf("(");
                int lastIndex=processor.lastIndexOf(")");
                int len=lastIndex-firstIndex;
                if(firstIndex>0&&lastIndex>0&&len>0)
                {
                    sCPUInfoArch=processor.substring(firstIndex+1,lastIndex);
                }
                else
                {
                    sCPUInfoArch="v"+cpuInfoMap.get("CPU architecture");
                }
            }
            sCPUInfoVFP=cpuInfoMap.get("Features");
            sCPUArchit=cpuInfoMap.get("CPU part");
            sHasInitCPUInfo=true;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(bufferedReader);
        }
    }
    
    public static String getCPUInfoArch()
    {
        initCPUInfo();
        return sCPUInfoArch;
    }
    
    public static String getCPUInfoArchit()
    {
        initCPUInfo();
        return sCPUArchit;
    }
    
    public static String getCPUInfoVFP()
    {
        initCPUInfo();
        return sCPUInfoVFP;
    }
    
    /** 获取解析器架构信息(统一返回小写) */
    public static String getCPUArch()
    {
        String cpuArch=sCPUArch;
        if(cpuArch!=null)
        {
            return cpuArch;
        }
        synchronized(sGetCPUArchLock)
        {
            if(sCPUArch!=null)
            {
                return sCPUArch;
            }
            /** 注意:4.0-4.1系统版本Runtime.getRuntime().exec()会导致死锁.对这个区间的版本仍然采用旧方式判断是否是X86架构 */
            int ICExCREAMxSANDWICH=14;
            int JELLYxBEAN=16;
            if(VERSION.SDK_INT<ICExCREAMxSANDWICH||VERSION.SDK_INT>JELLYxBEAN)
            {
                BufferedReader bufferedReader=null;
                Process process=null;
                try
                {
                    process=Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
                    bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String strAbi=bufferedReader.readLine();
                    if(strAbi!=null&&strAbi.contains("x86"))
                    {
                        sCPUArch="x86";
                    }
                    else if(strAbi!=null&&strAbi.contains("armeabi-v7a"))
                    {
                        sCPUArch="armv7";
                    }
                }
                catch(Throwable throwable)
                {
                    throwable.printStackTrace();
                }
                finally
                {
                    IOUtil.safeClose(bufferedReader);
                    if(process!=null)
                    {
                        process.destroy();
                    }
                }
            }
            if(TextUtils.isEmpty(sCPUArch))
            {
                try
                {
                    sCPUArch=System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
                    if(!StringUtil.isEmpty(sCPUArch)&&sCPUArch.contains("i686"))
                    {
                        sCPUArch="x86";
                    }
                }
                catch(Throwable throwable)
                {
                    throwable.printStackTrace();
                }
            }
            if(StringUtil.isEmpty(sCPUArch))
            {
                sCPUArch="";
            }
            if(DEBUG)
            {
                Logger.d(TAG,"getCPUArch-sCPUArch:"+sCPUArch);
            }
            return sCPUArch;
        }
    }
    
    /** 获取解析器架构前缀(即大致类别) */
    public static String getCPUArchPrefix()
    {
        String strArch=getCPUArch();
        if(strArch.startsWith("armv7"))
        {
            strArch="arm7";
        }
        else if(strArch.startsWith("armv6"))
        {
            strArch="arm6";
        }
        else if(strArch.startsWith("armv5"))
        {
            strArch="arm5";
        }
        else if("x86".equals(strArch)||"i686".equals(strArch))
        {
            strArch="x86";
        }
        else if("mips".equals(strArch))
        {
            strArch="mips";
        }
        return strArch;
    }
    
    public static boolean isSupportAbi(String targetAbi)
    {
        if(StringUtil.isEmpty(targetAbi))
        {
            return false;
        }
        String[] supportedAbis={Build.CPU_ABI,Build.CPU_ABI2};
        String targetAbiLower=targetAbi.toLowerCase(Locale.ENGLISH);
        for(String abi:supportedAbis)
        {
            if(StringUtil.isNotEmptyWithTrim(abi)&&abi.toLowerCase(Locale.ENGLISH).contains(targetAbiLower))
            {
                return true;
            }
        }
        return false;
    }
    
    /** 是否是MIPS解析器架构 */
    public static boolean isMIPSCPUArch(String strArch)
    {
        return "mips".equals(strArch);
    }
    
    /** 判断当前解析器架构是否适用ARM7.0架构包 */
    public static boolean isARM7CPUArchCompatible(String strArch)
    {
        if(StringUtil.isEmpty(strArch))
        {
            return false;
        }
        return strArch.startsWith("armv7")//
        ||strArch.startsWith("armv8")//
        ||strArch.startsWith("arm64-v8")//
        ||"aarch64".equals(strArch);
    }
    
    /** 判断当前解析器架构是否适用ARM6.0架构包 */
    public static boolean isARM6CPUArchCompatible(String strArch)
    {
        if(StringUtil.isEmpty(strArch))
        {
            return false;
        }
        return strArch.startsWith("armv6");
    }
    
    /** 判断当前解析器架构是否适用ARM5.0架构包 */
    public static boolean isARM5CPUArchCompatible(String strArch)
    {
        if(StringUtil.isEmpty(strArch))
        {
            return false;
        }
        return strArch.startsWith("armv5");
    }
    
    /** 判断当前解析器架构是否适用X86架构包 */
    public static boolean isX86CPUArchCompatible(String strArch)
    {
        return "x86".equals(strArch)||"i686".equals(strArch);
    }
}
