package com.townspriter.base.foundation.utils.loader;
/******************************************************************************
 * @path Foundation:IRemoteTrace
 * @describe 远程日志注入接口
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface IRemoteTrace
{
    /** 收集日志内容 */
    void traceContent(String content);
}
