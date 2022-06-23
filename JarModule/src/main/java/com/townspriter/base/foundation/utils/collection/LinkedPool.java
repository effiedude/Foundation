package com.townspriter.base.foundation.utils.collection;

import com.townspriter.base.foundation.utils.collection.LinkedPool.ILinkedPoolable;

/******************************************************************************
 * @path LinkedPool
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
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