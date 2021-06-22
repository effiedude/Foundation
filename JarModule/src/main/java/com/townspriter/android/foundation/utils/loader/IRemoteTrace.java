package com.townspriter.android.foundation.utils.loader;
/******************************************************************************
 * @Path Foundation:IRemoteTrace
 * @Describe 远程日志注入接口
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public interface IRemoteTrace
{
    /** 收集日志内容 */
    void traceContent(String content);
}
