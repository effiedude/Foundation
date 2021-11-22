package com.townspriter.base.foundation.utils.ui;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

/******************************************************************************
 * @path Foundation:ThrottleClickListener
 * @describe 防止频繁点击
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
