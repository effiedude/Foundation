package com.townspriter.base.foundation.utils.codec;

import java.lang.reflect.Type;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path GsonUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class GsonUtil
{
    /** 默认关闭超文本协议编码 */
    private static final Gson GSON=new GsonBuilder().disableHtmlEscaping().create();
    
    public static Gson instance()
    {
        return GSON;
    }
    
    @Nullable
    public static JsonObject parseJsonObj(@NonNull String jsonString)
    {
        try
        {
            return JsonParser.parseString(jsonString).getAsJsonObject();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return null;
    }
    
    public static HashMap<String,String> parseHashMap(String jsonString)
    {
        HashMap<String,String> map=null;
        try
        {
            map=GSON.fromJson(jsonString,new TypeToken<HashMap<String,String>>()
            {}.getType());
        }
        catch(Exception exception)
        {
            AssertUtil.fail(jsonString+":"+exception);
        }
        return map;
    }
    
    public static HashMap<String,Double> parseMeasureMap(String jsonString)
    {
        HashMap<String,Double> map=null;
        try
        {
            map=GSON.fromJson(jsonString,new TypeToken<HashMap<String,Double>>()
            {}.getType());
        }
        catch(Exception exception)
        {
            AssertUtil.fail(jsonString+":"+exception);
        }
        return map;
    }
    
    public static <T> T parse(String jsonString,Class<T> clazz)
    {
        T t=null;
        try
        {
            t=GSON.fromJson(jsonString,clazz);
        }
        catch(Exception exception)
        {
            AssertUtil.fail(jsonString+":"+exception);
        }
        return t;
    }
    
    public static <T> T parse(String jsonString,Type type)
    {
        T t=null;
        try
        {
            t=GSON.fromJson(jsonString,type);
        }
        catch(Exception exception)
        {
            AssertUtil.fail(jsonString+":"+exception);
        }
        return t;
    }
    
    public static <T> String toJson(T t)
    {
        return GSON.toJson(t);
    }
}
