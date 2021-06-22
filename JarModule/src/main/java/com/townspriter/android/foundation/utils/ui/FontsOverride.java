package com.townspriter.android.foundation.utils.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.townspriter.android.foundation.utils.device.RomUtils;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

/******************************************************************************
 * @path Foundation:FontsOverride
 * @version 1.0.0.0
 * @describe 通过反射替换所有系统字体为自定义的字体.替换完毕后需要重新设置字体样式为任意字体类型才能生效.方案来自反解的澎湃新闻
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-05
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class FontsOverride
{
    public static final String SYSxDEFAULTxFIELD="DEFAULT";
    public static final String SYSxDEFAULTxBOLDxFIELD="DEFAULT_BOLD";
    public static final String SYSxSANSxSERIFxFIELD="SANS_SERIF";
    public static final String SYSxSERIFxFIELD="SERIF";
    public static final String SYSxMONOSPACExFIELD="MONOSPACE";
    private static final Map<String,Typeface> sSysTypefaceMap=new HashMap<>(5);
    
    public static void execute(Context context,String fontAssetName)
    {
        final Typeface newTypeface=Typeface.createFromAsset(context.getAssets(),fontAssetName);
        try
        {
            setTypeFaceField(SYSxDEFAULTxFIELD,newTypeface);
            setTypeFaceField(SYSxDEFAULTxBOLDxFIELD,newTypeface);
            setTypeFaceField(SYSxSANSxSERIFxFIELD,newTypeface);
            setTypeFaceField(SYSxSERIFxFIELD,newTypeface);
            setTypeFaceField(SYSxMONOSPACExFIELD,newTypeface);
            setTypeFaceField("sDefaults",new Typeface[]{newTypeface,newTypeface,newTypeface,newTypeface});
            invokeTypeFaceMethod("setDefault",newTypeface);
            setTypeFaceMap(newTypeface);
            if(RomUtils.isOPPOROM())
            {
                hookOPPODevice(newTypeface);
            }
        }
        catch(Throwable throwable)
        {
            traceException(throwable);
        }
    }
    
    private static void setTypeFaceField(String name,Object param)
    {
        try
        {
            Field field=Typeface.class.getDeclaredField(name);
            field.setAccessible(true);
            switch(name)
            {
                case SYSxDEFAULTxFIELD:
                case SYSxDEFAULTxBOLDxFIELD:
                case SYSxSANSxSERIFxFIELD:
                case SYSxSERIFxFIELD:
                case SYSxMONOSPACExFIELD:
                    sSysTypefaceMap.put(name,(Typeface)field.get(null));
                    break;
                default:
                    break;
            }
            field.set(null,param);
        }
        catch(Exception exception)
        {
            traceException(exception);
        }
    }
    
    private static void invokeTypeFaceMethod(String name,Object param)
    {
        try
        {
            Method method=Typeface.class.getDeclaredMethod(name,Typeface.class);
            method.setAccessible(true);
            method.invoke(null,param);
        }
        catch(Exception exception)
        {
            traceException(exception);
        }
    }
    
    private static void setTypeFaceMap(Typeface newTypeface)
    {
        try
        {
            Field mapField=null;
            if(isVersionGreaterOrEqualToLollipop())
            {
                mapField=Typeface.class.getDeclaredField("sSystemFontMap");
            }
            if(mapField==null)
            {
                return;
            }
            mapField.setAccessible(true);
            Map map=(Map)mapField.get(null);
            if(map==null||map.isEmpty())
            {
                return;
            }
            Iterator<Entry<String,Typeface>> it=map.entrySet().iterator();
            Map<String,Typeface> newMap=new HashMap<>(map.size());
            while(it.hasNext())
            {
                Map.Entry<String,Typeface> entry=it.next();
                newMap.put(entry.getKey(),newTypeface);
            }
            mapField.set(null,newMap);
        }
        catch(Exception exception)
        {
            traceException(exception);
        }
    }
    
    private static void hookOPPODevice(Typeface newTypeface)
    {
        try
        {
            Class<?> clazz=Class.forName("oppo.content.res.OppoFontUtils");
            Field field=clazz.getDeclaredField("sCurrentTypefaces");
            field.setAccessible(true);
            Typeface[] originArray=(Typeface[])field.get(null);
            if(originArray!=null)
            {
                for(int i=0;i<originArray.length;++i)
                {
                    originArray[i]=newTypeface;
                }
            }
            field=clazz.getDeclaredField("DEFAULT_BOLD_ITALIC");
            field.setAccessible(true);
            field.set(null,newTypeface);
            field=clazz.getDeclaredField("DEFAULT_ITALIC");
            field.setAccessible(true);
            field.set(null,newTypeface);
        }
        catch(Exception exception)
        {
            traceException(exception);
        }
    }
    
    private static boolean isVersionGreaterOrEqualToLollipop()
    {
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP;
    }
    
    /** 获取系统字体 */
    public static Typeface getSysTypeface(String typeface)
    {
        if(sSysTypefaceMap!=null)
        {
            return sSysTypefaceMap.get(typeface);
        }
        return null;
    }
    
    private static void traceException(Throwable throwable)
    {
        throwable.printStackTrace();
    }
}
