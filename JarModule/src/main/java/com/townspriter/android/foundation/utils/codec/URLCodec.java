package com.townspriter.android.foundation.utils.codec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import android.text.TextUtils;

/******************************************************************************
 * @Path Foundation:URLCodec
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class URLCodec
{
    private static final String UTF8="UTF-8";
    
    public static String encode(String data)
    {
        if(TextUtils.isEmpty(data))
        {
            return "";
        }
        String retData;
        try
        {
            retData=URLEncoder.encode(data,UTF8);
        }
        catch(UnsupportedEncodingException e)
        {
            retData=URLEncoder.encode(data);
        }
        return retData;
    }
    
    public static String decode(String data)
    {
        if(TextUtils.isEmpty(data))
        {
            return "";
        }
        String retData;
        try
        {
            retData=URLDecoder.decode(data,UTF8);
        }
        catch(UnsupportedEncodingException e)
        {
            retData=URLEncoder.encode(data);
        }
        return retData;
    }
}
