package com.townspriter.android.foundation.utils.ui;

import java.io.InputStream;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/******************************************************************************
 * @Path Foundation:ResDelegate
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-05
 * CopyRight(C)2021 小镇精灵科技版权所有
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
