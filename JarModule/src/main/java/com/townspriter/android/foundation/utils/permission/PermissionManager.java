package com.townspriter.android.foundation.utils.permission;

import androidx.annotation.NonNull;

/******************************************************************************
 * @path Foundation:PermissionManager
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 17:12:56
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class PermissionManager
{
    private IPermissionInjectInterface mIPermissionInjectInterface=new AbstractPermissionInjectAdapter()
    {
        @Override
        public boolean isInjectExternalStorageAvailable()
        {
            return super.isInjectExternalStorageAvailable();
        }
    };
    
    private PermissionManager()
    {}
    
    public static PermissionManager getInstance()
    {
        return PermissionHolder.INSTANCE;
    }
    
    @NonNull
    public IPermissionInjectInterface getPermissionInjectInterface()
    {
        return mIPermissionInjectInterface;
    }
    
    public void setPermissionInjectInterface(IPermissionInjectInterface impl)
    {
        mIPermissionInjectInterface=impl;
    }
    
    private static class PermissionHolder
    {
        private static final PermissionManager INSTANCE=new PermissionManager();
    }
}
