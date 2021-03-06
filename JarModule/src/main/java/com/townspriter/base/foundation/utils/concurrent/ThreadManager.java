package com.townspriter.base.foundation.utils.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.townspriter.base.foundation.utils.device.CPUUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.os.HandlerEx;
import com.townspriter.base.foundation.utils.reflect.ReflectionHelper;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.Process;
import android.os.SystemClock;

/******************************************************************************
 * @path ThreadManager
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ThreadManager
{
    private static final String TAG="ThreadManager";
    private static HandlerThread sBackgroundThread;
    private static HandlerEx sBackgroundHandler;
    private static HandlerThread sWorkThread;
    private static HandlerEx sWorkHandler;
    private static HandlerThread sNormalThread;
    private static HandlerEx sNormalHandler;
    /** 后台线程优先级的线程 */
    public static final int THREADxBACKGROUND=0;
    /** 介于后台和默认优先级之间的线程 */
    public static final int THREADxWORK=1;
    /** 主线程 */
    public static final int THREADxUI=2;
    /** 和主线程同优先级的线程 */
    public static final int THREADxNORMAL=3;
    /** 最小线程数量.兼容旧逻辑最小线程数以防止死锁 */
    private static final int THREADxPOOLxSIZE=Math.max(CPUUtil.getCPUCoreCount()+2,5);
    private static final HashMap<Object,RunnableMap> mRunnableCache=new HashMap<Object,RunnableMap>();
    private static final long RUNNABLExTIMExOUTxTIME=30*1000;
    private static final int THREADxIDLExINTERNAL=1024;
    private static final List<LoopRunnable> mLoopRunnableList=new ArrayList<>();
    private static ExecutorService mThreadPool=new ThreadPoolExecutor(THREADxPOOLxSIZE,THREADxPOOLxSIZE,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),Executors.defaultThreadFactory());
    private static HandlerEx mMainThreadHandler;
    private static HandlerEx sMonitorHandler;
    private static boolean sDebugMode;
    
    public static void debugMode()
    {
        sDebugMode=true;
        enableMonitor();
    }
    
    private static void enableMonitor()
    {
        if(sMonitorHandler==null)
        {
            HandlerThread thread=new HandlerThread("MonitorThread",android.os.Process.THREAD_PRIORITY_BACKGROUND+android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
            thread.start();
            sMonitorHandler=new HandlerEx("MonitorThread",thread.getLooper());
        }
    }
    
    private ThreadManager()
    {}
    
    /**
     * 将Runnable放入线程池执行
     * 默认为后台优先级执行.如果需要调优先级请使用execute(runnable,callback,priority)调用
     *
     * @param runnable
     */
    public static void execute(Runnable runnable)
    {
        execute(runnable,null,android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }
    
    /**
     * 将Runnable放入线程池执行
     * 默认为后台优先级执行.如果需要调优先级请使用execute(runnable,callback,priority)调用
     *
     * @param runnable
     * @param callback
     * 回调到execute函数所运行的线程中
     */
    public static void execute(Runnable runnable,Runnable callback)
    {
        execute(runnable,callback,android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }
    
    /**
     * 将Runnable放入线程池执行
     *
     * @param runnable
     * @param callback
     * 回调到execute函数所运行的线程中
     * @param priority
     * android.os.Process中指定的线程优先级
     */
    public static void execute(final Runnable runnable,final Runnable callback,final int priority)
    {
        try
        {
            if(!mThreadPool.isShutdown())
            {
                HandlerEx handler=null;
                if(callback!=null)
                {
                    handler=new HandlerEx("ThreadPool",Looper.myLooper());
                }
                final HandlerEx finalHandler=handler;
                mThreadPool.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        android.os.Process.setThreadPriority(priority);
                        try
                        {
                            runnable.run();
                            if(finalHandler!=null&&callback!=null)
                            {
                                finalHandler.post(callback);
                            }
                        }
                        catch(final Throwable throwable)
                        {
                            if(sDebugMode)
                            {
                                if(mMainThreadHandler==null)
                                {
                                    createMainThread();
                                }
                                mMainThreadHandler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        throw new RuntimeException(Logger.getStackTraceString(throwable),throwable);
                                    }
                                });
                            }
                        }
                        finally
                        {
                            if(priority!=Process.THREAD_PRIORITY_BACKGROUND)
                            {
                                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            }
                        }
                    }
                });
            }
        }
        catch(final Exception exception)
        {
            if(sDebugMode)
            {
                if(mMainThreadHandler==null)
                {
                    createMainThread();
                }
                mMainThreadHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        throw new RuntimeException(Logger.getStackTraceString(exception),exception);
                    }
                });
            }
        }
    }
    
    public static void post(int threadType,final Runnable preCallback,final Runnable task,final Runnable postCallback,final boolean callbackToMainThread,long delayMillis)
    {
        if(task==null)
        {
            return;
        }
        if(mMainThreadHandler==null)
        {
            createMainThread();
        }
        Handler handler;
        switch(threadType)
        {
            case THREADxBACKGROUND:
                if(sBackgroundThread==null)
                {
                    createBackgroundThread();
                }
                handler=sBackgroundHandler;
                break;
            case THREADxWORK:
                if(sWorkThread==null)
                {
                    createWorkerThread();
                }
                handler=sWorkHandler;
                break;
            case THREADxUI:
                handler=mMainThreadHandler;
                break;
            case THREADxNORMAL:
                if(sNormalThread==null)
                {
                    createNormalThread();
                }
                handler=sNormalHandler;
                break;
            default:
                handler=mMainThreadHandler;
                break;
        }
        if(handler==null)
        {
            return;
        }
        final Handler finalHandler=handler;
        Looper myLooper=null;
        if(callbackToMainThread==false)
        {
            myLooper=Looper.myLooper();
            if(myLooper==null)
            {
                myLooper=mMainThreadHandler.getLooper();
            }
        }
        final Looper looper=myLooper;
        final Runnable postRunnable=new Runnable()
        {
            @Override
            public void run()
            {
                Runnable monitorRunnable=null;
                if(sMonitorHandler!=null)
                {
                    monitorRunnable=new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mMainThreadHandler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if(Debug.isDebuggerConnected())
                                    {
                                        return;
                                    }
                                    RuntimeException runtimeException=new RuntimeException("这里使用了ThreadManager.post函数运行了一个超过30秒的任务."+"请查看这个任务是否是非常耗时的任务或者存在死循环或者存在死锁或者存在一直卡住线程的情况."+"如果存在上述情况请解决或者使用ThreadManager.execute函数放入线程池执行该任务.",new Throwable(task.toString()));
                                    throw runtimeException;
                                }
                            });
                        }
                    };
                }
                if(sMonitorHandler!=null)
                {
                    sMonitorHandler.postDelayed(monitorRunnable,RUNNABLExTIMExOUTxTIME);
                }
                synchronized(mRunnableCache)
                {
                    mRunnableCache.remove(task);
                }
                try
                {
                    task.run();
                }
                catch(final Throwable t)
                {
                    if(sDebugMode)
                    {
                        mMainThreadHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                throw new RuntimeException(Logger.getStackTraceString(t),t);
                            }
                        });
                    }
                }
                if(sMonitorHandler!=null)
                {
                    sMonitorHandler.removeCallbacks(monitorRunnable);
                }
                if(postCallback!=null)
                {
                    if(callbackToMainThread||(looper==mMainThreadHandler.getLooper()))
                    {
                        mMainThreadHandler.post(postCallback);
                    }
                    else
                    {
                        new Handler(looper).post(postCallback);
                    }
                }
            }
        };
        Runnable realRunnable=new Runnable()
        {
            @Override
            public void run()
            {
                if(preCallback!=null)
                {
                    if(callbackToMainThread||(looper==mMainThreadHandler.getLooper()))
                    {
                        mMainThreadHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                preCallback.run();
                                finalHandler.post(postRunnable);
                            }
                        });
                    }
                    else
                    {
                        new Handler(looper).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                preCallback.run();
                                finalHandler.post(postRunnable);
                            }
                        });
                    }
                }
                else
                {
                    postRunnable.run();
                }
            }
        };
        synchronized(mRunnableCache)
        {
            mRunnableCache.put(task,new RunnableMap(realRunnable,threadType));
        }
        finalHandler.postDelayed(realRunnable,delayMillis);
    }
    
    public static void post(int threadType,Runnable preCallback,Runnable task,Runnable postCallback,boolean callbackToMainThread)
    {
        post(threadType,preCallback,task,postCallback,callbackToMainThread,0);
    }
    
    public static void post(int threadType,Runnable task,Runnable postCallbackRunnable)
    {
        post(threadType,null,task,postCallbackRunnable,false,0);
    }
    
    public static void post(int threadType,Runnable preCallback,Runnable task,Runnable postCallback)
    {
        post(threadType,preCallback,task,postCallback,false,0);
    }
    
    /**
     * @note 谨慎使用:POST到消息队列.顺序执行事件.如需要实时性强的操作请注意.可考虑使用本类的execute()或其他
     * @param threadType
     * {@link #THREADxBACKGROUND}
     * {@link #THREADxWORK}
     * {@link #THREADxUI}
     * {@link #THREADxNORMAL}
     * @param task
     */
    public static void post(int threadType,Runnable task)
    {
        post(threadType,null,task,null,false,0);
    }
    
    /**
     * @note 谨慎使用:POST到消息队列.顺序执行事件.如需要实时性强的操作请注意.可考虑使用本类的execute()或其他
     * @param threadType
     * {@link #THREADxBACKGROUND}
     * {@link #THREADxWORK}
     * {@link #THREADxUI}
     * {@link #THREADxNORMAL}
     * @param task
     */
    public static void postDelayed(int threadType,Runnable task,long delayMillis)
    {
        post(threadType,null,task,null,false,delayMillis);
    }
    
    public static void loop(int threadType,final Runnable task,long delayMillis,long interval)
    {
        synchronized(mLoopRunnableList)
        {
            LoopRunnable loopRunnable=new LoopRunnable(threadType,interval,task)
            {
                @Override
                public void run()
                {
                    task.run();
                    postNextRun();
                }
            };
            postDelayed(threadType,loopRunnable,delayMillis);
            mLoopRunnableList.add(loopRunnable);
        }
    }
    
    public static void stopLoop(Runnable task)
    {
        synchronized(mLoopRunnableList)
        {
            LoopRunnable targetLoopRunnable=null;
            for(LoopRunnable loopRunnable:mLoopRunnableList)
            {
                if(loopRunnable.mTask==task)
                {
                    targetLoopRunnable=loopRunnable;
                    break;
                }
            }
            if(null!=targetLoopRunnable)
            {
                removeRunnable(targetLoopRunnable);
            }
        }
    }
    
    /**
     * 可以直接移除所有使用ThreadManager的post出去的task.不用指定是线程类型ThreadType
     */
    public static void removeRunnable(Runnable task)
    {
        if(task==null)
        {
            return;
        }
        RunnableMap map=mRunnableCache.get(task);
        if(map==null)
        {
            return;
        }
        Runnable realRunnable=map.getRunnable();
        if(realRunnable!=null)
        {
            switch(map.getType())
            {
                case THREADxBACKGROUND:
                    if(sBackgroundHandler!=null)
                    {
                        sBackgroundHandler.removeCallbacks(realRunnable);
                    }
                    break;
                case THREADxWORK:
                    if(sWorkHandler!=null)
                    {
                        sWorkHandler.removeCallbacks(realRunnable);
                    }
                    break;
                case THREADxUI:
                    if(mMainThreadHandler!=null)
                    {
                        mMainThreadHandler.removeCallbacks(realRunnable);
                    }
                    break;
                case THREADxNORMAL:
                    if(sNormalHandler!=null)
                    {
                        sNormalHandler.removeCallbacks(realRunnable);
                    }
                    break;
                case THREADxIDLExINTERNAL:
                    realRunnable.run(); // 特殊处理.使IDLERunnable支持取消
                    break;
                default:
                    break;
            }
            synchronized(mRunnableCache)
            {
                mRunnableCache.remove(task);
            }
        }
    }
    
    private static synchronized void createBackgroundThread()
    {
        if(sBackgroundThread==null)
        {
            sBackgroundThread=new HandlerThread("BackgroundHandler",android.os.Process.THREAD_PRIORITY_BACKGROUND);
            sBackgroundThread.start();
            sBackgroundHandler=new HandlerEx("BackgroundHandler",sBackgroundThread.getLooper());
        }
    }
    
    private static synchronized void createWorkerThread()
    {
        if(sWorkThread==null)
        {
            sWorkThread=new HandlerThread("WorkHandler",(android.os.Process.THREAD_PRIORITY_DEFAULT+android.os.Process.THREAD_PRIORITY_BACKGROUND)/2);
            sWorkThread.start();
            sWorkHandler=new HandlerEx("WorkHandler",sWorkThread.getLooper());
        }
    }
    
    private static synchronized void createNormalThread()
    {
        if(sNormalThread==null)
        {
            sNormalThread=new HandlerThread("sNormalHandler",android.os.Process.THREAD_PRIORITY_DEFAULT);
            sNormalThread.start();
            sNormalHandler=new HandlerEx("sNormalHandler",sNormalThread.getLooper());
        }
    }
    
    private static synchronized void createMainThread()
    {
        if(mMainThreadHandler==null)
        {
            mMainThreadHandler=new HandlerEx("BackgroundHandler.MainThreadHandler-38",Looper.getMainLooper());
        }
    }
    
    public static void doSomeThingBeforDestroy()
    {
        if(sBackgroundThread!=null)
        {
            sBackgroundThread.setPriority(Thread.MAX_PRIORITY);
        }
        if(sWorkThread!=null)
        {
            sWorkThread.setPriority(Thread.MAX_PRIORITY);
        }
    }
    
    public static synchronized void destroy()
    {
        if(sBackgroundThread!=null)
        {
            sBackgroundThread.quit();
            try
            {
                sBackgroundThread.interrupt();
            }
            catch(Throwable throwable)
            {
                Logger.e(TAG,"destroy:Throwable",throwable);
            }
            sBackgroundThread=null;
        }
        if(sWorkThread!=null)
        {
            sWorkThread.quit();
            try
            {
                sWorkThread.interrupt();
            }
            catch(Throwable throwable)
            {
                Logger.e(TAG,"destroy:Throwable",throwable);
            }
            sWorkThread=null;
        }
        if(sNormalThread!=null)
        {
            sNormalThread.quit();
            try
            {
                sNormalThread.interrupt();
            }
            catch(Throwable throwable)
            {
                Logger.e(TAG,"destroy:Throwable",throwable);
            }
            sNormalThread=null;
        }
        if(mThreadPool!=null)
        {
            try
            {
                mThreadPool.shutdown();
            }
            catch(Throwable throwable)
            {
                Logger.e(TAG,"destroy:Throwable",throwable);
            }
            mThreadPool=null;
        }
    }
    
    public static Looper getBackgroundLooper()
    {
        createBackgroundThread();
        return sBackgroundThread.getLooper();
    }
    
    public static Looper getWorkLooper()
    {
        createWorkerThread();
        return sWorkThread.getLooper();
    }
    
    private static class RunnableMap
    {
        private final Runnable mRunnable;
        private final Integer mType;

        public RunnableMap(Runnable runnable,Integer type)
        {
            mRunnable=runnable;
            mType=type;
        }

        public Runnable getRunnable()
        {
            return mRunnable;
        }

        public int getType()
        {
            return mType;
        }
    }
    private static class CustomIdleHandler implements IdleHandler
    {
        private static final MessageQueue mMainThreadQueue=(MessageQueue)ReflectionHelper.getFieldValue(Looper.getMainLooper(),"mQueue");
        private static final Handler mHandler=new HandlerEx("IdleHandler",Looper.getMainLooper());
        private static final long mRunnableDelayTime=10*1000;
        private final Runnable mRunnable;
        private static long mLastExcuteTime;
        private static final long TIME_VALUE=500;

        public CustomIdleHandler(Runnable runnable)
        {
            mRunnable=runnable;
        }

        private final Runnable mRemoveRunnable=new Runnable()
        {
            @Override
            public void run()
            {
                if(mMainThreadQueue!=null)
                {
                    mMainThreadQueue.removeIdleHandler(CustomIdleHandler.this);
                }
                mHandler.removeCallbacks(mPostRunnable);
            }
        };
        private final Runnable mPostRunnable=new Runnable()
        {
            @Override
            public void run()
            {
                if(mMainThreadQueue!=null)
                {
                    mMainThreadQueue.removeIdleHandler(CustomIdleHandler.this);
                }
                synchronized(mRunnableCache)
                {
                    mRunnableCache.remove(mRunnable);
                }
                mRunnable.run();
                mLastExcuteTime=SystemClock.elapsedRealtime();
            }
        };

        @Override
        public boolean queueIdle()
        {
            mHandler.removeCallbacks(mPostRunnable);
            synchronized(mRunnableCache)
            {
                mRunnableCache.remove(mRunnable);
            }
            if(SystemClock.elapsedRealtime()-mLastExcuteTime<TIME_VALUE)
            {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        post();
                    }
                });
            }
            else
            {
                mRunnable.run();
                mLastExcuteTime=SystemClock.elapsedRealtime();
            }
            return false;
        }

        public void post()
        {
            if(mMainThreadQueue!=null)
            {
                synchronized(mRunnableCache)
                {
                    mRunnableCache.put(mRunnable,new RunnableMap(mRemoveRunnable,THREADxIDLExINTERNAL));
                }
                mHandler.postDelayed(mPostRunnable,mRunnableDelayTime);
                mMainThreadQueue.addIdleHandler(this);
            }
            else
            {
                throw new Error("");
            }
        }
    }

    /**
     * 向主线程发送一个闲时处理的Runnable.这个Runnable会在主线程空闲的时候进行处理.也就是主线程没有消息要处理的时候进行处理
     *
     * @param runnable
     */
    public static void postIdleRunnable(Runnable runnable)
    {
        new CustomIdleHandler(runnable).post();
    }
    
    public static boolean isMainThread()
    {
        return Looper.myLooper()==Looper.getMainLooper();
    }
    
    public abstract static class RunnableEx implements Runnable
    {
        private Object mArg;

        public void setArg(Object arg)
        {
            mArg=arg;
        }

        public Object getArg()
        {
            return mArg;
        }
    }
    public abstract static class LoopRunnable extends RunnableEx
    {
        private final int mThreadType;
        private final long mInterval;
        private final Runnable mTask;

        public LoopRunnable(int threadType,long interval,Runnable task)
        {
            mThreadType=threadType;
            mInterval=interval;
            mTask=task;
        }

        protected void postNextRun()
        {
            postDelayed(mThreadType,this,mInterval);
        }
    }

    public static boolean fakeMainLooper(boolean reset)
    {
        if(isMainThread())
        {
            return true;
        }
        ThreadLocal<Looper> threadLocal=(ThreadLocal<Looper>)ReflectionHelper.getStaticFieldValue(Looper.class,"sThreadLocal");
        if(threadLocal==null)
        {
            return false;
        }
        Looper looper=null;
        if(!reset)
        {
            looper=Looper.getMainLooper();
        }
        ReflectionHelper.invokeMethod(threadLocal,"set",new Class[]{Object.class},new Object[]{looper});
        return true;
    }
    
    public static long fakeThreadId(long fakeId)
    {
        Thread t=Thread.currentThread();
        long id=t.getId();
        if(fakeId!=id)
        {
            if(VERSION.SDK_INT<=VERSION_CODES.M)
            {
                ReflectionHelper.setFieldValue(t,"id",fakeId);
            }
            else
            {
                ReflectionHelper.setFieldValue(t,"tid",fakeId);
            }
        }
        return id;
    }
    
    public static boolean prepareLooperWithMainThreadQueue(boolean reset)
    {
        if(isMainThread())
        {
            return true;
        }
        ThreadLocal<Looper> threadLocal=(ThreadLocal<Looper>)ReflectionHelper.getStaticFieldValue(Looper.class,"sThreadLocal");
        if(threadLocal==null)
        {
            return false;
        }
        Looper looper=null;
        if(!reset)
        {
            Looper.prepare();
            looper=Looper.myLooper();
            Object queue=ReflectionHelper.invokeMethod(Looper.getMainLooper(),"getQueue",new Class[0],new Object[0]);
            if(!(queue instanceof MessageQueue))
            {
                return false;
            }
            ReflectionHelper.setFieldValue(looper,"mQueue",queue);
        }
        ReflectionHelper.invokeMethod(threadLocal,"set",new Class[]{Object.class},new Object[]{looper});
        return true;
    }
    
    public static void runOnUiThread(Runnable runnable)
    {
        if(isMainThread())
        {
            runnable.run();
        }
        else
        {
            post(THREADxUI,runnable);
        }
    }
}
