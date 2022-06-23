package com.townspriter.base.foundation.utils.lang;

import java.util.Random;
import androidx.annotation.IntRange;

/******************************************************************************
 * @path RandomUtil
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class RandomUtil
{
    private static final Random RANDOM=new Random();
    
    public static boolean nextBoolean()
    {
        return RANDOM.nextBoolean();
    }
    
    public static byte[] nextBytes(@IntRange(from=0) int count)
    {
        checkArgument(count>=0,"不允许为负数");
        byte[] result=new byte[count];
        RANDOM.nextBytes(result);
        return result;
    }
    
    public static int nextInt(@IntRange(from=0) int startInclusive,@IntRange(from=0) int endExclusive)
    {
        checkArgument(endExclusive>=startInclusive,"起始值不应该大于结束值");
        checkArgument(startInclusive>=0,"不允许为负数");
        if(startInclusive==endExclusive)
        {
            return startInclusive;
        }
        return startInclusive+RANDOM.nextInt(endExclusive-startInclusive);
    }
    
    public static int nextInt()
    {
        return nextInt(0,Integer.MAX_VALUE);
    }
    
    public static long nextLong(@IntRange(from=0) long startInclusive,@IntRange(from=0) long endExclusive)
    {
        checkArgument(endExclusive>=startInclusive,"起始值不应该大于结束值");
        checkArgument(startInclusive>=0,"不允许为负数");
        if(startInclusive==endExclusive)
        {
            return startInclusive;
        }
        return (long)nextDouble(startInclusive,endExclusive);
    }
    
    public static long nextLong()
    {
        return nextLong(0,Long.MAX_VALUE);
    }
    
    public static double nextDouble(double startInclusive,double endInclusive)
    {
        checkArgument(endInclusive>=startInclusive,"起始值不应该大于结束值");
        checkArgument(startInclusive>=0,"不允许为负数");
        if(startInclusive==endInclusive)
        {
            return startInclusive;
        }
        return startInclusive+((endInclusive-startInclusive)*RANDOM.nextDouble());
    }
    
    public static double nextDouble()
    {
        return nextDouble(0,Double.MAX_VALUE);
    }
    
    public static float nextFloat(float startInclusive,float endInclusive)
    {
        checkArgument(endInclusive>=startInclusive,"起始值不应该大于结束值");
        checkArgument(startInclusive>=0,"不允许为负数");
        if(startInclusive==endInclusive)
        {
            return startInclusive;
        }
        return startInclusive+((endInclusive-startInclusive)*RANDOM.nextFloat());
    }
    
    public static float nextFloat()
    {
        return nextFloat(0,Float.MAX_VALUE);
    }
    
    private static void checkArgument(boolean cond,String desc)
    {
        if(!cond)
        {
            throw new IllegalArgumentException(desc);
        }
    }
}