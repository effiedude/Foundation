package com.townspriter.base.foundation.utils.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import android.os.Looper;

/******************************************************************************
 * @path Services
 * @describe 服务管理器提供同步和异步两种方式获取服务实例组件可包含多个服务.每个服务通过工厂创建服务实例.启动初始化时需要注册所有服务类和对应的构建工厂
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Services
{
    /** 服务容器 */
    private final Map<Class<?>,Object> mCache=new ConcurrentHashMap<>();
    /** 服务构建工厂容器 */
    private final Map<Class<?>,IServiceFactory> mServiceFactoryCache=new ConcurrentHashMap<>();
    /** 默认的服务构建工厂 */
    private IServiceFactory mDefaultServiceFactory=null;
    
    private Services()
    {}
    
    private static Services getInstance()
    {
        return LazyHolder.sServices;
    }
    
    /**
     * 向服务容器中注册服务
     *
     * @param clazz
     * 服务接口类型
     * @param object
     * 服务具体实现对象
     * @param <T>
     * 服务泛型类
     */
    public static <T extends IService> void registerService(Class<T> clazz,T object)
    {
        synchronized(getInstance().mCache)
        {
            getInstance().mCache.put(clazz,object);
        }
    }
    
    /**
     * 向服务容器中注册服务工厂
     *
     * @param clazz
     * 服务接口类型
     * @param factory
     * 注册的不是目标服务本身.而是注册创建这个服务的构建工厂
     */
    public static void registerFactory(Class<?> clazz,IServiceFactory factory)
    {
        synchronized(getInstance().mServiceFactoryCache)
        {
            getInstance().mServiceFactoryCache.put(clazz,factory);
        }
    }
    
    /**
     * 向服务容器默认工厂中反注册服务
     *
     * @param clazz
     * 服务接口类型
     */
    public static void unregisterService(Class<?> clazz)
    {
        synchronized(getInstance().mCache)
        {
            Object service=getInstance().mCache.get(clazz);
            if(service instanceof IService)
            {
                ((IService)service).unbind();
            }
            getInstance().mCache.remove(clazz);
        }
    }
    
    public static void unregisterAll()
    {
        synchronized(getInstance().mCache)
        {
            for(Entry<Class<?>,Object> entry:getInstance().mCache.entrySet())
            {
                Object service=entry.getValue();
                if(service instanceof IService)
                {
                    ((IService)service).unbind();
                }
            }
            getInstance().mCache.clear();
        }
    }
    
    /**
     * 设置默认的服务构建工厂当缓存.注册构建工厂中找不到对应的服务.则从默认的构建工厂中获取
     *
     * @param factory
     * 默认构建工厂
     */
    public static void setServiceFactory(IServiceFactory factory)
    {
        getInstance().mDefaultServiceFactory=factory;
    }
    
    /**
     * 同步获取服务实例
     *
     * @param clazz
     * 服务类型
     * @param <T>
     * 服务类
     * @return 服务实例
     */
    public static <T> T get(Class<T> clazz)
    {
        T service=(T)getInstance().mCache.get(clazz);
        /** 如果已经找到服务直接退出向下的查找 */
        if(service!=null)
        {
            return service;
        }
        if(Thread.currentThread()==Looper.getMainLooper().getThread())
        {}
        /** 优先从注册的构建工厂缓存中获取构建工厂 */
        IServiceFactory factory=getInstance().mServiceFactoryCache.get(clazz);
        if(factory==null)
        {
            factory=getInstance().mDefaultServiceFactory;
        }
        if(factory==null)
        {
            return service;
        }
        /** 考虑多线程情况下线程安全创建.service单实例 */
        synchronized(getInstance().mCache)
        {
            service=(T)getInstance().mCache.get(clazz);
            if(service!=null)
            {
                return service;
            }
            service=factory.createService(clazz);
            if(service==null)
            {
                return service;
            }
            getInstance().mCache.put(clazz,service);
        }
        return service;
    }
    
    private static class LazyHolder
    {
        private static final Services sServices=new Services();
    }
    private static class ServiceThreadFactory implements ThreadFactory
    {
        private static final AtomicInteger poolNumber=new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber=new AtomicInteger(1);
        private final String namePrefix;
        
        ServiceThreadFactory()
        {
            SecurityManager securityManager=System.getSecurityManager();
            group=(securityManager!=null)?securityManager.getThreadGroup():Thread.currentThread().getThreadGroup();
            namePrefix="services-pool-"+poolNumber.getAndIncrement()+"-thread-";
        }
        
        /**
         * 创建线程
         *
         * @param runnable
         * @return 新创建的线程
         */
        @Override
        public Thread newThread(Runnable runnable)
        {
            Thread thread=new Thread(group,runnable,namePrefix+threadNumber.getAndIncrement(),0);
            if(thread.isDaemon())
            {
                thread.setDaemon(false);
            }
            if(thread.getPriority()!=Thread.NORM_PRIORITY)
            {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }
}
