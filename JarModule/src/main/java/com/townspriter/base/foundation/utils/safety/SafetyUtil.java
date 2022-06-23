package com.townspriter.base.foundation.utils.safety;

import com.townspriter.base.foundation.utils.lang.AssertUtil;

/******************************************************************************
 * @path SafetyUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
