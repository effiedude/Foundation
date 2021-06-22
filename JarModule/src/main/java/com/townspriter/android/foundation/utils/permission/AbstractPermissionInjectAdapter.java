package com.townspriter.android.foundation.utils.permission;
/******************************************************************************
 * @path Foundation:AbstractPermissionInjectAdapter
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-04
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public abstract class AbstractPermissionInjectAdapter implements IPermissionInjectInterface
{
    @Override
    public boolean isInjectExternalStorageAvailable()
    {
        return false;
    }
}
