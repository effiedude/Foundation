package com.townspriter.android.foundation.utils.lang;

import com.townspriter.android.foundation.utils.text.StringUtil;
import android.os.Looper;

/******************************************************************************
 * @Path Foundation:AssertUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class AssertUtil
{
    private static IAssert sAssertImpl;
    static
    {
        setAssertImpl(new AssertionErrorAssertImpl());
    }
    
    public static void setAssertImpl(IAssert assertImpl)
    {
        sAssertImpl=assertImpl;
    }
    
    public static final void mustOk(boolean aOk)
    {
        mustOk(aOk,null);
    }
    
    public static final void mustOk(boolean aOk,Object aErrMsg)
    {
        if(!aOk)
        {
            if(null!=aErrMsg)
            {
                assertDie(aErrMsg.toString());
            }
            else
            {
                assertDie();
            }
        }
    }
    
    public static void mustNotNull(Object aObj)
    {
        mustNotNull(aObj,null);
    }
    
    public static void mustNotNull(Object aObj,String aErrMsg)
    {
        mustOk(null!=aObj,aErrMsg);
    }
    
    public static void fail(String aMsg)
    {
        mustOk(false,aMsg);
    }
    
    public static void fail(Throwable throwable)
    {
        mustOk(false,throwable.getMessage());
    }
    
    public static void fail()
    {
        fail("");
    }
    
    public static void mustInNonUiThread()
    {
        mustInNonUiThread(null);
    }
    
    public static void mustInUiThread()
    {
        mustInUiThread(null);
    }
    
    public static void mustInNonUiThread(String aErrMsg)
    {
        mustOk(!isMainThread(),aErrMsg);
    }
    
    public static void mustInUiThread(String aErrMsg)
    {
        mustOk(isMainThread(),aErrMsg);
    }
    
    private static boolean isMainThread()
    {
        return Looper.getMainLooper()==Looper.myLooper();
    }
    
    public static void mustNotEmpty(String aStr)
    {
        mustOk(StringUtil.isNotEmpty(aStr));
    }
    
    private static void assertDie()
    {
        assertDie("assertDie");
    }
    
    private static void assertDie(String msg)
    {
        IAssert impl=sAssertImpl;
        if(impl!=null)
        {
            impl.assertDie(msg);
        }
    }
    
    public interface IAssert
    {
        void assertDie(String msg);
    }
}
