package com.townspriter.base.foundation.utils.lang;
/******************************************************************************
 * @path Triple
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Triple<F,S,T>
{
    public final F first;
    public final S second;
    public final T third;
    
    public Triple(F first,S second,T third)
    {
        this.first=first;
        this.second=second;
        this.third=third;
    }
    
    private static boolean obJequals(Object a,Object b)
    {
        return (a==b)||(a!=null&&a.equals(b));
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object instanceof Triple)
        {
            Triple<?,?,?> other=(Triple<?,?,?>)object;
            return obJequals(first,other.first)&&obJequals(second,other.second)&&obJequals(third,other.third);
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return (first==null?0:first.hashCode())^(second==null?0:second.hashCode())^(third==null?0:third.hashCode());
    }
    
    @Override
    public String toString()
    {
        return "Triple{"+first+" "+second+" "+third+"}";
    }
}
