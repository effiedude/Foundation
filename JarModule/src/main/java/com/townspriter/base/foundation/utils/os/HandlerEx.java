package com.townspriter.base.foundation.utils.os;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/******************************************************************************
 * @path Foundation:HandlerEx
 * @describe 签名辅助类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class HandlerEx extends Handler
{
    private static IHandlerExNotifier sHandlerExNotifier;
    private String mName;
    
    public HandlerEx(String name)
    {
        setName(name);
    }
    
    public HandlerEx(String name,Callback callback)
    {
        super(callback);
        setName(name);
    }
    
    public HandlerEx(String name,Looper looper)
    {
        super(looper);
        setName(name);
    }
    
    public HandlerEx(String name,Looper looper,Callback callback)
    {
        super(looper,callback);
        setName(name);
    }
    
    public static void setHandlerExNotifier(IHandlerExNotifier handlerExNotifier)
    {
        sHandlerExNotifier=handlerExNotifier;
    }
    
    public String getName()
    {
        return mName;
    }
    
    public void setName(String name)
    {
        mName=name;
    }
    
    @Override
    public String toString()
    {
        return "HandlerEx("+mName+"){}";
    }
    
    @Override
    public boolean sendMessageAtTime(Message msg,long uptimeMillis)
    {
        boolean sent=super.sendMessageAtTime(msg,uptimeMillis);
        IHandlerExNotifier handlerExNotifier=sHandlerExNotifier;
        if(handlerExNotifier!=null)
        {
            handlerExNotifier.onSendMessageAtTime(sent,msg,uptimeMillis);
        }
        return sent;
    }
    
    @Override
    public void dispatchMessage(Message msg)
    {
        IHandlerExNotifier handlerExNotifier=sHandlerExNotifier;
        if(handlerExNotifier!=null)
        {
            handlerExNotifier.onDispatchMessage(msg);
        }
        super.dispatchMessage(msg);
    }
    
    public interface IHandlerExNotifier
    {
        void onSendMessageAtTime(boolean ret,Message msg,long uptimeMillis);
        
        void onDispatchMessage(Message msg);
    }
}
