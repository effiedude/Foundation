package com.townspriter.android.foundation.utils.service;

import androidx.annotation.Nullable;

/******************************************************************************
 * @Path Foundation:IServiceFactory
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public interface IServiceFactory
{
    @Nullable
    <T> T createService(Class<T> clazz);
}
