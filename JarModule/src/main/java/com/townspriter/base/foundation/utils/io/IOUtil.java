package com.townspriter.base.foundation.utils.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipFile;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import android.database.Cursor;

/******************************************************************************
 * @path IOUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class IOUtil
{
    private static final int DEFAULTxBUFFERxSIZE=1024*4;
    private static final String UTFx8="UTF-8";
    
    /**
     * 从输入流中读取部份数据(字节数组)
     *
     * @param aInput
     * 输入流
     * @param aReadLength
     * 读取的长度
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream aInput,int aReadLength) throws IOException
    {
        if(aInput==null||aReadLength<=0)
        {
            return null;
        }
        byte[] sData=new byte[aReadLength];
        int sLength;
        for(int i=0;i<aReadLength;)
        {
            if(aReadLength-i<DEFAULTxBUFFERxSIZE)
            {
                sLength=aInput.read(sData,i,aReadLength-i);
            }
            else
            {
                sLength=aInput.read(sData,i,DEFAULTxBUFFERxSIZE);
            }
            if(sLength==-1)
            {
                break;
            }
            i+=sLength;
        }
        return sData;
    }
    
    /**
     * @param input
     * 调用者注意关闭输入流
     * @return
     */
    public static byte[] readBytes(InputStream input)
    {
        if(input==null)
        {
            return null;
        }
        ByteArrayOutputStream baos=new ByteArrayOutputStream(4096);
        try
        {
            copy(input,baos,DEFAULTxBUFFERxSIZE);
            return baos.toByteArray();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            safeClose(baos);
        }
        return null;
    }
    
    public static byte[] readFullBytes(InputStream input)
    {
        if(input==null)
        {
            return null;
        }
        byte[] inputStreamBuffer=new byte[32*1024];
        ByteArrayOutputStream baos=new ByteArrayOutputStream(2048);
        try
        {
            int offset=0;
            while((offset=input.read(inputStreamBuffer,0,inputStreamBuffer.length))>0)
            {
                baos.write(inputStreamBuffer,0,offset);
            }
            return baos.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            safeClose(baos);
            baos=null;
        }
        return null;
    }
    
    public static void safeClose(Closeable closeable)
    {
        if(closeable!=null)
        {
            try
            {
                closeable.close();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
    
    public static void safeClose(Cursor cursor)
    {
        if(cursor!=null)
        {
            try
            {
                cursor.close();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
    
    public static void safeClose(ZipFile zipFile)
    {
        if(zipFile!=null)
        {
            try
            {
                zipFile.close();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
    
    public static int copy(InputStream input,OutputStream output) throws IOException
    {
        if(input==null||output==null)
        {
            return -1;
        }
        long count=copyLarge(input,output);
        if(count>Integer.MAX_VALUE)
        {
            return -1;
        }
        return (int)count;
    }
    
    private static long copyLarge(InputStream input,OutputStream output) throws IOException
    {
        return copy(input,output,DEFAULTxBUFFERxSIZE);
    }
    
    public static long copy(InputStream input,OutputStream output,int bufferSize) throws IOException
    {
        if(input==null||output==null)
        {
            return -1;
        }
        byte[] buffer=new byte[bufferSize];
        long count=0;
        int n;
        while(-1!=(n=input.read(buffer)))
        {
            output.write(buffer,0,n);
            count+=n;
        }
        output.flush();
        return count;
    }
    
    /**
     * @param input
     * 请调用者关闭流.本方法不会关闭
     * @return 一定非空
     * @throws IOException
     */
    public static List<String> readLines(InputStream input) throws IOException
    {
        ArrayList<String> lines=new ArrayList<>();
        BufferedReader reader=new BufferedReader(new InputStreamReader(input,StandardCharsets.UTF_8));
        String line;
        while((line=reader.readLine())!=null)
        {
            lines.add(line);
        }
        return lines;
    }
    
    /**
     * @param lines
     * @param lineEnding
     * 空则使用换行符
     * @param output
     * 输出流(调用者关闭)
     * @throws IOException
     */
    public static void writeLines(Collection<?> lines,String lineEnding,OutputStream output) throws IOException
    {
        if(lines!=null)
        {
            if(lineEnding==null)
            {
                lineEnding="\n";
            }
            byte[] lineEndingBytes=lineEnding.getBytes(StandardCharsets.UTF_8);
            for(Object line:lines)
            {
                if(line!=null)
                {
                    output.write(line.toString().getBytes(StandardCharsets.UTF_8));
                }
                output.write(lineEndingBytes);
            }
        }
    }
    
    /** 将对象序列化成字节流 */
    public static byte[] serializeObjectToBytes(Object object)
    {
        byte[] ret=null;
        ByteArrayOutputStream byteArrayOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        if(null!=object)
        {
            try
            {
                byteArrayOutputStream=new ByteArrayOutputStream();
                objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                ret=byteArrayOutputStream.toByteArray();
            }
            catch(IOException exception)
            {
                exception.printStackTrace();
                AssertUtil.fail(exception);
            }
            finally
            {
                safeClose(objectOutputStream);
                safeClose(byteArrayOutputStream);
            }
        }
        return ret;
    }
    
    /** 将字节流反序列化成对象 */
    public static Object deserializeObjectFromBytes(byte[] bytes)
    {
        Object ret=null;
        ByteArrayInputStream byteArrayInputStream=null;
        ObjectInputStream objectInputStream=null;
        if(null!=bytes)
        {
            try
            {
                byteArrayInputStream=new ByteArrayInputStream(bytes);
                objectInputStream=new ObjectInputStream(byteArrayInputStream);
                ret=objectInputStream.readObject();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
                AssertUtil.fail(exception);
            }
            finally
            {
                safeClose(objectInputStream);
                safeClose(byteArrayInputStream);
            }
        }
        return ret;
    }
}
