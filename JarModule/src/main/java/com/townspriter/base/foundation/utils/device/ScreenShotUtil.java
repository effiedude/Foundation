package com.townspriter.base.foundation.utils.device;

import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.os.SystemUIUtil;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import com.townspriter.base.foundation.utils.system.SystemUtil;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

/******************************************************************************
 * @path Foundation:ScreenShotUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ScreenShotUtil
{
    public static Bitmap getBackWindowBitmap(Activity activity,View view)
    {
        Bitmap cacheBitmap;
        Bitmap snapshot=null;
        if(view==null)
        {
            return snapshot;
        }
        try
        {
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            view.buildDrawingCache();
            cacheBitmap=view.getDrawingCache();
            if(cacheBitmap==null||cacheBitmap.isRecycled())
            {
                return snapshot;
            }
            /** 获取状态栏的高度 */
            int statusBarHeight=SystemUtil.getStatusBarHeight(activity);
            /** 获取屏幕的宽高 */
            int screenWidth=SystemInfo.INSTANCE.getScreenWidth(activity);
            int screenHeight=SystemInfo.INSTANCE.getScreenRealHeight(activity,true);
            int bitmapHeight=screenHeight-statusBarHeight;
            bitmapHeight=statusBarHeight+bitmapHeight>view.getHeight()?view.getHeight()-statusBarHeight:bitmapHeight;
            snapshot=Bitmap.createBitmap(cacheBitmap,0,statusBarHeight,screenWidth,bitmapHeight);
        }
        catch(Throwable throwable)
        {
            AssertUtil.fail(throwable);
            return null;
        }
        finally
        {
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }
        return snapshot;
    }
    
    public static Bitmap getBackWindowBitmap(Activity activity,View view,boolean hasStatusBar,int givenHeight)
    {
        Bitmap cacheBitmap;
        Bitmap snapshot=null;
        if(view==null)
        {
            return snapshot;
        }
        try
        {
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            view.buildDrawingCache();
            cacheBitmap=view.getDrawingCache();
            if(cacheBitmap==null||cacheBitmap.isRecycled())
            {
                return snapshot;
            }
            /** 获取状态栏的高度 */
            int statusBarHeight;
            if(hasStatusBar)
            {
                statusBarHeight=SystemUIUtil.getStatusBarHeight();
            }
            else
            {
                statusBarHeight=0;
            }
            /** 获取屏幕的宽高 */
            int screenWidth=SystemInfo.INSTANCE.getScreenWidth(activity);
            int bitmapHeight=givenHeight;
            if(bitmapHeight==0)
            {
                /**
                 * 增加兜底处理.如果高度参数为零情况下.计算屏幕中窗口显示区域来进行截图
                 */
                int screenHeight=SystemInfo.INSTANCE.getScreenHeight(activity);
                if(screenHeight>view.getHeight())
                {
                    bitmapHeight=view.getHeight()-statusBarHeight;
                }
                else
                {
                    bitmapHeight=screenHeight-statusBarHeight;
                }
            }
            else if(bitmapHeight>view.getHeight()-statusBarHeight)
            {
                bitmapHeight=view.getHeight()-statusBarHeight;
            }
            snapshot=Bitmap.createBitmap(cacheBitmap,0,statusBarHeight,screenWidth,bitmapHeight);
        }
        catch(Throwable throwable)
        {
            AssertUtil.fail(throwable);
            return null;
        }
        finally
        {
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }
        return snapshot;
    }
}