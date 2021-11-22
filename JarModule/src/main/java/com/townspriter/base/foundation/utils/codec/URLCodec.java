package com.townspriter.base.foundation.utils.codec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import android.text.TextUtils;

/******************************************************************************
 * @path Foundation:URLCodec
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
