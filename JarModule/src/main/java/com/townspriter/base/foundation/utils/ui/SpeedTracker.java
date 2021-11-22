package com.townspriter.base.foundation.utils.ui;

import java.util.LinkedList;
import android.os.SystemClock;

/******************************************************************************
 * @path Foundation:SpeedTracker
 * @describe 可滚动控件滚动速度计算器
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class SpeedTracker
{
    private static final int DEFAULTxCOUNT=3;
    private final LinkedList<Integer> mSpeedItems=new LinkedList<>();
    private long mLastTime;
    private int mLastPosition;
    
    /**
     * 根据事件位置记录
     *
     * @param y
     * 事件位置.需要计算前后两次位置差值得出速度值
     */
    public void trackPosition(int y)
    {
        long currTime=SystemClock.uptimeMillis();
        long time=currTime-mLastTime;
        if(time>0)
        {
            int dis=y-mLastPosition;
            int vs=(int)(dis*1000/time);
            mSpeedItems.addFirst(vs);
        }
        mLastTime=currTime;
        mLastPosition=y;
    }
    
    /**
     * 根据距离增量记录
     *
     * @param distance
     * 每次打点距离增量
     */
    public void trackDistance(int distance)
    {
        long currTime=SystemClock.uptimeMillis();
        long time=currTime-mLastTime;
        if(time>0)
        {
            int vs=(int)(distance*1000/time);
            mSpeedItems.addFirst(vs);
        }
        mLastTime=currTime;
    }
    
    /**
     * 多少次记录点的平均速度值
     *
     * @param count
     * 平均点数
     * @return
     * 速度均值
     */
    public int getAverageSpeed(int count)
    {
        int vs=0;
        for(int i=0;i<Math.min(count,mSpeedItems.size());i++)
        {
            vs+=mSpeedItems.get(i);
        }
        mSpeedItems.clear();
        return vs/count;
    }
    
    public int getSpeed()
    {
        return getAverageSpeed(DEFAULTxCOUNT);
    }
}
