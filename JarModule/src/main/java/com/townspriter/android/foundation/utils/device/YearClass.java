package com.townspriter.android.foundation.utils.device;

import java.util.ArrayList;
import java.util.Collections;

/******************************************************************************
 * @Path Foundation:YearClass
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6-下午2:41
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class YearClass
{
    public static final int CLASSxUNKNOWN=-1;
    public static final int CLASSx2008=2008;
    public static final int CLASSx2009=2009;
    public static final int CLASSx2010=2010;
    public static final int CLASSx2011=2011;
    public static final int CLASSx2012=2012;
    public static final int CLASSx2013=2013;
    public static final int CLASSx2014=2014;
    public static final int CLASSx2015=2015;
    public static final int CLASSx2016=2016;
    public static final int CLASSx2017=2017;
    private static final long MB=1024*1024;
    private static final int MHZxINxKHZ=1000;
    private static volatile Integer mYearCategory;
    
    public static int get()
    {
        if(mYearCategory==null)
        {
            synchronized(YearClass.class)
            {
                if(mYearCategory==null)
                {
                    mYearCategory=categorizeByYear2017Method();
                }
            }
        }
        return mYearCategory;
    }
    
    private static void conditionallyAdd(ArrayList<Integer> list,int value)
    {
        if(value!=CLASSxUNKNOWN)
        {
            list.add(value);
        }
    }
    
    private static int categorizeByYear2017Method()
    {
        long totalRam=DeviceInfo.getTotalMemory();
        if(totalRam==DeviceInfo.DEVICEINFOxUNKNOWN)
        {
            return categorizeByYear2014Method();
        }
        if(totalRam<=768*MB)
        {
            return DeviceInfo.getNumberOfCPUCores()<=1?CLASSx2009:CLASSx2010;
        }
        if(totalRam<=1024*MB)
        {
            return DeviceInfo.getCPUMaxFreqKHZ()<1300*MHZxINxKHZ?CLASSx2011:CLASSx2012;
        }
        if(totalRam<=1536*MB)
        {
            return DeviceInfo.getCPUMaxFreqKHZ()<1800*MHZxINxKHZ?CLASSx2013:CLASSx2014;
        }
        if(totalRam<=2048*MB)
        {
            return CLASSx2014;
        }
        if(totalRam<=3*1024*MB)
        {
            return CLASSx2015;
        }
        return totalRam<=5*1024*MB?CLASSx2016:CLASSx2017;
    }
    
    private static int categorizeByYear2014Method()
    {
        ArrayList<Integer> componentYears=new ArrayList<Integer>();
        conditionallyAdd(componentYears,getNumCoresYear());
        conditionallyAdd(componentYears,getClockSpeedYear());
        conditionallyAdd(componentYears,getRamYear());
        if(componentYears.isEmpty())
        {
            return CLASSxUNKNOWN;
        }
        Collections.sort(componentYears);
        if((componentYears.size()&0x01)==1)
        {
            return componentYears.get(componentYears.size()/2);
        }
        else
        {
            int baseIndex=componentYears.size()/2-1;
            return componentYears.get(baseIndex)+(componentYears.get(baseIndex+1)-componentYears.get(baseIndex))/2;
        }
    }
    
    private static int getNumCoresYear()
    {
        int cores=DeviceInfo.getNumberOfCPUCores();
        if(cores<1)
        {
            return CLASSxUNKNOWN;
        }
        if(cores==1)
        {
            return CLASSx2008;
        }
        if(cores<=3)
        {
            return CLASSx2011;
        }
        return CLASSx2012;
    }
    
    private static int getClockSpeedYear()
    {
        long clockSpeedKHz=DeviceInfo.getCPUMaxFreqKHZ();
        if(clockSpeedKHz==DeviceInfo.DEVICEINFOxUNKNOWN)
        {
            return CLASSxUNKNOWN;
        }
        if(clockSpeedKHz<=528*MHZxINxKHZ)
        {
            return CLASSx2008;
        }
        if(clockSpeedKHz<=620*MHZxINxKHZ)
        {
            return CLASSx2009;
        }
        if(clockSpeedKHz<=1020*MHZxINxKHZ)
        {
            return CLASSx2010;
        }
        if(clockSpeedKHz<=1220*MHZxINxKHZ)
        {
            return CLASSx2011;
        }
        if(clockSpeedKHz<=1520*MHZxINxKHZ)
        {
            return CLASSx2012;
        }
        if(clockSpeedKHz<=2020*MHZxINxKHZ)
        {
            return CLASSx2013;
        }
        return CLASSx2014;
    }
    
    private static int getRamYear()
    {
        long totalRam=DeviceInfo.getTotalMemory();
        if(totalRam<=0)
        {
            return CLASSxUNKNOWN;
        }
        if(totalRam<=192*MB)
        {
            return CLASSx2008;
        }
        if(totalRam<=290*MB)
        {
            return CLASSx2009;
        }
        if(totalRam<=512*MB)
        {
            return CLASSx2010;
        }
        if(totalRam<=1024*MB)
        {
            return CLASSx2012;
        }
        if(totalRam<=1536*MB)
        {
            return CLASSx2013;
        }
        if(totalRam<=2048*MB)
        {
            return CLASSx2014;
        }
        return CLASSx2015;
    }
}
