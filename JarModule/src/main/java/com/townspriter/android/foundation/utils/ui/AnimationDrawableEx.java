package com.townspriter.android.foundation.utils.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path Foundation:AnimationDrawableEx
 * @Describe 扩展动画监听器
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-5-16
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class AnimationDrawableEx extends AnimationDrawable
{
    private static final int MAXxDURATION=1000;
    private final Handler mHandler;
    private final Runnable mRunnable;
    private @Nullable OnAnimationListener animationListener;
    private int maxDuration;
    
    public AnimationDrawableEx()
    {
        mHandler=new Handler();
        mRunnable=new Runnable()
        {
            @Override
            public void run()
            {
                // 获取最后一帧和当前帧做比较.如果相等就结束动画并调用动画结束回调
                if(AnimationDrawableEx.this.getFrame(AnimationDrawableEx.this.getNumberOfFrames()-1)!=AnimationDrawableEx.this.getCurrent())
                {
                    AnimationDrawableEx.this.initHandler();
                }
                else
                {
                    AnimationDrawableEx.this.unRunning();
                }
            }
        };
    }
    
    public void setAnimationListener(@Nullable OnAnimationListener animationListener)
    {
        this.animationListener=animationListener;
    }
    
    /**
     * 重写开始方法监听动画是否结束
     */
    @Override
    public void start()
    {
        super.start();
        initHandler();
        if(animationListener!=null)
        {
            animationListener.onStart();
        }
    }
    
    @Override
    public void stop()
    {
        super.stop();
        unRunning();
    }
    
    /**
     * 获取持续时间最长的帧的持续时间
     *
     * @return 时间
     * 如果这一帧大于一秒则返回一秒.否则返回这一帧的持续时间
     */
    private int getMaxDuration()
    {
        for(int i=0;i<this.getNumberOfFrames();i++)
        {
            if(maxDuration<getDuration(i))
            {
                maxDuration=getDuration(i);
            }
        }
        return maxDuration>MAXxDURATION?MAXxDURATION:maxDuration;
    }
    
    private void initHandler()
    {
        mHandler.postDelayed(mRunnable,maxDuration==0?getMaxDuration():maxDuration);
    }
    
    /** 动画不在运行.触发结束回调 */
    private void unRunning()
    {
        if(animationListener!=null)
        {
            animationListener.onEnd();
        }
        mHandler.removeCallbacks(mRunnable);
    }
    
    /**
     * 动画监听器
     */
    public interface OnAnimationListener
    {
        /** 动画开始 */
        void onStart();
        
        /** 动画结束 */
        void onEnd();
    }
}
