package com.townspriter.base.foundation.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/******************************************************************************
 * @path ListUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ListUtil
{
    public static <E> ArrayList<E> asArrayList(E...array)
    {
        if(array==null)
        {
            return new ArrayList<E>();
        }
        ArrayList<E> list=new ArrayList<>(computeArrayListCapacity(array.length));
        Collections.addAll(list,array);
        return list;
    }
    
    public static <T> ArrayList<T> asArrayList(Collection<? extends T> source)
    {
        if(CollectionUtil.isEmpty(source))
        {
            return new ArrayList<T>();
        }
        ArrayList<T> list=new ArrayList<>(computeArrayListCapacity(source.size()));
        list.addAll(source);
        return list;
    }
    
    public static <E> ArrayList<E> select(Collection<? extends E> inputCollection,Predicate<? super E> predicate)
    {
        return CollectionUtil.select(inputCollection,predicate,new ArrayList<E>(inputCollection.size()));
    }
    
    private static int computeArrayListCapacity(int arraySize)
    {
        int value=arraySize+5+arraySize/10;
        /** 随便算的一个值.略大于arraySize */
        if(value<0)
        {
            value=arraySize;
        }
        return value;
    }
    
    /**
     * 去掉数组中的空元素
     * 
     * @param list
     */
    public static void trim(List list)
    {
        if(list==null||list.size()<=0)
        {
            return;
        }
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i)==null)
            {
                list.remove(i);
                i=i-1;
            }
        }
    }
    
    static <T> List<T> cast(Iterable<T> iterable)
    {
        return (List<T>)iterable;
    }
}
