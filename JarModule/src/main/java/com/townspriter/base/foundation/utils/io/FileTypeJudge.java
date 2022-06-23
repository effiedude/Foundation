package com.townspriter.base.foundation.utils.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/******************************************************************************
 * @path FileTypeJudge
 * @describe 文件类型判断类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class FileTypeJudge
{
    private FileTypeJudge()
    {}
    
    /** 将文件头转换成十六进制字符串 */
    private static String bytesToHexString(byte[] src)
    {
        StringBuilder stringBuilder=new StringBuilder();
        if(src==null||src.length<=0)
        {
            return null;
        }
        for(int i=0;i<src.length;i++)
        {
            int v=src[i]&0xFF;
            String hv=Integer.toHexString(v);
            if(hv.length()<2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /** 得到文件头 */
    private static String getFileContent(String filePath) throws IOException
    {
        byte[] b=new byte[28];
        InputStream inputStream=null;
        try
        {
            inputStream=new FileInputStream(filePath);
            inputStream.read(b,0,28);
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
            throw ioException;
        }
        finally
        {
            if(inputStream!=null)
            {
                try
                {
                    inputStream.close();
                }
                catch(IOException ioException)
                {
                    ioException.printStackTrace();
                    throw ioException;
                }
            }
        }
        return bytesToHexString(b);
    }
    
    /** 判断文件类型 */
    public static FileType getType(String filePath)
    {
        try
        {
            String fileHead=getFileContent(filePath);
            if(fileHead==null||fileHead.length()==0)
            {
                return null;
            }
            fileHead=fileHead.toUpperCase();
            FileType[] fileTypes=FileType.values();
            for(FileType type:fileTypes)
            {
                if(fileHead.startsWith(type.getValue()))
                {
                    return type;
                }
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        return null;
    }
    
    public enum FileType
    {
        JPEG("FFD8FF"),PNG("89504E47"),GIF("47494638"),TIFF("49492A00"),BMP("424D"),RTF("7B5C727466"),XML("3C3F786D6C"),HTML("68746D6C3E"),DBX("CFAD12FEC5FD746F"),XLSxDOC("D0CF11E0"),PDF("255044462D312E"),ZIP("504B0304"),RAR("52617221"),WAV("57415645"),AVI("41564920"),RAM("2E7261FD"),RM("2E524D46"),MPG("000001BA"),MOV("6D6F6F76"),ASF("3026B2758E66CF11"),MID("4D546864");
        
        private String value;
        
        FileType(String value)
        {
            this.value=value;
        }
        
        public String getValue()
        {
            return value;
        }
        
        public void setValue(String value)
        {
            this.value=value;
        }
    }
}