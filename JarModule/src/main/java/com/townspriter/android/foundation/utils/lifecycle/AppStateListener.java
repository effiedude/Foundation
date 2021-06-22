package com.townspriter.android.foundation.utils.lifecycle;
/******************************************************************************
 * @Path Foundation:AppStateListener
 * @Describe 当应用进入前后台以后触发此监听函数
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public interface AppStateListener
{
    /** 应用进入后台 */
    void onEnterBackground();
    
    /** 应用进入前台 */
    void onEnterForeground();
}
