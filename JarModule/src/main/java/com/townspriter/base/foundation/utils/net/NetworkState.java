package com.townspriter.base.foundation.utils.net;
/******************************************************************************
 * @path NetworkState
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class NetworkState
{
    public boolean isWifi;
    public boolean isAbove4G;
    public boolean isConnected;
    
    public NetworkState(boolean isWifi,boolean isConnected)
    {
        this.isWifi=isWifi;
        this.isConnected=isConnected;
    }
    
    public NetworkState(boolean isWifi,boolean isAbove4G,boolean isConnected)
    {
        this.isWifi=isWifi;
        this.isAbove4G=isAbove4G;
        this.isConnected=isConnected;
    }
    
    @Override
    public String toString()
    {
        return "isWifi:"+isWifi+"\nisConnected:"+isConnected;
    }
}