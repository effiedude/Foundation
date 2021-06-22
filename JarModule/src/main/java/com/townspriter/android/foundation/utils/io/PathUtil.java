package com.townspriter.android.foundation.utils.io;

import java.io.File;
import android.text.TextUtils;
import androidx.annotation.NonNull;

/******************************************************************************
 * @Path Foundation:PathUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class PathUtil
{
    private PathUtil()
    {}
    
    @NonNull
    public static String getParent(@NonNull String path)
    {
        if(TextUtils.isEmpty(path))
        {
            return "";
        }
        int index=path.lastIndexOf(File.separator);
        if(index>=0)
        {
            return path.substring(0,index);
        }
        return "";
    }
    
    @NonNull
    public static String getFileName(@NonNull String path)
    {
        if(TextUtils.isEmpty(path))
        {
            return "";
        }
        int index=path.lastIndexOf(File.separator);
        if(index>=0)
        {
            return path.substring(index+1);
        }
        return path;
    }
}
