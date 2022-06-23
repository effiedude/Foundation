package com.townspriter.base.foundation.utils.event;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.UiThread;

/******************************************************************************
 * @path HandlerCenter
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
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
