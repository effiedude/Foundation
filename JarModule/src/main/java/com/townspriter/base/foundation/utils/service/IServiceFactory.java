package com.townspriter.base.foundation.utils.service;

import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:IServiceFactory
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface IServiceFactory
{
    @Nullable
    <T> T createService(Class<T> clazz);
}
