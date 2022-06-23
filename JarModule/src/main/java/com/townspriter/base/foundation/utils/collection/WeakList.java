package com.townspriter.base.foundation.utils.collection;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/******************************************************************************
 * @path WeakList
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class WeakList<E>
{
    private final ReferenceQueue<Object> mDeadRefQueue=new ReferenceQueue<Object>();
    private final List<WeakReference<E>> mList;
    
    public WeakList()
    {
        mList=new ArrayList<WeakReference<E>>();
    }
    
    public int size()
    {
        removeDeadRef();
        return mList.size();
    }
    
    public boolean isEmpty()
    {
        removeDeadRef();
        return mList.isEmpty();
    }
    
    public boolean contains(Object o)
    {
        return indexOf(o)>=0;
    }
    
    public boolean add(E e)
    {
        return mList.add(new WeakReference<E>(e,mDeadRefQueue));
    }
    
    public void add(int index,E element)
    {
        WeakReference<E> ref=new WeakReference<E>(element,mDeadRefQueue);
        mList.add(index,ref);
    }
    
    public E get(int index)
    {
        WeakReference<E> ref=mList.get(index);
        return ref.get();
    }
    
    public boolean remove(Object o)
    {
        int index=indexOf(o);
        if(index>=0)
        {
            mList.remove(index);
            return true;
        }
        return false;
    }
    
    public E remove(int index)
    {
        WeakReference<E> ref=mList.remove(index);
        return ref.get();
    }
    
    public void clear()
    {
        mList.clear();
    }
    
    public Object[] toArray()
    {
        removeDeadRef();
        int size=size();
        Object[] array=new Object[size];
        for(int i=0;i<size;i++)
        {
            WeakReference<E> ref=mList.get(i);
            array[i]=ref.get();
        }
        return array;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
        removeDeadRef();
        boolean contain=true;
        @SuppressWarnings("unchecked")
        Iterator<? extends E> it=(Iterator<? extends E>)collection.iterator();
        while(it.hasNext())
        {
            if(contains(it.next()))
            {
                contain=false;
            }
        }
        return contain;
    }
    
    public boolean addAll(Collection<? extends E> collection)
    {
        Iterator<? extends E> it=collection.iterator();
        while(it.hasNext())
        {
            add(it.next());
        }
        return !collection.isEmpty();
    }
    
    public boolean addAll(int index,Collection<? extends E> collection)
    {
        Iterator<? extends E> it=collection.iterator();
        while(it.hasNext())
        {
            add(index++,it.next());
        }
        return !collection.isEmpty();
    }
    
    public boolean removeAll(Collection<?> collection)
    {
        removeDeadRef();
        boolean res=true;
        @SuppressWarnings("unchecked")
        Iterator<? extends E> it=(Iterator<? extends E>)collection.iterator();
        while(it.hasNext())
        {
            if(!remove(it.next()))
            {
                res=false;
            }
        }
        return res;
    }
    
    public boolean retainAll(Collection<?> collection)
    {
        mList.clear();
        boolean res=true;
        @SuppressWarnings("unchecked")
        Iterator<? extends E> it=(Iterator<? extends E>)collection.iterator();
        while(it.hasNext())
        {
            if(!add(it.next()))
            {
                res=false;
            }
        }
        return res;
    }
    
    public int indexOf(Object obj)
    {
        removeDeadRef();
        int index=-1;
        if(mList.isEmpty()||obj==null)
        {
            return index;
        }
        int size=mList.size();
        for(int i=0;i<size;i++)
        {
            WeakReference<E> reference=mList.get(i);
            E ref=reference.get();
            if(ref==obj)
            {
                index=i;
                break;
            }
        }
        return index;
    }
    
    public int lastIndexOf(Object obj)
    {
        removeDeadRef();
        int index=-1;
        if(mList.isEmpty()||obj==null)
        {
            return index;
        }
        int size=mList.size();
        for(int i=size-1;i>=0;i++)
        {
            WeakReference<E> reference=mList.get(i);
            E ref=reference.get();
            if(ref==obj)
            {
                index=i;
                break;
            }
        }
        return index;
    }
    
    @SuppressWarnings("unchecked")
    private void removeDeadRef()
    {
        Reference<Object> deadRef=null;
        while((deadRef=(Reference<Object>)mDeadRefQueue.poll())!=null)
        {
            mList.remove(deadRef);
        }
    }
}
