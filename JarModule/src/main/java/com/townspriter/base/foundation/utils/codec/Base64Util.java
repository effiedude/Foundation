package com.townspriter.base.foundation.utils.codec;

import java.io.UnsupportedEncodingException;
import android.util.Base64;

/******************************************************************************
 * @path Foundation:Base64Util
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Base64Util
{
    private static final byte[] map={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'};
    
    public static byte[] decode(byte[] in)
    {
        return decode(in,in.length);
    }
    
    public static byte[] decode(byte[] in,int len)
    {
        int length=len/4*3;
        if(length==0)
        {
            return new byte[0];
        }
        byte[] out=new byte[length];
        int pad=0;
        byte chr;
        for(;;len--)
        {
            chr=in[len-1];
            if((chr=='\n')||(chr=='\r')||(chr==' ')||(chr=='\t'))
            {
                continue;
            }
            if(chr=='=')
            {
                pad++;
            }
            else
            {
                break;
            }
        }
        int out_index=0;
        int in_index=0;
        int bits=0;
        int quantum=0;
        for(int i=0;i<len;i++)
        {
            chr=in[i];
            if((chr=='\n')||(chr=='\r')||(chr==' ')||(chr=='\t'))
            {
                continue;
            }
            if((chr>='A')&&(chr<='Z'))
            {
                /**
                 * (ASCII-65)
                 * A 65 0
                 * Z 90 25
                 */
                bits=chr-65;
            }
            else if((chr>='a')&&(chr<='z'))
            {
                /**
                 * (ASCII-71)
                 * a 97 26
                 * z 122 51
                 */
                bits=chr-71;
            }
            else if((chr>='0')&&(chr<='9'))
            {
                /**
                 * (ASCII+4)
                 * 0 48 52
                 * 9 57 61
                 */
                bits=chr+4;
            }
            else if(chr=='+')
            {
                bits=62;
            }
            else if(chr=='/')
            {
                bits=63;
            }
            else
            {
                return null;
            }
            quantum=(quantum<<6)|(byte)bits;
            if(in_index%4==3)
            {
                out[out_index++]=(byte)((quantum&0x00FF0000)>>16);
                out[out_index++]=(byte)((quantum&0x0000FF00)>>8);
                out[out_index++]=(byte)(quantum&0x000000FF);
            }
            in_index++;
        }
        if(pad>0)
        {
            quantum=quantum<<(6*pad);
            out[out_index++]=(byte)((quantum&0x00FF0000)>>16);
            if(pad==1)
            {
                out[out_index++]=(byte)((quantum&0x0000FF00)>>8);
            }
        }
        byte[] result=new byte[out_index];
        System.arraycopy(out,0,result,0,out_index);
        return result;
    }
    
    public static String encode(byte[] in,String charsetName) throws UnsupportedEncodingException
    {
        return encode(in,charsetName,true);
    }
    
    /**
     * 将比特数组BASE-64编码
     * 
     * @param in
     * 传入的要加密的比特数组
     * @param charsetName
     * 用于构建BASE-64返回字符串的编译(不能为空.建议:US-ASCII)
     * @return 编码的字符串
     */
    public static String encode(byte[] in,String charsetName,boolean insertLFs) throws UnsupportedEncodingException
    {
        int length=in.length*4/3;
        length+=length/76+3;
        byte[] out=new byte[length];
        int index=0,i,crlr=0,end=in.length-in.length%3;
        for(i=0;i<end;i+=3)
        {
            out[index++]=map[(in[i]&0xff)>>2];
            out[index++]=map[((in[i]&0x03)<<4)|((in[i+1]&0xff)>>4)];
            out[index++]=map[((in[i+1]&0x0f)<<2)|((in[i+2]&0xff)>>6)];
            out[index++]=map[(in[i+2]&0x3f)];
            if(((index-crlr)%76==0)&&(index!=0)&&insertLFs)
            {
                out[index++]='\n';
                crlr++;
                // out[index++]='\r';
                // crlr++;
            }
        }
        switch(in.length%3)
        {
            case 1:
                out[index++]=map[(in[end]&0xff)>>2];
                out[index++]=map[(in[end]&0x03)<<4];
                out[index++]='=';
                out[index++]='=';
                break;
            case 2:
                out[index++]=map[(in[end]&0xff)>>2];
                out[index++]=map[((in[end]&0x03)<<4)|((in[end+1]&0xff)>>4)];
                out[index++]=map[((in[end+1]&0x0f)<<2)];
                out[index++]='=';
                break;
        }
        return new String(out,0,index,charsetName==null?"US-ASCII":charsetName);
    }
    
    public static String base64Encode2String(byte[] bytes)
    {
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    
    public static String base64Encode2String(byte[] bytes,int flag)
    {
        return Base64.encodeToString(bytes,flag);
    }
    
    public static byte[] base64Decode(String str)
    {
        return Base64.decode(str,Base64.DEFAULT);
    }
}
