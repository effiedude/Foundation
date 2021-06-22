package com.townspriter.android.foundation.utils.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.townspriter.android.foundation.utils.collection.Predicate;

/******************************************************************************
 * @Path Foundation:ArrayUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class ArrayUtil
{
    public static final int INDEXxNOTxFOUND=-1;
    
    public static <T> int indexOf(T[] array,T objectToFind)
    {
        return indexOf(array,objectToFind,0);
    }
    
    public static <T> int indexOf(T[] array,T objectToFind,int startIndex)
    {
        if(array==null)
        {
            return INDEXxNOTxFOUND;
        }
        if(startIndex<0)
        {
            startIndex=0;
        }
        if(objectToFind==null)
        {
            for(int i=startIndex;i<array.length;i++)
            {
                if(array[i]==null)
                {
                    return i;
                }
            }
        }
        else
        {
            for(int i=startIndex;i<array.length;i++)
            {
                if(objectToFind.equals(array[i]))
                {
                    return i;
                }
            }
        }
        return INDEXxNOTxFOUND;
    }
    
    public static <T> boolean contains(T[] array,T objectToFind)
    {
        return indexOf(array,objectToFind)!=INDEXxNOTxFOUND;
    }
    
    public static boolean isEmpty(byte[] array)
    {
        return getLength(array)==0;
    }
    
    public static boolean isEmpty(long[] array)
    {
        return getLength(array)==0;
    }
    
    public static boolean isEmpty(int[] array)
    {
        return getLength(array)==0;
    }
    
    public static <T> boolean isEmpty(T[] array)
    {
        return getLength(array)==0;
    }
    
    private static int getLength(Object array)
    {
        if(array==null)
        {
            return 0;
        }
        return Array.getLength(array);
    }
    
    public static byte[] add(byte[] array,byte element)
    {
        byte[] newArray=(byte[])copyArrayGroup(array,Byte.TYPE);
        newArray[newArray.length-1]=element;
        return newArray;
    }
    
    private static Object copyArrayGroup(Object array,Class<?> newArrayComponentType)
    {
        if(array!=null)
        {
            int arrayLength=Array.getLength(array);
            Object newArray=Array.newInstance(array.getClass().getComponentType(),arrayLength+1);
            System.arraycopy(array,0,newArray,0,arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType,1);
    }
    
    public static byte[] addAll(byte[] arrayStart,byte...arrayOthers)
    {
        if(arrayStart==null)
        {
            return clone(arrayOthers);
        }
        else if(arrayOthers==null)
        {
            return clone(arrayStart);
        }
        byte[] joinedArray=new byte[arrayStart.length+arrayOthers.length];
        System.arraycopy(arrayStart,0,joinedArray,0,arrayStart.length);
        System.arraycopy(arrayOthers,0,joinedArray,arrayStart.length,arrayOthers.length);
        return joinedArray;
    }
    
    public static byte[] clone(byte[] array)
    {
        if(array==null)
        {
            return null;
        }
        return array.clone();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] findAllIf(T[] container,Predicate<T> pred)
    {
        List<T> list=new ArrayList<T>();
        for(T item:container)
        {
            if(pred.evaluate(item))
            {
                list.add(item);
            }
        }
        return (T[])list.toArray();
    }
}
