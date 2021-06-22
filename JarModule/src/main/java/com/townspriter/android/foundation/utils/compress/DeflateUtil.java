package com.townspriter.android.foundation.utils.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import com.townspriter.android.foundation.utils.io.IOUtil;

/******************************************************************************
 * @Path Foundation:DeflateUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class DeflateUtil
{
    /**
     * 使用算法解压数据.不涉及ZIP文件格式.与{@link DeflateUtil#deflateData(byte[])}对应
     * 目前用于云同步
     * 
     * @param data
     * @return
     */
    public static byte[] inflateData(byte[] data)
    {
        if(null==data||0==data.length)
        {
            return null;
        }
        byte[] result=null;
        ByteArrayOutputStream baos=null;
        ByteArrayInputStream bais=null;
        InflaterInputStream gis=null;
        try
        {
            baos=new ByteArrayOutputStream();
            bais=new ByteArrayInputStream(data);
            gis=new InflaterInputStream(bais);
            byte[] tmpBuf=new byte[4096];
            int readlen=0;
            while((readlen=gis.read(tmpBuf))!=-1)
            {
                baos.write(tmpBuf,0,readlen);
            }
            gis.close();
            result=baos.toByteArray();
            bais.close();
            baos.close();
        }
        catch(Exception exception)
        {}
        finally
        {
            IOUtil.safeClose(gis);
            IOUtil.safeClose(bais);
            IOUtil.safeClose(baos);
        }
        return result;
    }
    
    /**
     * 使用算法解压数据.不涉及ZIP文件格式.与{@link DeflateUtil#deflateData(byte[])}对应
     * 目前用于云同步
     * 
     * @param data
     * @return
     */
    public static byte[] deflateData(byte[] data)
    {
        if(data==null||0==data.length)
        {
            return null;
        }
        byte[] result=null;
        ByteArrayOutputStream bos=null;
        DeflaterOutputStream zip=null;
        try
        {
            bos=new ByteArrayOutputStream();
            zip=new DeflaterOutputStream(bos);
            zip.write(data);
            zip.close();
            result=bos.toByteArray();
            bos.close();
        }
        catch(Exception exception)
        {}
        finally
        {
            IOUtil.safeClose(zip);
            IOUtil.safeClose(bos);
        }
        return result;
    }
}
