package com.townspriter.base.foundation.utils.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import com.townspriter.base.foundation.R;
import com.townspriter.base.foundation.utils.lang.DateFormatUtil;
import com.townspriter.base.foundation.utils.ui.ResHelper;
import android.os.SystemClock;
import androidx.annotation.NonNull;

/******************************************************************************
 * @path TimeUtil
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 19:14:28
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class TimeUtil
{
    public static final int HOURxINxMILLIS=60*60*1000;
    public static final int MINUTExINxMILLIS=60*1000;
    public static final int SECONDxINxMILLIS=1000;
    private static final String FORMATxHOUR="%02d:%02d:%02d";
    private static final String FORMATxMINUTE="%02d:%02d";
    private static final long MINUTE=60*1000L;
    private static final long HOUR=60*MINUTE;
    private static final long DAY=24*HOUR;
    private static long mServerTime;
    private static long serverTimeUpdateTs;
    
    public static String formatServerTimeToPublishTime(@NonNull String serverTime) throws ParseException
    {
        long utcTime=TimeUtil.getUTCTime(serverTime,TimeZone.getDefault());
        return TimeUtil.formatPublishTime(utcTime);
    }
    
    /**
     * 目前调用方直接解析服务端响应头返回的时间,这里存在细微的时间差(秒级),因为服务端从返回到客户端接收到数据中间耗时无法确定
     */
    public static void setServerTime(long serverTime)
    {
        mServerTime=serverTime;
        if(mServerTime>0)
        {
            serverTimeUpdateTs=SystemClock.elapsedRealtime();
        }
    }
    
    /**
     * 获取服务端当前时间,如果尚未有接口请求返回,则返回系统时间
     */
    public static long preferredServerTimeInMills()
    {
        if(mServerTime>0)
        {
            return getServerTimeInMills();
        }
        else
        {
            return System.currentTimeMillis();
        }
    }
    
    /**
     * 获取服务端当前时间,如果尚未有接口请求返回,则返回0
     */
    public static long getServerTimeInMills()
    {
        if(mServerTime>0)
        {
            return mServerTime+SystemClock.elapsedRealtime()-serverTimeUpdateTs;
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * 获取当前的UTC时间
     *
     * @return
     */
    public static long getCurrentUTCTime()
    {
        return System.currentTimeMillis();
    }
    
    /**
     * 根据输入的时间和时区获取对应的UTC时间
     *
     * @param time
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long getUTCTime(String time,TimeZone timeZone) throws ParseException
    {
        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormatter.setTimeZone(timeZone);
        return dateFormatter.parse(time).getTime();
    }
    
    /**
     * 将时间戳转换成日期
     *
     * @param time
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getUTCTime(long time) throws ParseException
    {
        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(time);
    }
    
    public static String timeFormat(long time)
    {
        return timeFormat(time,false);
    }
    
    public static String timeFormat(long time,boolean forceWithHour)
    {
        if(time<0)
        {
            time=0;
        }
        String formatTime;
        long hour=time/HOURxINxMILLIS;
        long minute=time/MINUTExINxMILLIS%60;
        long second=time/SECONDxINxMILLIS%60;
        if(hour>0||forceWithHour)
        {
            formatTime=String.format(Locale.CHINA,FORMATxHOUR,hour,minute,second);
        }
        else
        {
            formatTime=String.format(Locale.CHINA,FORMATxMINUTE,minute,second);
        }
        return formatTime;
    }
    
    /**
     * 格式化一段时间为<code>HH:MM:SS</code>
     *
     * @param timeInMillis
     * @return
     */
    public static String formatDurationTime(long timeInMillis)
    {
        if(timeInMillis<=0)
        {
            return "0";
        }
        long hours=timeInMillis/(1000*60*60);
        long left=timeInMillis%(1000*60*60);
        long mins=left/(1000*60);
        left=left%(1000*60);
        long second=left/1000;
        StringBuilder time=new StringBuilder();
        if(hours>0)
        {
            if(hours<10)
            {
                time.append(0);
            }
            time.append(hours).append(":");
        }
        if(mins<10)
        {
            time.append(0);
        }
        time.append(mins).append(":");
        if(second<10)
        {
            time.append(0);
        }
        time.append(second);
        return time.toString();
    }
    
    /**
     * <p>
     * 输出格式
     * 一分钟内显示:刚刚
     * 一小时内显示:几分钟前
     * 一天内显示:几小时前
     * 两天内显示:几天前
     * 超过两天显示月日:如06-11
     * 跨年显示年月日:如2020-06-11
     * </p>
     * 
     * @param publishTime
     * @return
     */
    public static String formatPublishTime(long publishTime)
    {
        String ret="";
        long now=System.currentTimeMillis();
        long timeDelta=now-publishTime;
        if(timeDelta<MINUTE)
        {
            ret=ResHelper.getString(R.string.foundationTimeJustNow);
        }
        else if(timeDelta<HOUR)
        {
            ret=(timeDelta/MINUTE)+ResHelper.getString(R.string.foundationTimeMinutes);
        }
        else if(timeDelta<DAY)
        {
            ret=(timeDelta/HOUR)+ResHelper.getString(R.string.foundationTimeHours);
        }
        else if(timeDelta<3*DAY)
        {
            ret=(timeDelta/DAY)+ResHelper.getString(R.string.foundationTimeDays);
        }
        else
        {
            Date nowDate=new Date(now);
            Date publishDate=new Date(publishTime);
            if(nowDate.getYear()==publishDate.getYear())
            {
                ret=DateFormatUtil.getSimpleDateFormat("MM-dd").format(publishDate);
            }
            else
            {
                ret=DateFormatUtil.getSimpleDateFormat("yyyy-MM-dd").format(publishDate);
            }
        }
        return ret;
    }
    
    public static final String formatMuteTime(long muteTime)
    {
        return DateFormatUtil.getSimpleDateFormat("yyyy年MM月dd日HH时mm分").format(muteTime);
    }
    
    public static boolean isSameDay(Calendar calendar1,Calendar calendar2)
    {
        if(null==calendar1||null==calendar2)
        {
            throw new IllegalArgumentException("日期不可为空");
        }
        return calendar1.get(Calendar.ERA)==calendar2.get(Calendar.ERA)&&calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR)&&calendar1.get(Calendar.DAY_OF_YEAR)==calendar2.get(Calendar.DAY_OF_YEAR);
    }
    
    public static boolean isSameDay(long time1,long time2)
    {
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2=Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        return isSameDay(calendar1,calendar2);
    }
    
    public static boolean isTomorrow(long time)
    {
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTimeInMillis(time);
        Calendar calendar2=Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH,1);
        return isSameDay(calendar1,calendar2);
    }
    
    public static boolean isToday(long time)
    {
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTimeInMillis(time);
        Calendar calendar2=Calendar.getInstance();
        return isSameDay(calendar1,calendar2);
    }
}
