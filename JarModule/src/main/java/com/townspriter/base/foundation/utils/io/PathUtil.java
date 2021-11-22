package com.townspriter.base.foundation.utils.io;

import java.io.File;
import android.text.TextUtils;
import androidx.annotation.NonNull;

/******************************************************************************
 * @path Foundation:PathUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
