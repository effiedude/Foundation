package com.townspriter.android.foundation.utils.ui;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

/******************************************************************************
 * @Path Foundation:ThrottleClickListener
 * @Describe 防止频繁点击
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-05
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public abstract class ThrottleClickListener implements OnClickListener
{
    /** 两次点击之间的点击间隔不能少于五百毫秒 */
    private static final int MINxCLICKxDELAYxTIME=500;
    private static long sLastClickTime;
    
    public abstract void onSafeClick(View view);
    
    @Override
    public final void onClick(View view)
    {
        long curClickTime=SystemClock.elapsedRealtime();
        if((curClickTime-sLastClickTime)>=MINxCLICKxDELAYxTIME)
        {
            sLastClickTime=curClickTime;
            onSafeClick(view);
        }
    }
}
