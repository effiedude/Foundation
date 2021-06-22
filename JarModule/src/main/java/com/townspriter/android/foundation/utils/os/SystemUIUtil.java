package com.townspriter.android.foundation.utils.os;

import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.lang.AssertUtil;

/******************************************************************************
 * @Path Foundation:SystemUIUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class SystemUIUtil
{
    private static int sStatusBarHeight;
    private static boolean sHasCheckStatusBarHeight;
    
    public static int getStatusBarHeight()
    {
        if(sHasCheckStatusBarHeight)
        {
            return sStatusBarHeight;
        }
        int result=-1;
        int resourceId=Foundation.getApplication().getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0)
        {
            result=Foundation.getApplication().getResources().getDimensionPixelSize(resourceId);
        }
        if(result==-1)
        {
            result=guessStatusBarHeight();
        }
        if(result<0)
        {
            result=0;
        }
        sHasCheckStatusBarHeight=true;
        sStatusBarHeight=result;
        return result;
    }
    
    private static int guessStatusBarHeight()
    {
        try
        {
            int statusBarHeightDP=25;
            float density=Foundation.getApplication().getResources().getDisplayMetrics().density;
            return Math.round(density*statusBarHeightDP);
        }
        catch(Exception exception)
        {
            AssertUtil.fail(exception);
        }
        return 0;
    }
}
