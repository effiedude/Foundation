package com.townspriter.android.foundation.utils.device;
/******************************************************************************
 * @Path Foundation:LowPerformanceManager
 * @Describe 判断是否是低端机
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
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
