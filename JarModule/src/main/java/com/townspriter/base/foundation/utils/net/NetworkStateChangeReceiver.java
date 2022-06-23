package com.townspriter.base.foundation.utils.net;

import java.util.ArrayList;
import java.util.List;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.net.NetworkUtil.NetworkArgs;
import com.townspriter.base.foundation.utils.system.SystemHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/******************************************************************************
 * @path NetworkStateChangeReceiver
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class NetworkStateChangeReceiver extends BroadcastReceiver
{
    private static final String INTENT_ACTION_NET_CONNECTION_CHANGED="android.net.conn.CONNECTIVITY_CHANGE";
    private final List<INetworkStateChangedListener> mListeners=new ArrayList<>(8);
    
    private NetworkStateChangeReceiver()
    {}
    
    public static NetworkStateChangeReceiver getInstance()
    {
        return Singleton.HOLDER;
    }
    
    public void registerNetworkStateChangedListener(INetworkStateChangedListener listener)
    {
        synchronized(mListeners)
        {
            mListeners.add(listener);
        }
    }
    
    public void unregisterNetworkStateChangedListener(INetworkStateChangedListener listener)
    {
        synchronized(mListeners)
        {
            mListeners.remove(listener);
        }
    }
    
    public void registerNetworkStateChangedReceiver(Context applicationContext)
    {
        IntentFilter intentFilter=new IntentFilter(INTENT_ACTION_NET_CONNECTION_CHANGED);
        applicationContext.registerReceiver(this,intentFilter);
    }
    
    @Override
    public void onReceive(Context context,Intent intent)
    {
        if(context!=null&&intent!=null)
        {
            final NetworkArgs args=new NetworkArgs();
            ThreadManager.post(ThreadManager.THREADxWORK,new Runnable()
            {
                @Override
                public void run()
                {
                    args.mActiveNetworkInfo=NetworkUtil.getActiveNetworkInfo();
                    args.mIsConnected=NetworkUtil.isNetworkConnected();
                    args.mCurrAccessPointType=NetworkUtil.getCurrAccessPointType();
                    args.mCurrentIAPName=SystemHelper.getInstance().getCurrentIAPName();
                    args.mIsMobileNetwork=NetworkUtil.isMobileNetwork();
                    args.mIsWifi=NetworkUtil.isWiFiNetwork();
                    args.mAccessPointName=NetworkUtil.getAccessPointName();
                }
            },new Runnable()
            {
                @Override
                public void run()
                {
                    NetworkUtil.setNetworkArgs(args);
                    notifyNetworkStateChanged();
                    NetworkUtil.setNetworkArgs(null);
                }
            });
        }
    }
    
    private void notifyNetworkStateChanged()
    {
        boolean isWifi=false;
        boolean isAbove4G=false;
        boolean isConnected=NetworkUtil.isNetworkConnected();
        if(isConnected)
        {
            int type=NetworkUtil.getCurrAccessPointType();
            isWifi=(type==NetworkUtil.NETWORKxAPxTYPExWIFI);
            isAbove4G=NetworkUtil.is4GAboveNetwork();
        }
        synchronized(mListeners)
        {
            for(INetworkStateChangedListener listener:mListeners)
            {
                listener.onStateChanged(new NetworkState(isWifi,isAbove4G,isConnected));
            }
        }
    }
    
    public interface INetworkStateChangedListener
    {
        void onStateChanged(NetworkState state);
    }
    static class Singleton
    {
        static final NetworkStateChangeReceiver HOLDER=new NetworkStateChangeReceiver();
    }
}
