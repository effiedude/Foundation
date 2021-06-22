package com.townspriter.android.foundation.utils.safety;

import com.townspriter.android.foundation.utils.lang.AssertUtil;

/******************************************************************************
 * @Path Foundation:SafetyUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class SafetyUtil
{
    private static ISafety sSafetyImpl;
    
    public static void setSafetyImpl(ISafety impl)
    {
        AssertUtil.mustNotNull(impl);
        sSafetyImpl=impl;
    }
    
    public static String encrypt(String original)
    {
        AssertUtil.mustNotNull(sSafetyImpl);
        return sSafetyImpl!=null?sSafetyImpl.encrypt(original):null;
    }
    
    public static String decrypt(String encrypted)
    {
        AssertUtil.mustNotNull(sSafetyImpl);
        return sSafetyImpl!=null?sSafetyImpl.decrypt(encrypted):null;
    }
}
