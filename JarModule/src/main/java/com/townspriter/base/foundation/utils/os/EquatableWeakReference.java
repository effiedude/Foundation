package com.townspriter.base.foundation.utils.os;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/******************************************************************************
 * @path EquatableWeakReference
 * @describe 签名辅助类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class EquatableWeakReference<T>extends WeakReference<T>
{
    public EquatableWeakReference(T referent)
    {
        super(referent);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean ret=false;
        if(obj instanceof Reference)
        {
            Object myObj=get();
            Object otherObj=((Reference)obj).get();
            if(myObj==null)
            {
                ret=otherObj==null;
            }
            else
            {
                ret=myObj.equals(otherObj);
            }
        }
        return ret;
    }
    
    @Override
    public int hashCode()
    {
        return null!=get()?get().hashCode():0;
    }
}
