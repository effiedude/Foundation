package com.townspriter.android.foundation.utils.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/******************************************************************************
 * @Path Foundation:CollectionUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class IterableUtil
{
    public static <T> T getFirst(Iterable<? extends T> iterable,T defaultValue)
    {
        if(iterable==null)
        {
            return defaultValue;
        }
        return getNext(iterable.iterator(),defaultValue);
    }
    
    private static <T> T getNext(Iterator<? extends T> iterator,T defaultValue)
    {
        if(iterator==null||!iterator.hasNext())
        {
            return defaultValue;
        }
        return iterator.next();
    }
    
    public static <T> T getLast(Iterable<? extends T> iterable,T defaultValue)
    {
        if(iterable==null)
        {
            return defaultValue;
        }
        if(iterable instanceof Collection)
        {
            Collection<? extends T> collection=CollectionUtil.cast(iterable);
            if(collection.isEmpty())
            {
                return defaultValue;
            }
            else if(iterable instanceof List)
            {
                return getLastInNonemptyList(ListUtil.cast(iterable));
            }
        }
        return getLast(iterable.iterator(),defaultValue);
    }
    
    private static <T> T getLast(Iterator<? extends T> iterator,T defaultValue)
    {
        if(iterator==null||!iterator.hasNext())
        {
            return defaultValue;
        }
        return getLastInHasNextIterator(iterator);
    }
    
    private static <T> T getLastInHasNextIterator(Iterator<T> iterator)
    {
        while(true)
        {
            T current=iterator.next();
            if(!iterator.hasNext())
            {
                return current;
            }
        }
    }
    
    private static <T> T getLastInNonemptyList(List<T> list)
    {
        return list.get(list.size()-1);
    }
    
    /**
     * @param iterable
     * @param index
     * @param defaultValue
     * 无论什么原因导致拿不到值, 就返回defaultValue
     * @param <T>
     * @return
     */
    public static <T> T get(Iterable<? extends T> iterable,int index,T defaultValue)
    {
        if(iterable==null||index<0)
        {
            return defaultValue;
        }
        if(iterable instanceof List)
        {
            List<? extends T> list=ListUtil.cast(iterable);
            return (index<list.size())?list.get(index):defaultValue;
        }
        else
        {
            return get(iterable.iterator(),index,defaultValue);
        }
    }
    
    private static <T> T get(Iterator<? extends T> iterator,int index,T defaultValue)
    {
        if(index<0)
        {
            return defaultValue;
        }
        advance(iterator,index);
        return getNext(iterator,defaultValue);
    }
    
    /**
     * 在Iterator上调用next()
     * 调用numberToAdvance次或者直到hasNext返回false
     *
     * @param iterator
     * @param numberToAdvance
     * @return 调用next()的次数.-1表示参数不合法
     */
    public static int advance(Iterator<?> iterator,int numberToAdvance)
    {
        if(iterator==null||numberToAdvance<0)
        {
            return -1;
        }
        int i;
        for(i=0;i<numberToAdvance&&iterator.hasNext();i++)
        {
            iterator.next();
        }
        return i;
    }
    
    /**
     * @param iterable
     * @param predicate
     * @param defaultValue
     * 无论什么原因导致拿不到值.就返回此值
     * @param <T>
     * @return
     */
    public static <T> T find(Iterable<? extends T> iterable,Predicate<? super T> predicate,T defaultValue)
    {
        if(iterable==null)
        {
            return defaultValue;
        }
        return find(iterable.iterator(),predicate,defaultValue);
    }
    
    private static <T> T find(Iterator<? extends T> iterator,Predicate<? super T> predicate,T defaultValue)
    {
        if(iterator==null||predicate==null)
        {
            return defaultValue;
        }
        while(iterator.hasNext())
        {
            T t=iterator.next();
            if(predicate.evaluate(t))
            {
                return t;
            }
        }
        return defaultValue;
    }
    
    /**
     * @param removeFrom
     * @param predicate
     * @param <T>
     * @return 被删除的数量
     */
    public static <T> int removeIf(Iterable<T> removeFrom,Predicate<? super T> predicate)
    {
        if(removeFrom==null||predicate==null)
        {
            return 0;
        }
        return removeIf(removeFrom.iterator(),predicate);
    }
    
    private static <T> int removeIf(Iterator<T> removeFrom,Predicate<? super T> predicate)
    {
        int deleted=0;
        if(removeFrom!=null&&predicate!=null)
        {
            while(removeFrom.hasNext())
            {
                if(predicate.evaluate(removeFrom.next()))
                {
                    removeFrom.remove();
                    deleted++;
                }
            }
        }
        return deleted;
    }
}
