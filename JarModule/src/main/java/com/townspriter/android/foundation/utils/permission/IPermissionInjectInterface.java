package com.townspriter.android.foundation.utils.permission;
/******************************************************************************
 * @path Foundation:IPermissionInjectInterface
 * @version 1.0.0.0
 * @describe 权限注入接口类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 17:12:18
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface IPermissionInjectInterface
{
    /**
     * 当前外部存储是否可用.可用需满足两个条件:
     * 1.挂载正常
     * 2.权限已开启
     * 
     * @return
     */
    boolean isInjectExternalStorageAvailable();
}