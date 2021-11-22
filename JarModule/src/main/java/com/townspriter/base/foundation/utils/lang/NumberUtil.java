package com.townspriter.base.foundation.utils.lang;

import com.townspriter.base.foundation.utils.text.StringUtil;

/******************************************************************************
 * @path Foundation:NumberUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class NumberUtil
{
    public static int toInt(String value)
    {
        return toInt(value,0);
    }
    
    public static int toInt(String value,int defaultValue)
    {
        int ret=defaultValue;
        if(StringUtil.isNotEmpty(value))
        {
            boolean tryHex=startsWith0x(value);
            try
            {
                if(tryHex)
                {
                    value=value.substring(2);
                    ret=Integer.parseInt(value,16);
                }
                else
                {
                    ret=Integer.parseInt(value);
                }
            }
            catch(NumberFormatException numberFormatException)
            {
                numberFormatException.printStackTrace();
            }
        }
        return ret;
    }
    
    public static long toLong(String value)
    {
        return toLong(value,0L);
    }
    
    public static long toLong(String value,long defaultValue)
    {
        long ret=defaultValue;
        if(StringUtil.isNotEmpty(value))
        {
            boolean tryHex=startsWith0x(value);
            try
            {
                if(tryHex)
                {
                    value=value.substring(2);
                    ret=Long.parseLong(value,16);
                }
                else
                {
                    ret=Long.parseLong(value);
                }
            }
            catch(NumberFormatException numberFormatException)
            {
                numberFormatException.printStackTrace();
            }
        }
        return ret;
    }
    
    private static boolean startsWith0x(String text)
    {
        return text.startsWith("0x");
    }
    
    public static float toFloat(String value,float defaultValue)
    {
        float ret=defaultValue;
        try
        {
            ret=Float.parseFloat(value);
        }
        catch(NumberFormatException numberFormatException)
        {
            numberFormatException.printStackTrace();
        }
        return ret;
    }
    
    public static double toDouble(String value,double defaultValue)
    {
        double ret=defaultValue;
        try
        {
            ret=Double.parseDouble(value);
        }
        catch(NumberFormatException numberFormatException)
        {
            numberFormatException.printStackTrace();
        }
        return ret;
    }
    
    public static double toDouble(String value)
    {
        return toDouble(value,0.0);
    }
    
    public static Integer[] toIntegerArray(int[] intArray)
    {
        if(ArrayUtil.isEmpty(intArray))
        {
            return null;
        }
        Integer[] ret=new Integer[intArray.length];
        for(int i=0;i<intArray.length;i++)
        {
            ret[i]=intArray[i];
        }
        return ret;
    }
    
    public static int byteArrayBEToInt(byte[] bytes)
    {
        return bytes[3]&0xff|(bytes[2]&0xff)<<8|(bytes[1]&0xff)<<16|(bytes[0]&0xff)<<24;
    }
    
    public static byte[] intToByteArrayBE(int i)
    {
        return new byte[]{(byte)(i>>>24),(byte)(i>>>16),(byte)(i>>>8),(byte)i};
    }
    
    /** 判断字符串是否是只由0-9组成 */
    public static boolean isIntOrLong(String text)
    {
        /** 空字符串肯定不是整形 */
        if(StringUtil.isEmpty(text))
        {
            return false;
        }
        int index=0;
        char ch=text.charAt(index);
        /** 第一个可能是符号 */
        if(ch<'0')
        {
            /** 第一个既不是数字也不是符号肯定不是整形 */
            if(ch=='-'||ch=='+')
            {
                index++;
            }
            else
            {
                return false;
            }
        }
        int len=text.length();
        while(index<len)
        {
            /** 后面的只要不是数字就不是整形 */
            ch=text.charAt(index);
            if(ch<'0'||ch>'9')
            {
                return false;
            }
            index++;
        }
        return true;
    }
    
    public static boolean isDoubleEquals(double d1,double d2)
    {
        return Math.abs(d1-d2)<0.000001;
    }
    
    public static boolean isZero(double data)
    {
        return isDoubleEquals(data,0.0f)||isNaN(data);
    }
    
    public static boolean isNaN(double data)
    {
        return !(data<=0||data>0);
    }
}
