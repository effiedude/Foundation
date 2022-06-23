package com.townspriter.base.foundation.utils.safety;
/******************************************************************************
 * @path ISafety
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface ISafety
{
    String encrypt(String original);
    
    String decrypt(String encrypted);
}
