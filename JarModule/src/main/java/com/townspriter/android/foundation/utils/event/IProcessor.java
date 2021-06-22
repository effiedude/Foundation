package com.townspriter.android.foundation.utils.event;

import androidx.annotation.UiThread;

/******************************************************************************
 * @Path Foundation:IProcessor
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
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
