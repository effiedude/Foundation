package com.townspriter.base.foundation.utils.text;

import com.townspriter.base.foundation.utils.ui.FontsOverride;
import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;

/******************************************************************************
 * @path TypeFaceUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class TypeFaceUtil
{
    public static final String SYSxDEFAULT="DEFAULT";
    public static final String SYSxDEFAULTxBOLD="DEFAULT_BOLD";
    public static final String SYSxSANSxSERIF="SANS_SERIF";
    public static final String SYSxSERIF="SERIF";
    public static final String SYSxMONOSPACE="MONOSPACE";
    private static Typeface mTypeface;
    
    public static Typeface getSystemTypeface(String typeface)
    {
        return FontsOverride.getSysTypeface(typeface);
    }
    
    public static Typeface createTypeface(@NonNull Context context,@NonNull String path)
    {
        if(mTypeface!=null)
        {
            return mTypeface;
        }
        Typeface typeface=null;
        try
        {
            typeface=Typeface.createFromAsset(context.getApplicationContext().getAssets(),path);
            mTypeface=typeface;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(null==typeface)
        {
            try
            {
                typeface=Typeface.createFromAsset(context.getApplicationContext().getAssets(),path);
                mTypeface=typeface;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return typeface;
    }
}
