package com.townspriter.base.foundation.utils.os;

import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.lang.AssertUtil;

/******************************************************************************
 * @path Foundation:SystemUIUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
