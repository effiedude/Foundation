package com.townspriter.base.foundation.utils.device;
/******************************************************************************
 * @path Foundation:LowPerformanceManager
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:07:42
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class LowPerformanceManager
{
    private static final int LOWxDEVICExWIDTH=480;
    private static volatile LowPerformanceManager sLowPerformanceMnager;
    private Boolean mIsLowMachine;
    private Boolean mIsPoorMachine;
    
    private LowPerformanceManager()
    {}
    
    public static LowPerformanceManager getInstance()
    {
        if(sLowPerformanceMnager==null)
        {
            synchronized(LowPerformanceManager.class)
            {
                if(sLowPerformanceMnager==null)
                {
                    sLowPerformanceMnager=new LowPerformanceManager();
                }
            }
        }
        return sLowPerformanceMnager;
    }
    
    /** 判断低端机 */
    public boolean isLowMachine()
    {
        if(mIsLowMachine!=null)
        {
            return mIsLowMachine;
        }
        mIsLowMachine=false;
        int year=YearClass.get();
        if(year<=2012)
        {
            mIsLowMachine=true;
        }
        return mIsLowMachine;
    }
    
    /** 极低端机(这种情况可能需要考虑牺牲用户的体验) */
    public boolean isPoorMachine()
    {
        if(mIsPoorMachine!=null)
        {
            return mIsPoorMachine;
        }
        mIsPoorMachine=false;
        int year=YearClass.get();
        if(year<=2010)
        {
            mIsPoorMachine=true;
        }
        return mIsPoorMachine;
    }
}
