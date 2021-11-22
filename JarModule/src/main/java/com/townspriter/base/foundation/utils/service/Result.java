package com.townspriter.base.foundation.utils.service;
/******************************************************************************
 * @path Foundation:Result
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class Result<T>
{
    private final T mResult;
    private final Throwable mException;
    
    /**
     * 封装正常处理结果的构造函数
     *
     * @param result
     * 处理结果
     */
    private Result(T result)
    {
        mException=null;
        mResult=result;
    }
    
    /**
     * 封装处理过程中异常的构造函数
     *
     * @param exception
     */
    private Result(Throwable exception)
    {
        mException=exception;
        mResult=null;
    }
    
    /**
     * 封装正常处理结果
     *
     * @param result
     * 处理结果
     * @param <T>
     * 处理结果类型
     * @return 包含正常结果的对象
     */
    public static <T> Result<T> of(T result)
    {
        return new Result<T>(result);
    }
    
    /**
     * 封装处理过程中的exception
     *
     * @param exception
     * 处理过程中的异常对象
     * @param <T>
     * 处理结果类型
     * @return 包含异常的对象
     */
    public static <T> Result<T> of(Throwable exception)
    {
        return new Result<>(exception);
    }
    
    /**
     * 获取处理结果或继续抛出异步处理过程中的异常
     * 
     * @return T 处理结果
     * @exception Throwable
     * 异步处理过程中的异常对象
     */
    public T get() throws Throwable
    {
        if(mException!=null)
        {
            throw mException;
        }
        if(mResult==null)
        {
            throw new Throwable("空内容");
        }
        return mResult;
    }
}