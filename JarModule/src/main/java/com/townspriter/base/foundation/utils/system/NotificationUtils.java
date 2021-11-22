package com.townspriter.base.foundation.utils.system;

import com.townspriter.base.foundation.utils.reflect.ReflectionHelper;
import android.content.Context;
import android.os.Build.VERSION;

/******************************************************************************
 * @path Foundation:NotificationUtils
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
