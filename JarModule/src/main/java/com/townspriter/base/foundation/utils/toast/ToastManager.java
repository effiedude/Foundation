package com.townspriter.base.foundation.utils.toast;

import java.lang.reflect.Field;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/******************************************************************************
 * @path Foundation:ToastManager
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class ToastManager
{
    public static final byte LENGTHxSHORT=0;
    public static final byte LENGTHxLONG=1;
    private static ToastManager sInstance;
    private static String lastToastContent;
    
    public static ToastManager getInstance()
    {
        if(sInstance==null)
        {
            sInstance=new ToastManager();
        }
        return sInstance;
    }
    
    public void showToast(String message,int duration)
    {
        showToast("toast_icon.svg",message,duration);
    }
    
    public void showToast(String iconName,final String content,final int duration)
    {
        if(!TextUtils.isEmpty(content))
        {
            if(content.equals(lastToastContent))
            {
                return;
            }
            if(Looper.myLooper()==Looper.getMainLooper())
            {
                showToastInternal(content,duration);
            }
            else
            {
                ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showToastInternal(content,duration);
                    }
                });
            }
        }
    }
    
    private void showToastInternal(String content,int duration)
    {
        Toast toast=Toast.makeText(Foundation.getApplication(),content,duration);
        // 这个Gravity是从原App迁移过来
        toast.setGravity(Gravity.CENTER,0,0);
        lastToastContent=content;
        try
        {
            HookUtil.hookToast(toast);
            toast.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        ThreadManager.postDelayed(ThreadManager.THREADxUI,new Runnable()
        {
            @Override
            public void run()
            {
                lastToastContent="";
            }
        },2000L);
    }
    
    public static class HookUtil
    {
        public HookUtil()
        {}
        
        private static boolean isNeedHook()
        {
            return VERSION.SDK_INT==24||VERSION.SDK_INT==25;
        }
        
        public static void hookToast(Toast toast)
        {
            if(toast!=null)
            {
                if(isNeedHook())
                {
                    try
                    {
                        Field fieldTn=Toast.class.getDeclaredField("mTN");
                        fieldTn.setAccessible(true);
                        Field tnHandler=fieldTn.getType().getDeclaredField("mHandler");
                        tnHandler.setAccessible(true);
                        Object tn=fieldTn.get(toast);
                        Handler handler=(Handler)tnHandler.get(tn);
                        tnHandler.set(tn,new SafeHandlerWrapper(handler));
                    }
                    catch(Throwable throwable)
                    {
                        AssertUtil.fail(throwable);
                    }
                }
            }
        }
    }
    public static class SafeHandlerWrapper extends Handler
    {
        private final Handler mOriginHandler;
        
        public SafeHandlerWrapper(Handler originHandler)
        {
            this.mOriginHandler=originHandler;
        }
        
        @Override
        public void dispatchMessage(Message msg)
        {
            try
            {
                super.dispatchMessage(msg);
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        
        @Override
        public void handleMessage(Message msg)
        {
            if(this.mOriginHandler!=null)
            {
                this.mOriginHandler.handleMessage(msg);
            }
        }
    }
}
