package com.townspriter.base.foundation.utils.collection;
/******************************************************************************
 * @path Foundation:Predicate
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface Predicate<T>
{
    boolean evaluate(T object);
}
