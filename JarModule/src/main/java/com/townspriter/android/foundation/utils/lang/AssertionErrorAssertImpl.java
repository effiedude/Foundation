package com.townspriter.android.foundation.utils.lang;

import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.lang.AssertUtil.IAssert;
import android.util.Log;

/******************************************************************************
 * @Path Foundation:AssertionErrorAssertImpl
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-04
 * CopyRight(C)2021 小镇精灵科技版权所有
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
