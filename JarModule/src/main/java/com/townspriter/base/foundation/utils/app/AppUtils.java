package com.townspriter.base.foundation.utils.app;

import com.townspriter.base.foundation.utils.log.Logger;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:AppUtils
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:06:37
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
