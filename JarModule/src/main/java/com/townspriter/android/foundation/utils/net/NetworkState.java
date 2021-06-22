package com.townspriter.android.foundation.utils.net;
/******************************************************************************
 * @Path Foundation:NetworkState
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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