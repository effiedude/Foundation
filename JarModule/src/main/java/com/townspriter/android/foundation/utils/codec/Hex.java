package com.townspriter.android.foundation.utils.codec;
/******************************************************************************
 * @Path Foundation:Hex
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class Hex
{
    private static final char[] DIGITS_LOWER={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    private static final char[] DIGITS_UPPER={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    
    public static char[] encodeHex(byte[] data)
    {
        return encodeHex(data,true);
    }
    
    public static char[] encodeHex(byte[] data,boolean toLowerCase)
    {
        return encodeHex(data,toLowerCase?DIGITS_LOWER:DIGITS_UPPER);
    }
    
    private static char[] encodeHex(byte[] data,char[] toDigits)
    {
        int l=data.length;
        char[] out=new char[l<<1];
        int i=0;
        for(int j=0;i<l;i++)
        {
            out[(j++)]=toDigits[((0xF0&data[i])>>>4)];
            out[(j++)]=toDigits[(0xF&data[i])];
        }
        return out;
    }
    
    public static String encodeHexString(byte[] data)
    {
        return new String(encodeHex(data));
    }
}
