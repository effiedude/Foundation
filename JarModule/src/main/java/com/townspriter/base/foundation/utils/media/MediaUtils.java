package com.townspriter.base.foundation.utils.media;

import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.net.URLUtil;
import com.townspriter.base.foundation.utils.text.StringUtil;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:MediaUtils
 * @version 1.0.0.0
 * @describe 多媒体工具类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 17:34:49
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class MediaUtils
{
    private static final String TAG="MediaUtils";
    private static final MediaMetadataRetriever RETRIEVER=new MediaMetadataRetriever();
    
    /**
     * getLocalVideoBitmapCover
     *
     * @param filePath
     * 视频本地地址
     * @param microseconds
     * 微秒
     * @return Bitmap
     */
    public static @Nullable Bitmap loadVideoCoverLocal(@NonNull String filePath,@IntRange(from=0) long microseconds)
    {
        RETRIEVER.setDataSource(filePath);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P)
        {
            return RETRIEVER.getFrameAtIndex(1);
        }
        /** 检索与最接近或给定时间的数据源相关联的帧(不一定是关键帧) */
        return RETRIEVER.getFrameAtTime(microseconds,MediaMetadataRetriever.OPTION_CLOSEST);
    }
    
    /**
     * getMediaDuration
     *
     * @param filePath
     * 音视频本地地址
     * @return 音视频时长(毫秒)
     */
    public static long getMediaDuration(@NonNull String filePath)
    {
        String result=extractMetadata(filePath,MediaMetadataRetriever.METADATA_KEY_DURATION);
        if(StringUtil.isEmptyWithNull(result))
        {
            return 0;
        }
        return Long.parseLong(result);
    }
    
    /**
     * getVideoFrameWidth
     *
     * @param filePath
     * @return 视频帧宽度(有失败情况)
     */
    public static int getVideoFrameWidth(@NonNull String filePath)
    {
        String result=extractMetadata(filePath,MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        if(StringUtil.isEmptyWithNull(result))
        {
            return 0;
        }
        return Integer.parseInt(result);
    }
    
    /**
     * getVideoFrameHeight
     *
     * @param filePath
     * @return 视频帧高度(有失败情况)
     */
    public static int getVideoFrameHeight(@NonNull String filePath)
    {
        String result=extractMetadata(filePath,MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        if(StringUtil.isEmptyWithNull(result))
        {
            return 0;
        }
        return Integer.parseInt(result);
    }
    
    private static @Nullable String extractMetadata(@NonNull String filePath,@IntRange(from=0) int keyCode)
    {
        if(URLUtil.isNetworkUrl(filePath))
        {
            return null;
        }
        /** 有可能是无效地址需进行异常处理 */
        try
        {
            RETRIEVER.setDataSource(filePath);
            return RETRIEVER.extractMetadata(keyCode);
        }
        catch(Exception exception)
        {
            Logger.e(TAG,"extractMetadata:Exception",exception);
        }
        return null;
    }
}
