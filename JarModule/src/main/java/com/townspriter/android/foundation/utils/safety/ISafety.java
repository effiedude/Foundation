package com.townspriter.android.foundation.utils.safety;
/******************************************************************************
 * @Path Foundation:ISafety
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public interface ISafety
{
    String encrypt(String original);
    
    String decrypt(String encrypted);
}
