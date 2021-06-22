package com.townspriter.android.foundation.utils.lang;
/******************************************************************************
 * @Path Foundation:Triple
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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
