package com.townspriter.android.foundation.utils.collection;

import com.townspriter.android.foundation.utils.collection.LinkedPool.ILinkedPoolable;

/******************************************************************************
 * @Path Foundation:LinkedPool
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class LinkedPool<T extends ILinkedPoolable>
{
    private static final Object EMPTY=new Object();
    private final InstanceCreator<T> mCreator;
    private final int mMaxRecycled;
    private int mRecyclerUsed;
    private T mRecyclerTop;
    
    public LinkedPool(InstanceCreator<T> creator,int maxRecycled)
    {
        mCreator=creator;
        mMaxRecycled=maxRecycled;
    }
    
    @SuppressWarnings("unchecked")
    public T obtain()
    {
        T node=mRecyclerTop;
        if(node==null)
        {
            return mCreator.createInstance();
        }
        Object nextNode=node.getNext();
        node.setNext(null);
        mRecyclerTop=nextNode!=EMPTY?(T)nextNode:null;
        --mRecyclerUsed;
        return node;
    }
    
    public void recycle(T node)
    {
        ensureNotRecycled(node);
        Object nextNode=null;
        if(mRecyclerUsed<mMaxRecycled)
        {
            ++mRecyclerUsed;
            nextNode=mRecyclerTop;
            mRecyclerTop=node;
        }
        if(nextNode==null)
        {
            nextNode=EMPTY;
        }
        node.setNext(nextNode);
    }
    
    public void ensureNotRecycled(T node)
    {
        if(node.getNext()!=null)
        {
            throw new RuntimeException(node+" is recycled");
        }
    }
    
    public interface ILinkedPoolable
    {
        Object getNext();
        
        void setNext(Object nextNode);
    }
    public interface InstanceCreator<T>
    {
        T createInstance();
    }
}