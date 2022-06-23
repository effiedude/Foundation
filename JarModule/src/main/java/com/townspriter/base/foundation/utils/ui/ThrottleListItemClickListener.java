package com.townspriter.base.foundation.utils.ui;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/******************************************************************************
 * @path ThrottleListItemClickListener
 * @describe 防止列表子控件频繁重复点击
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
