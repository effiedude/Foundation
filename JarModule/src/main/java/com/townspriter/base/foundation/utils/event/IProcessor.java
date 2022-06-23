package com.townspriter.base.foundation.utils.event;

import androidx.annotation.UiThread;

/******************************************************************************
 * @path IProcessor
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
@UiThread
public interface IProcessor
{
    /**
     * 处理来自父节点的消息
     *
     * @param id
     * 消息标示
     * @param params
     * 事件参数
     * @param result
     * 事件结果
     */
    boolean processCommand(int id,EventParams params,EventParams result);
}
