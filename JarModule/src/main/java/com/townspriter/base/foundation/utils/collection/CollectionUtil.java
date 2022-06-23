package com.townspriter.base.foundation.utils.collection;

import java.util.Collection;
import java.util.Map;

/******************************************************************************
 * @path CollectionUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class CollectionUtil
{
    public static boolean isEmpty(Collection<?> container)
    {
        return container==null||container.isEmpty();
    }
    
    public static boolean isEmpty(Map<?,?> container)
    {
        return container==null||container.isEmpty();
    }
    
    /**
     * @param inputCollection
     * @param predicate
     * @param outputCollection
     * 当inputCollection和predicate都不为空时不能传空
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R extends Collection<? super T>> R select(Collection<? extends T> inputCollection,Predicate<? super T> predicate,R outputCollection)
    {
        if(inputCollection!=null&&predicate!=null)
        {
            for(T item:inputCollection)
            {
                if(predicate.evaluate(item))
                {
                    outputCollection.add(item);
                }
            }
        }
        return outputCollection;
    }
    
    static <T> Collection<T> cast(Iterable<T> iterable)
    {
        return (Collection<T>)iterable;
    }
}
