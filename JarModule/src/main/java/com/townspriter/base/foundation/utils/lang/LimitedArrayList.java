package com.townspriter.base.foundation.utils.lang;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/******************************************************************************
 * @path Foundation:LimitedArrayList
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class LimitedArrayList<E>extends AbstractList<E>
{
    private final int mMaxSize;
    private final transient Object[] elementData;
    private int mSize;
    
    public LimitedArrayList(@IntRange(from=1) int maxSize)
    {
        if(maxSize<=0)
        {
            throw new IllegalArgumentException("非法容量值:"+maxSize);
        }
        mMaxSize=maxSize;
        elementData=new Object[maxSize];
    }
    
    private static <T> void requireNonNull(T object)
    {
        if(object==null)
        {
            throw new NullPointerException();
        }
    }
    
    @Override
    public boolean addAll(Collection<? extends E> collection)
    {
        Object[] objects=collection.toArray();
        int numNew=objects.length;
        int space=mMaxSize-mSize;
        int canAddNum=Math.min(numNew,space);
        if(canAddNum>0)
        {
            System.arraycopy(objects,0,elementData,mSize,canAddNum);
            mSize+=canAddNum;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addAll(int index,@NonNull Collection<? extends E> collection)
    {
        if(index>mSize||index<0)
        {
            throw new IndexOutOfBoundsException("索引值越界");
        }
        Object[] objects=collection.toArray();
        int numNew=objects.length;
        int moveIndex=Math.min(index+numNew,mMaxSize);
        int canInsertNum=moveIndex-index;
        int numMoved=Math.min(mSize-index,mMaxSize-moveIndex);
        /** 插入新元素后还有坑位.则进行剩余坑位的拷贝 */
        if(numMoved>0)
        {
            System.arraycopy(elementData,index,elementData,index+numNew,numMoved);
        }
        /** 插入可插入数量的元素 */
        System.arraycopy(objects,0,elementData,index,canInsertNum);
        mSize+=canInsertNum+numMoved-mSize;
        return canInsertNum!=0;
    }
    
    @Override
    public boolean add(E element)
    {
        if(mSize<mMaxSize)
        {
            elementData[mSize++]=element;
            return true;
        }
        return false;
    }
    
    @Override
    public void add(int index,E element)
    {
        if(index>mSize||index<0)
        {
            throw new IndexOutOfBoundsException("索引值越界");
        }
        if(mSize==mMaxSize)
        {
            mSize--;
        }
        System.arraycopy(elementData,index,elementData,index+1,mSize-index);
        elementData[index]=element;
        mSize++;
    }
    
    @Override
    public boolean removeAll(Collection<?> collection)
    {
        requireNonNull(collection);
        return batchRemove(collection,false);
    }
    
    private boolean batchRemove(Collection<?> collection,boolean complement)
    {
        Object[] elementData=this.elementData;
        int r=0,w=0;
        boolean modified=false;
        try
        {
            for(;r<mSize;r++)
            {
                if(collection.contains(elementData[r])==complement)
                {
                    elementData[w++]=elementData[r];
                }
            }
        }
        finally
        {
            if(r!=mSize)
            {
                System.arraycopy(elementData,r,elementData,w,mSize-r);
                w+=mSize-r;
            }
            if(w!=mSize)
            {
                for(int i=w;i<mSize;i++)
                {
                    elementData[i]=null;
                }
                modCount+=mSize-w;
                mSize=w;
                modified=true;
            }
        }
        return modified;
    }
    
    @Override
    public boolean retainAll(Collection<?> collection)
    {
        requireNonNull(collection);
        return batchRemove(collection,true);
    }
    
    @Override
    public void clear()
    {
        for(int i=0;i<mSize;i++)
        {
            elementData[i]=null;
        }
        mSize=0;
    }
    
    @Override
    public int size()
    {
        return mSize;
    }
    
    @Override
    public boolean isEmpty()
    {
        return mSize==0;
    }
    
    @Override
    public boolean contains(Object object)
    {
        return indexOf(object)>=0;
    }
    
    @NonNull
    @Override
    public Object[] toArray()
    {
        return Arrays.copyOf(elementData,mSize);
    }
    
    @SuppressWarnings({"unchecked","SuspiciousSystemArraycopy"})
    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] array)
    {
        if(array.length<mSize)
        {
            return (T[])Arrays.copyOf(elementData,mSize,array.getClass());
        }
        System.arraycopy(elementData,0,array,0,mSize);
        if(array.length>mSize)
        {
            array[mSize]=null;
        }
        return array;
    }
    
    @Override
    public boolean remove(Object object)
    {
        if(object==null)
        {
            for(int index=0;index<mSize;index++)
            {
                if(elementData[index]==null)
                {
                    fastRemove(index);
                    return true;
                }
            }
        }
        else
        {
            for(int index=0;index<mSize;index++)
            {
                if(object.equals(elementData[index]))
                {
                    fastRemove(index);
                    return true;
                }
            }
        }
        return false;
    }
    
    private void fastRemove(int index)
    {
        int numMoved=mSize-index-1;
        if(numMoved>0)
        {
            System.arraycopy(elementData,index+1,elementData,index,numMoved);
        }
        elementData[--mSize]=null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index)
    {
        if(index>=mSize)
        {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
        modCount++;
        E oldValue=(E)elementData[index];
        int numMoved=mSize-index-1;
        if(numMoved>0)
        {
            System.arraycopy(elementData,index+1,elementData,index,numMoved);
        }
        elementData[--mSize]=null;
        return oldValue;
    }
    
    private String outOfBoundsMsg(int index)
    {
        return "outOfBoundsMsg-index:"+index+"\noutOfBoundsMsg-size:"+mSize;
    }
    
    @Override
    public int indexOf(Object object)
    {
        if(object==null)
        {
            for(int i=0;i<mSize;i++)
            {
                if(elementData[i]==null)
                {
                    return i;
                }
            }
        }
        else
        {
            for(int i=0;i<mSize;i++)
            {
                if(object.equals(elementData[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(Object object)
    {
        if(object==null)
        {
            for(int i=mSize-1;i>=0;i--)
            {
                if(elementData[i]==null)
                {
                    return i;
                }
            }
        }
        else
        {
            for(int i=mSize-1;i>=0;i--)
            {
                if(object.equals(elementData[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index)
    {
        if(index>=mSize)
        {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
        return (E)elementData[index];
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index,E element)
    {
        if(index>=mSize)
        {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
        E oldValue=(E)elementData[index];
        elementData[index]=element;
        return oldValue;
    }
}
