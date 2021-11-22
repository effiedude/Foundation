package com.townspriter.base.foundation.utils.ui;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.device.DisplayUtil;
import com.townspriter.base.foundation.utils.io.PathUtil;
import com.townspriter.base.foundation.utils.lang.AssertUtil;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/******************************************************************************
 * @path Foundation:ResHelper
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ResHelper
{
    public static final String SOURCExAPP="APP";
    public static Drawable sDefaultItemDrawable;
    @Nullable
    private static ResDelegate mResDelegate;
    private static WeakReference<Map<String,List<String>>> mAppAssetNamesCache;
    
    public static void setResDelegate(@Nullable final ResDelegate resDelegate)
    {
        mResDelegate=resDelegate;
    }
    
    public static Drawable getDefaultItemDrawable()
    {
        if(null==sDefaultItemDrawable)
        {
            sDefaultItemDrawable=new ColorDrawable(Color.BLUE);
        }
        return sDefaultItemDrawable;
    }
    
    public static Drawable getDrawable(int resID)
    {
        if(mResDelegate!=null)
        {
            return mResDelegate.getDrawable(resID);
        }
        Drawable ret=null;
        Resources resources=Foundation.getApplication().getResources();
        try
        {
            ret=resources.getDrawable(resID);
        }
        catch(NotFoundException notFoundException)
        {
            throwResourceNotFoundException(notFoundException);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return ret;
    }
    
    public static Drawable getDyeDrawable(@DrawableRes int resId,@ColorRes int colorRes)
    {
        return DrawableUtils.deepTintDrawable(resId,getColor(colorRes));
    }
    
    public static Bitmap getBitmap(int resId)
    {
        Bitmap ret=null;
        Resources resources=Foundation.getApplication().getResources();
        try
        {
            ret=BitmapFactory.decodeResource(resources,resId);
        }
        catch(NotFoundException notFoundException)
        {
            throwResourceNotFoundException(notFoundException);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return ret;
    }
    
    public static int getColor(int resID)
    {
        if(mResDelegate!=null)
        {
            return mResDelegate.getColor(resID);
        }
        int color=Color.WHITE;
        Resources resources=Foundation.getApplication().getResources();
        try
        {
            color=resources.getColor(resID);
        }
        catch(NotFoundException notFoundException)
        {
            throwResourceNotFoundException(notFoundException);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return color;
    }
    
    /**
     * 获取带透明度的颜色
     *
     * @param alphaId
     * 透明度资源标示
     * @param colorId
     * 颜色资源标示
     * @return 返回AARRGGBB值
     */
    public static int getColor(@IntegerRes int alphaId,@ColorRes int colorId)
    {
        /** 实体色 */
        int color=getColor(colorId)&0x00ffffff;
        /** 透明度 */
        int alpha=getInteger(255,alphaId);
        return (alpha<<24)|color;
    }
    
    /**
     * 获取颜色状态列表
     *
     * @param resId
     * 资源标示
     * @return 颜色状态列表
     */
    public static ColorStateList getColorStateList(@ColorRes int resId)
    {
        ColorStateList color=ColorStateList.valueOf(Color.WHITE);
        Resources resources=Foundation.getApplication().getResources();
        try
        {
            color=resources.getColorStateList(resId);
        }
        catch(NotFoundException exception)
        {
            throwResourceNotFoundException(exception);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return color;
    }
    
    public static int getInteger(int defaultValue,@IntegerRes int integerId)
    {
        int ret=defaultValue;
        try
        {
            ret=Foundation.getApplication().getResources().getInteger(integerId);
        }
        catch(NotFoundException exception)
        {
            throwResourceNotFoundException(exception);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return ret;
    }
    
    public static float getDimen(int resId)
    {
        return getDimen(true,resId);
    }
    
    /**
     * @param toGetPixelDimen
     * 是否允许四舍五入
     * @param resId
     * @return
     */
    private static float getDimen(boolean toGetPixelDimen,int resId)
    {
        float ret=0;
        try
        {
            Resources resources=Foundation.getApplication().getResources();
            if(toGetPixelDimen)
            {
                ret=resources.getDimensionPixelSize(resId);
            }
            else
            {
                ret=resources.getDimension(resId);
            }
        }
        catch(NotFoundException exception)
        {
            throwResourceNotFoundException(exception);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return ret;
    }
    
    public static int getDimenInt(int aResId)
    {
        return (int)getDimen(aResId);
    }
    
    public static String getString(int resId)
    {
        String ret="";
        try
        {
            if(mResDelegate!=null)
            {
                return mResDelegate.getString(resId);
            }
            ret=Foundation.getApplication().getResources().getString(resId);
        }
        catch(NotFoundException notFoundException)
        {
            throwResourceNotFoundException(notFoundException);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return ret;
    }
    
    public static String getString(int resId,Object...formatArgs)
    {
        String ret="";
        try
        {
            if(mResDelegate!=null)
            {
                return mResDelegate.getString(resId,formatArgs);
            }
            ret=Foundation.getApplication().getResources().getString(resId,formatArgs);
        }
        catch(NotFoundException notFoundException)
        {
            throwResourceNotFoundException(notFoundException);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return ret;
    }
    
    private static void ensureAppAssetNames(String assetFolder)
    {
        Map<String,List<String>> map;
        if(mAppAssetNamesCache==null||(map=mAppAssetNamesCache.get())==null)
        {
            map=new HashMap<>();
            mAppAssetNamesCache=new WeakReference<>(map);
        }
        List<String> assetNames=map.get(assetFolder);
        if(assetNames==null)
        {
            try
            {
                String[] files=Foundation.getApplication().getAssets().list(assetFolder);
                if(files!=null&&files.length>0)
                {
                    map.put(assetFolder,Arrays.asList(files));
                }
                else
                {
                    map.put(assetFolder,Collections.EMPTY_LIST);
                }
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
    
    @Nullable
    public static String locateAsset(@NonNull String assetName)
    {
        String ret=null;
        if(mResDelegate!=null)
        {
            ret=mResDelegate.locateAsset(assetName);
        }
        if(ret==null)
        {
            try
            {
                String assetFolder=PathUtil.getParent(assetName);
                ensureAppAssetNames(assetFolder);
                ret=mAppAssetNamesCache.get().get(assetFolder).contains(PathUtil.getFileName(assetName))?SOURCExAPP:null;
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        return ret;
    }
    
    @Nullable
    public static InputStream getAssetInputStream(@NonNull String assetName,@Nullable ResDelegate.IResLocateCallback locateCallback)
    {
        InputStream ret=null;
        if(mResDelegate!=null)
        {
            ret=mResDelegate.getAssetInputStream(assetName,locateCallback);
        }
        if(ret==null)
        {
            try
            {
                ret=Foundation.getApplication().getAssets().open(assetName);
                if(locateCallback!=null)
                {
                    locateCallback.onHit(SOURCExAPP);
                }
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        return ret;
    }
    
    @NonNull
    public static String locateRes(@RawRes int rawResId)
    {
        String ret=null;
        if(mResDelegate!=null)
        {
            ret=mResDelegate.locateRes(rawResId);
        }
        return ret==null?SOURCExAPP:ret;
    }
    
    @Nullable
    public static InputStream getRawResInputStream(@RawRes int rawResId,@Nullable ResDelegate.IResLocateCallback locateCallback)
    {
        InputStream ret=null;
        if(mResDelegate!=null)
        {
            ret=mResDelegate.getRawResInputStream(rawResId,locateCallback);
        }
        if(ret==null)
        {
            try
            {
                ret=Foundation.getApplication().getResources().openRawResource(rawResId);
                if(locateCallback!=null)
                {
                    locateCallback.onHit(SOURCExAPP);
                }
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        return ret;
    }
    
    public static int getBackgroundAlphaCompat(Drawable drawable)
    {
        if(VERSION.SDK_INT>=VERSION_CODES.KITKAT)
        {
            return drawable.getAlpha();
        }
        if(drawable instanceof ColorDrawable)
        {
            return ((ColorDrawable)drawable).getColor()>>>24;
        }
        else if(drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable)drawable).getPaint().getAlpha();
        }
        return 0;
    }
    
    public static float dpToPxF(float dips)
    {
        return(dips*DisplayUtil.getDensity()+0.5F);
    }
    
    public static int dpToPxI(float dips)
    {
        return DisplayUtil.convertDPToPixels(dips);
    }
    
    private static void throwResourceNotFoundException(NotFoundException exception) throws NotFoundException
    {
        if(null!=exception)
        {
            exception.printStackTrace();
            AssertUtil.fail();
        }
    }
    
    public static int pxToDpI(float pxValue)
    {
        return (int)(pxValue/DisplayUtil.getDensity()+0.5f);
    }
}
