package com.townspriter.android.foundation.utils.event;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.UiThread;

/******************************************************************************
 * @Path Foundation:HandlerCenter
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
@UiThread
public class HandlerCenter implements IProcessor
{
    private final List<IHandler> mHandlers=new ArrayList<>(8);
    
    @Override
    public boolean processCommand(int id,EventParams params,EventParams result)
    {
        for(IHandler processor:mHandlers)
        {
            boolean intercept=processor.processCommand(id,params,result);
            if(intercept)
            {
                return true;
            }
        }
        return false;
    }
    
    /** 添加处理器.从性能考虑这个类没有做线程保护.业务自己注意用法 */
    public void addHandler(IHandler processor)
    {
        mHandlers.add(processor);
    }
}
