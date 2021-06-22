package com.townspriter.android.foundation.utils.service;
/******************************************************************************
 * @Path Foundation:ICallback
 * @Describe 异步回调接口
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public interface ICallback<T>
{
    /**
     * 通知异步处理结果
     *
     * @param result
     * 处理结果
     */
    void onResult(Result<T> result);
}
