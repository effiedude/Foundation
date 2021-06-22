package com.townspriter.android.foundation.utils.os;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/******************************************************************************
 * @Path Foundation:EquatableWeakReference
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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
