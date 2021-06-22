package com.townspriter.android.foundation.utils.app;

import com.townspriter.android.foundation.utils.log.Logger;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path Foundation:AppUtils
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class AppUtils
{
    private static final String TAG="AppUtils";
    
    private AppUtils()
    {}
    
    @Nullable
    public static String getMetaValue(@NonNull Context context,@NonNull String metaKey)
    {
        try
        {
            ApplicationInfo applicationInfo=context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            Bundle bundle=applicationInfo.metaData;
            return bundle.getString(metaKey);
        }
        catch(Exception exception)
        {
            Logger.e(TAG,"getMetaValue:Exception",exception);
        }
        return null;
    }
}
