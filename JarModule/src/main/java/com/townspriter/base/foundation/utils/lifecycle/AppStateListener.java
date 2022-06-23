package com.townspriter.base.foundation.utils.lifecycle;
/******************************************************************************
 * @path AppStateListener
 * @describe 当应用进入前后台以后触发此监听函数
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface AppStateListener
{
    /** 应用进入后台 */
    void onEnterBackground();
    
    /** 应用进入前台 */
    void onEnterForeground();
}
