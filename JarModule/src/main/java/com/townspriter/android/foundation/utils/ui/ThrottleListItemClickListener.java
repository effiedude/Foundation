package com.townspriter.android.foundation.utils.ui;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/******************************************************************************
 * @Path Foundation:ThrottleListItemClickListener
 * @Describe 防止列表子控件频繁重复点击
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-05
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public abstract class ThrottleListItemClickListener implements AbsListView.OnItemClickListener
{
    /** 两次点击之间的点击间隔不能少于四百毫秒 */
    private static final int MINxCLICKxDELAYxTIME=400;
    private long mLastClickTime;
    
    public abstract void onItemSafeClick(AdapterView<?> parent,View view,int position,long id);
    
    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id)
    {
        long curClickTime=SystemClock.elapsedRealtime();
        if((curClickTime-mLastClickTime)>=MINxCLICKxDELAYxTIME)
        {
            mLastClickTime=curClickTime;
            onItemSafeClick(parent,view,position,id);
        }
    }
}
