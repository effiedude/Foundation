package com.townspriter.base.foundation.utils.math;
/******************************************************************************
 * @path MathUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class MathUtil
{
    /**
     * 如果值在最大与最小(包含)区间内.返回值本身
     * 如果值小于最小值则返回最小值
     * 如果值大于最大值则返回最大值
     *
     * @param value
     * 值
     * @param lowerBound
     * 最小值
     * @param upperBound
     * 最大值
     * @return
     */
    public static int correctRange(int value,int lowerBound,int upperBound)
    {
        if(lowerBound>upperBound)
        {
            throw new IllegalArgumentException("lowerBound <= upperBound");
        }
        if(value<lowerBound)
        {
            return lowerBound;
        }
        else if(value>upperBound)
        {
            return upperBound;
        }
        else
        {
            return value;
        }
    }
    
    /**
     * 如果值在最大与最小(包含)区间内.返回值本身.否则返回默认值
     *
     * @param value
     * 值
     * @param lowerBound
     * 最小值
     * @param upperBound
     * 最大值
     * @param defValue
     * 默认值
     * @return
     */
    public static int correctDefault(int value,int lowerBound,int upperBound,int defValue)
    {
        if(lowerBound<=value&&value<=upperBound)
        {
            return value;
        }
        else
        {
            return defValue;
        }
    }
    
    public static boolean rangeIn(int val,int min,int max)
    {
        return val<=max&&val>=min;
    }
    
    public static boolean rangeIn(long val,long min,long max)
    {
        return val<=max&&val>=min;
    }
}
