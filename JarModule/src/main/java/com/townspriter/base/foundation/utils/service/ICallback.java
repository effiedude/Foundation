package com.townspriter.base.foundation.utils.service;
/******************************************************************************
 * @path Foundation:ICallback
 * @describe 异步回调接口
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
