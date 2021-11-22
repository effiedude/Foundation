package com.townspriter.base.foundation.utils.lang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.townspriter.base.foundation.utils.text.StringUtil;

/******************************************************************************
 * @path Foundation:JsonHelper
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class JsonHelper
{
    public static JSONObject getJSONObject(JSONObject aJson,String aString)
    {
        JSONObject ret=null;
        try
        {
            ret=aJson.getJSONObject(aString);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }
        return ret;
    }
    
    public static JSONArray getJSONArray(JSONObject aJson,String aString)
    {
        if(null==aJson)
        {
            return null;
        }
        JSONArray ret=null;
        try
        {
            ret=aJson.getJSONArray(aString);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }
        return ret;
    }
    
    public static JSONObject createJSONObject(Object...aKeyValue)
    {
        JSONObject ret=null;
        if(null!=aKeyValue&&0==(aKeyValue.length%2))
        {
            try
            {
                ret=new JSONObject();
                int length=aKeyValue.length;
                for(int i=0,len=length/2;i<len;i++)
                {
                    Object key=aKeyValue[2*i];
                    Object value=aKeyValue[2*i+1];
                    ret.put(key.toString(),value);
                }
            }
            catch(JSONException jsonException)
            {
                jsonException.printStackTrace();
            }
        }
        else
        {
            AssertUtil.fail("键值对不匹配");
        }
        return ret;
    }
    
    public static JSONObject createJSONObject(String aJs)
    {
        JSONObject ret=null;
        if(StringUtil.isNotEmpty(aJs))
        {
            try
            {
                ret=new JSONObject(aJs);
            }
            catch(JSONException jsonException)
            {
                jsonException.printStackTrace();
            }
        }
        return ret;
    }
    
    public static Long getLong(JSONObject aJsonObject,String aKey)
    {
        Long ret=null;
        try
        {
            ret=aJsonObject.getLong(aKey);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }
        return ret;
    }
    
    public static String getString(JSONObject aJsonObject,String aKey)
    {
        String ret=null;
        try
        {
            ret=aJsonObject.getString(aKey);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }
        return ret;
    }
    
    public static int getInt(JSONArray aJsonArray,int aIndex)
    {
        int ret=Integer.MIN_VALUE;
        if((null==aJsonArray)||!(0<=aIndex&&aIndex<=aJsonArray.length()-1))
        {
            return ret;
        }
        try
        {
            ret=aJsonArray.getInt(aIndex);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }
        return ret;
    }
    
    public static JSONArray createJOSNArray(String aJson)
    {
        JSONArray ret=null;
        if(StringUtil.isNotEmpty(aJson))
        {
            try
            {
                ret=new JSONArray(aJson);
            }
            catch(JSONException jsonException)
            {
                jsonException.printStackTrace();
            }
        }
        return ret;
    }
    
    public static long getJSONArrayLong(JSONArray aArray,int aIndex)
    {
        AssertUtil.mustNotNull(aArray);
        AssertUtil.mustOk(0<=aIndex&&aIndex<aArray.length());
        try
        {
            return aArray.getLong(aIndex);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
            AssertUtil.fail();
            return 0;
        }
    }
    
    public static String getJSONArrayString(JSONArray aArray,int aIndex)
    {
        AssertUtil.mustNotNull(aArray);
        AssertUtil.mustOk(0<=aIndex&&aIndex<aArray.length());
        try
        {
            return aArray.getString(aIndex);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
            AssertUtil.fail();
            return null;
        }
    }
}
