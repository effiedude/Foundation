package com.townspriter.android.foundation.utils.system;

import com.townspriter.android.foundation.utils.reflect.ReflectionHelper;
import android.content.Context;
import android.os.Build.VERSION;

/******************************************************************************
 * @Path Foundation:NotificationUtils
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class NotificationUtils
{
    /** 收起通知栏 */
    public static void collapseStatusBar(Context context)
    {
        try
        {
            Object statusBarManager=context.getSystemService("statusbar");
            if(statusBarManager==null)
            {
                return;
            }
            if(VERSION.SDK_INT<=16)
            {
                ReflectionHelper.invokeMethod(statusBarManager,"collapse");
            }
            else
            {
                ReflectionHelper.invokeMethod(statusBarManager,"collapsePanels");
            }
        }
        catch(Exception localException)
        {
            localException.printStackTrace();
        }
    }
}
