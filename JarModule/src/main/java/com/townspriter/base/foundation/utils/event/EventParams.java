package com.townspriter.base.foundation.utils.event;

import com.townspriter.base.foundation.utils.collection.LinkedPool;
import com.townspriter.base.foundation.utils.collection.LinkedPool.ILinkedPoolable;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import android.util.SparseArray;

/******************************************************************************
 * @path Foundation:EventParams
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class EventParams implements ILinkedPoolable
{
    private static final int MAXxRECYCLED=16;
    private static final ThreadLocal<LinkedPool<EventParams>> POOLxTHREADxLOCAL=new ThreadLocal<>();
    private static final LinkedPool.InstanceCreator<EventParams> CREATOR=new LinkedPool.InstanceCreator<EventParams>()
    {
        @Override
        public EventParams createInstance()
        {
            return new EventParams();
        }
    };
    private final SparseArray<Object> mMap=new SparseArray<>();
    private Object mNext=null;
    
    private EventParams()
    {}
    
    private static LinkedPool<EventParams> getPool()
    {
        if(POOLxTHREADxLOCAL.get()==null)
        {
            POOLxTHREADxLOCAL.set(new LinkedPool<>(CREATOR,MAXxRECYCLED));
        }
        return POOLxTHREADxLOCAL.get();
    }
    
    public static Object get(EventParams params,int key,Class<?> checkType,Object defaultValue)
    {
        Object obj=get(params,key,defaultValue);
        if(obj!=null&&!checkType.isInstance(obj))
        {
            return defaultValue;
        }
        return obj;
    }
    
    public static Object get(EventParams params,int key,Object defaultValue)
    {
        if(params==null||!params.containsKey(key))
        {
            return defaultValue;
        }
        return params.get(key);
    }
    
    public static EventParams put(EventParams params,int key,Object value)
    {
        if(params!=null)
        {
            params.put(key,value);
        }
        return params;
    }
    
    public static boolean contains(EventParams params,int key)
    {
        return params!=null&&params.containsKey(key);
    }
    
    public static void recycle(EventParams params)
    {
        if(params!=null)
        {
            params.recycle();
        }
    }
    
    public static EventParams obtain()
    {
        return getPool().obtain();
    }
    
    public static EventParams obtain(EventParams src)
    {
        EventParams params=obtain();
        params.merge(src);
        return params;
    }
    
    public final void recycle()
    {
        mMap.clear();
        getPool().recycle(this);
    }
    
    private void ensureNotRecycled()
    {
        getPool().ensureNotRecycled(this);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(int key)
    {
        ensureNotRecycled();
        return (T)mMap.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(int key,T defaultValue)
    {
        Object value=get(key);
        if(value==null)
        {
            return defaultValue;
        }
        try
        {
            return (T)value;
        }
        catch(ClassCastException classCastException)
        {
            classCastException.printStackTrace();
            return defaultValue;
        }
    }
    
    public EventParams copyValue(int srcKey,int dstKey)
    {
        ensureNotRecycled();
        int index=mMap.indexOfKey(srcKey);
        if(index<0)
        {
            throw new RuntimeException("");
        }
        put(dstKey,mMap.valueAt(index));
        return this;
    }
    
    public boolean containsKey(int key)
    {
        ensureNotRecycled();
        return mMap.indexOfKey(key)>=0;
    }
    
    public EventParams put(int key,Object value)
    {
        ensureNotRecycled();
        mMap.put(key,value);
        return this;
    }
    
    public void remove(int key)
    {
        ensureNotRecycled();
        mMap.remove(key);
    }
    
    public void clear()
    {
        ensureNotRecycled();
        mMap.clear();
    }
    
    public EventParams merge(EventParams src)
    {
        ensureNotRecycled();
        if(src!=null)
        {
            final SparseArray<Object> srcMap=src.mMap;
            for(int i=0,size=srcMap.size();i<size;++i)
            {
                mMap.put(srcMap.keyAt(i),srcMap.valueAt(i));
            }
        }
        return this;
    }
    
    @Override
    public Object getNext()
    {
        return mNext;
    }
    
    @Override
    public void setNext(Object nextNode)
    {
        mNext=nextNode;
    }
    
    public EventParams send(int actionId,IObserver observer)
    {
        AssertUtil.mustNotNull(observer);
        observer.handleEvent(actionId,this,null);
        return this;
    }
}