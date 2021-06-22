package com.townspriter.android.foundation.utils.storage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.io.MD5Utils;
import com.townspriter.android.foundation.utils.lang.AssertUtil;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path Foundation:PreferencesUtils
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class PreferencesUtils
{
    private static final String SPxNAMExSTxBASE="8418534539A4E8ED6466103E53AA12D5";
    private SharedPreferencesProxy mGlobalProxy;
    
    private PreferencesUtils()
    {}
    
    public static SharedPreferencesProxy getGlobalInstance()
    {
        PreferencesUtils instance=Singleton.INSTANCE;
        if(instance.mGlobalProxy==null)
        {
            instance.mGlobalProxy=new SharedPreferencesProxy(Foundation.getApplication().getSharedPreferences(SPxNAMExSTxBASE,Context.MODE_PRIVATE));
        }
        return Singleton.INSTANCE.mGlobalProxy;
    }
    
    public static SharedPreferencesProxy getInstance(String name)
    {
        return new SharedPreferencesProxy(Foundation.getApplication().getSharedPreferences(MD5Utils.getMD5(name),Context.MODE_PRIVATE));
    }
    
    private static final class Singleton
    {
        private static final PreferencesUtils INSTANCE=new PreferencesUtils();
    }
    public static class SharedPreferencesProxy implements SharedPreferences
    {
        private final SharedPreferences mSharedPreferences;
        
        SharedPreferencesProxy(SharedPreferences sharedPreferences)
        {
            mSharedPreferences=sharedPreferences;
        }
        
        @Override
        public Map<String,?> getAll()
        {
            try
            {
                return mSharedPreferences.getAll();
            }
            catch(NullPointerException nullPointerException)
            {
                AssertUtil.fail(nullPointerException);
            }
            return Collections.emptyMap();
        }
        
        @Nullable
        @Override
        public String getString(String key,@Nullable String defValue)
        {
            try
            {
                return mSharedPreferences.getString(key,defValue);
            }
            catch(ClassCastException classCastException)
            {
                AssertUtil.fail(classCastException);
            }
            return defValue;
        }
        
        @Nullable
        @Override
        public Set<String> getStringSet(String key,@Nullable Set<String> defValues)
        {
            try
            {
                return mSharedPreferences.getStringSet(key,defValues);
            }
            catch(ClassCastException classCastException)
            {
                AssertUtil.fail(classCastException);
            }
            return defValues;
        }
        
        @Override
        public int getInt(String key,int defValue)
        {
            try
            {
                return mSharedPreferences.getInt(key,defValue);
            }
            catch(ClassCastException classCastException)
            {
                AssertUtil.fail(classCastException);
            }
            return defValue;
        }
        
        @Override
        public long getLong(String key,long defValue)
        {
            try
            {
                return mSharedPreferences.getLong(key,defValue);
            }
            catch(ClassCastException classCastException)
            {
                AssertUtil.fail(classCastException);
            }
            return defValue;
        }
        
        @Override
        public float getFloat(String key,float defValue)
        {
            try
            {
                return mSharedPreferences.getFloat(key,defValue);
            }
            catch(ClassCastException classCastException)
            {
                AssertUtil.fail(classCastException);
            }
            return defValue;
        }
        
        @Override
        public boolean getBoolean(String key,boolean defValue)
        {
            try
            {
                return mSharedPreferences.getBoolean(key,defValue);
            }
            catch(ClassCastException classCastException)
            {
                AssertUtil.fail(classCastException);
            }
            return defValue;
        }
        
        @Override
        public boolean contains(String key)
        {
            return mSharedPreferences.contains(key);
        }
        
        @Override
        public Editor edit()
        {
            return mSharedPreferences.edit();
        }
        
        @Override
        public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
        {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        }
        
        @Override
        public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
        {
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        }
        
        public void setString(String key,String value)
        {
            Editor editor=edit();
            editor.putString(key,value);
            editor.apply();
        }
        
        public void setBoolean(String key,boolean value)
        {
            Editor editor=edit();
            editor.putBoolean(key,value);
            editor.apply();
        }
        
        public void setLong(String key,long value)
        {
            Editor editor=edit();
            editor.putLong(key,value);
            editor.apply();
        }
        
        public void setInt(String key,int value)
        {
            Editor editor=edit();
            editor.putInt(key,value);
            editor.apply();
        }
    }
}
