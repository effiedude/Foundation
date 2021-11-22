package com.townspriter.base.foundation.utils.ui;

import java.io.InputStream;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/******************************************************************************
 * @path Foundation:ResDelegate
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface ResDelegate
{
    Drawable getDrawable(int resID);
    
    int getColor(int resID);
    
    String locateAsset(@NonNull String assetName);
    
    InputStream getAssetInputStream(@NonNull String assetName,@Nullable IResLocateCallback callback);
    
    String locateRes(@RawRes int rawResId);
    
    InputStream getRawResInputStream(@RawRes int rawResId,@Nullable IResLocateCallback callback);
    
    String getString(int resId);
    
    String getString(int resId,Object...formatArgs);
    
    interface IResLocateCallback
    {
        void onHit(String source);
    }
}
