package com.townspriter.android.foundation.utils.system;

import java.util.ArrayList;
import java.util.List;
import com.townspriter.android.foundation.utils.os.HandlerEx;
import android.os.Looper;
import android.os.Message;

/******************************************************************************
 * @Path Foundation:Timer
 * @Describe 定时器
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class Timer
{
    private static final int REPEATxFLAG=9527;
    private final List<IConfig> mConfigList=new ArrayList<>();
    private final HandlerEx mHandle=new HandlerEx(Timer.class.getName(),Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            IConfig config=find(msg.what);
            config.getRunnable().run();
            if(REPEATxFLAG==msg.arg1)
            {
                doAction(msg.what,true,true);
            }
        }
    };
    
    public Timer(IConfig...aConfigs)
    {
        for(IConfig config:aConfigs)
        {
            if(0>=config.getId())
            {
                throw new RuntimeException("需要大于零的标示");
            }
            mConfigList.add(config);
        }
    }
    
    private IConfig find(int aId)
    {
        IConfig ret=null;
        for(IConfig config:mConfigList)
        {
            if(aId==config.getId())
            {
                ret=config;
                break;
            }
        }
        return ret;
    }
    
    /** 如果所对应的任务已存在会先移出任务队列 */
    public void doDelay(int aId)
    {
        doAction(aId,true,false);
    }
    
    public void doRepeat(int aId)
    {
        doAction(aId,false,true);
    }
    
    public void doRepeatWithDelay(int aId)
    {
        doAction(aId,true,true);
    }
    
    private void doAction(int aId,boolean aDelayForRepeat,boolean aRepeat)
    {
        mHandle.removeMessages(aId);
        if(aRepeat)
        {
            Message msg=Message.obtain();
            msg.what=aId;
            if(aRepeat)
            {
                msg.arg1=REPEATxFLAG;
            }
            if(aDelayForRepeat)
            {
                IConfig config=find(aId);
                if(config!=null)
                {
                    mHandle.sendMessageDelayed(msg,config.getTime());
                }
            }
            else
            {
                mHandle.sendMessage(msg);
            }
        }
        else
        {
            IConfig config=find(aId);
            if(config!=null)
            {
                mHandle.sendEmptyMessageDelayed(aId,config.getTime());
            }
        }
    }
    
    public void stop(int aId)
    {
        mHandle.removeMessages(aId);
    }
    
    public void stopAll()
    {
        for(IConfig config:mConfigList)
        {
            mHandle.removeMessages(config.getId());
        }
    }
    
    public interface IConfig
    {
        int getId();
        
        Runnable getRunnable();
        
        int getTime();
    }
}
