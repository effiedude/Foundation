package com.townspriter.android.foundation.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/******************************************************************************
 * @Path Foundation:MD5Utils
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class MD5Utils
{
    public MD5Utils()
    {}
    
    public static String byteToHexString(byte[] bArr)
    {
        if(bArr!=null&&bArr.length>0)
        {
            StringBuffer stringBuffer=new StringBuffer(bArr.length*2);
            for(int i=0;i<bArr.length;++i)
            {
                if((bArr[i]&255)<16)
                {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Long.toString((long)(bArr[i]&255),16));
            }
            return stringBuffer.toString();
        }
        else
        {
            return null;
        }
    }
    
    public static String getMD5(File file)
    {
        FileInputStream fileInputStream=null;
        String str=null;
        MessageDigest messageDigest;
        if(file!=null)
        {
            try
            {
                try
                {
                    messageDigest=MessageDigest.getInstance("MD5");
                    fileInputStream=new FileInputStream(file);
                    byte[] bArr=new byte[8192];
                    while(true)
                    {
                        int read=fileInputStream.read(bArr);
                        if(read==-1)
                        {
                            str=byteToHexString(messageDigest.digest());
                            if(fileInputStream!=null)
                            {
                                try
                                {
                                    fileInputStream.close();
                                }
                                catch(IOException ioException)
                                {
                                    ioException.printStackTrace();
                                }
                            }
                            String var6=str;
                            return var6;
                        }
                        messageDigest.update(bArr,0,read);
                    }
                }
                catch(FileNotFoundException fileNotFoundException)
                {
                    fileNotFoundException.printStackTrace();
                }
                catch(IOException ioException)
                {
                    ioException.printStackTrace();
                }
                catch(Throwable throwable)
                {
                    throwable.printStackTrace();
                }
                return str;
            }
            finally
            {
                IOUtil.safeClose(fileInputStream);
            }
        }
        else
        {
            return str;
        }
    }
    
    public static String getMD5(String str)
    {
        if(str==null)
        {
            return null;
        }
        else
        {
            try
            {
                byte[] bytes=str.getBytes();
                MessageDigest instance=MessageDigest.getInstance("MD5");
                instance.update(bytes);
                return byteToHexString(instance.digest());
            }
            catch(Exception exception)
            {
                return null;
            }
        }
    }
    
    public static byte[] getMD5(InputStream inputStream)
    {
        byte[] ret=null;
        if(inputStream!=null)
        {
            try
            {
                MessageDigest instance=MessageDigest.getInstance("MD5");
                if(instance!=null)
                {
                    byte[] tempBytes=new byte[8192];
                    while(true)
                    {
                        int readLength=inputStream.read(tempBytes);
                        if(readLength!=-1)
                        {
                            instance.update(tempBytes,0,readLength);
                        }
                        else
                        {
                            break;
                        }
                    }
                    ret=instance.digest();
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return ret;
    }
    
    public static byte[] getMD5Byte(File file)
    {
        byte[] ret=null;
        if(null!=file&&file.exists())
        {
            FileInputStream fis=null;
            try
            {
                fis=new FileInputStream(file);
                ret=getMD5(fis);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            finally
            {
                IOUtil.safeClose(fis);
            }
        }
        return ret;
    }
    
    public static byte[] getMD5(byte[] bArr)
    {
        try
        {
            MessageDigest instance=MessageDigest.getInstance("MD5");
            instance.update(bArr);
            return instance.digest();
        }
        catch(Exception exception)
        {
            return null;
        }
    }
}
