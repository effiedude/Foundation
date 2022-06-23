package com.townspriter.base.foundation.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

/******************************************************************************
 * @path PermissionUtil
 * @version 1.0.0.0
 * @describe 权限申请工具类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 17:09:31
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class PermissionUtil
{
    public static final int REQUESTxPERMISSIONxSTORAGExIMAGExCODE=1;
    public static final int REQUESTxPERMISSIONxSTORAGExVIDEOxCODE=2;
    public static final int REQUESTxPERMISSIONxRECORDxCODE=3;
    public static final int REQUESTxPERMISSIONxALERTxCODE=4;
    private static final String[] PERMISSIONSxSTORAGE={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String[] PERMISSIONSxRECORD={Manifest.permission.RECORD_AUDIO};
    private static final String[] PERMISSIONSxALERT={Manifest.permission.SYSTEM_ALERT_WINDOW};
    
    public static boolean requestImageStoragePermissions(Activity activity)
    {
        int permission=ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,PERMISSIONSxSTORAGE,REQUESTxPERMISSIONxSTORAGExIMAGExCODE);
        }
        return permission==PackageManager.PERMISSION_GRANTED;
    }
    
    public static boolean requestVideoStoragePermissions(Activity activity)
    {
        int permission=ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,PERMISSIONSxSTORAGE,REQUESTxPERMISSIONxSTORAGExVIDEOxCODE);
        }
        return permission==PackageManager.PERMISSION_GRANTED;
    }
    
    public static boolean requestRecordPermissions(Activity activity)
    {
        int permission=ActivityCompat.checkSelfPermission(activity,Manifest.permission.RECORD_AUDIO);
        if(permission!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,PERMISSIONSxRECORD,REQUESTxPERMISSIONxRECORDxCODE);
        }
        return permission==PackageManager.PERMISSION_GRANTED;
    }
}
