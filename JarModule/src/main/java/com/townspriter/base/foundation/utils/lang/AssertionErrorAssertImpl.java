package com.townspriter.base.foundation.utils.lang;

import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.lang.AssertUtil.IAssert;
import android.util.Log;

/******************************************************************************
 * @path Foundation:AssertionErrorAssertImpl
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class AssertionErrorAssertImpl implements IAssert
{
    private static final String TAG="AssertionErrorAssertImpl";
    
    @Override
    public void assertDie(String message)
    {
        if(!Foundation.isRelease())
        {
            throw new AssertionError(message);
        }
        else
        {
            if(message==null)
            {
                message="assertDie";
            }
            Log.e(TAG,message);
        }
    }
}
