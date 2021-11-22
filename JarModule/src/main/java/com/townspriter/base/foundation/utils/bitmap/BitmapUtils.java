package com.townspriter.base.foundation.utils.bitmap;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.io.IOUtil;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:BitmapUtils
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 19:13:24
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class BitmapUtils
{
    private static final String TAG="BitmapUtils";
    private static RenderScript renderScript;
    private static ScriptIntrinsicBlur scriptIntrinsicBlur;
    
    public static int getWidth(String localPath)
    {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(localPath,options);
        return options.outWidth;
    }
    
    public static int getHeight(String localPath)
    {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(localPath,options);
        return options.outHeight;
    }
    
    public static void saveBitmap(Bitmap bitmap,String path)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytes=stream.toByteArray();
        File imgFile=new File(path);
        FileOutputStream outputStream=null;
        BufferedOutputStream bufferedOutputStream=null;
        try
        {
            File file=imgFile.getParentFile();
            if(file!=null&&!file.exists())
            {
                file.mkdir();
            }
            if(imgFile.exists())
            {
                imgFile.delete();
            }
            imgFile.createNewFile();
            outputStream=new FileOutputStream(imgFile);
            bufferedOutputStream=new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            IOUtil.safeClose(outputStream);
            IOUtil.safeClose(bufferedOutputStream);
        }
    }
    
    /**
     * <font>
     * 此方法是对安卓系统图片类的创建进行封装.避免由于内存溢出出现崩溃
     * 目前不允许非主进程调用.使用者必须对返回值进行判断和处理.直接使用可能是空指针
     * </font>
     *
     * @param width
     * 构造图片的宽度
     * @param height
     * 构造图片的高度
     * @param config
     * 图片配置信息
     * @return 如果创建失败则返回空.否则返回创建好的位图对象
     */
    public static @Nullable Bitmap createBitmap(int width,int height,Config config)
    {
        Bitmap bitmap=null;
        if(width>0&&height>0)
        {
            try
            {
                bitmap=Bitmap.createBitmap(width,height,config);
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        return bitmap;
    }
    
    public static @Nullable Bitmap createBitmapWithColorBg(int width,int height,int color,Bitmap originBitmap)
    {
        if(width<=0||height<=0)
        {
            return null;
        }
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        Bitmap bitmap=Bitmap.createBitmap(width,height,originBitmap.getConfig());
        Canvas canvas=new Canvas(bitmap);
        canvas.drawRect(0,0,width,height,paint);
        canvas.drawBitmap(originBitmap,(width-originBitmap.getWidth())/2,(height-originBitmap.getHeight())/2,paint);
        return bitmap;
    }
    
    /**
     * 绘制一个带背景色的图片
     *
     * @param width
     * 控件大小
     * @param height
     * 控件大小
     * @param color
     * 背景色
     * @param radius
     * 圆角半径
     * @param originBitmap
     * 源图
     * @return 着色后的图片
     */
    public static @Nullable Bitmap createBitmapWithColorBg(int width,int height,int color,int radius,Bitmap originBitmap)
    {
        if(width<=0||height<=0)
        {
            return null;
        }
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        Bitmap bitmap=Bitmap.createBitmap(width,height,originBitmap.getConfig());
        Canvas canvas=new Canvas(bitmap);
        canvas.drawRoundRect(new RectF(0,0,width,height),radius,radius,paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Bitmap font=createBitmapWithColorBg(width,height,color,originBitmap);
        if(font!=null)
        {
            canvas.drawBitmap(font,0,0,paint);
        }
        return bitmap;
    }
    
    /**
     * @param bitmap
     * 源图片
     * @param width
     * 需要显示的宽度
     * @param height
     * 需要显示的高度
     * @return 等比缩放后的图片
     */
    public static @Nullable Bitmap getScaledBitmap(Bitmap bitmap,int width,int height)
    {
        if(bitmap==null)
        {
            return null;
        }
        int ow=bitmap.getWidth();
        int oh=bitmap.getHeight();
        if(ow==0||oh==0)
        {
            return null;
        }
        int nw;
        int nh;
        if(width*oh/ow>height)
        {
            nh=height;
            nw=ow*nh/oh;
        }
        else
        {
            nw=width;
            nh=oh*nw/ow;
        }
        if(nw>0&&nh>=0)
        {
            return Bitmap.createScaledBitmap(bitmap,nw,nh,true);
        }
        return null;
    }
    
    /**
     * 高斯模糊(RenderScript方式.比FastBlur方式快)
     *
     * @param source
     * @param radius
     * (0,25]
     * @param scale
     * 图缩放比例.通常高斯模糊可以将图缩小后再做运算效率会高很多
     */
    public static Bitmap rsBlur(@NonNull Bitmap source,@IntRange(from=0,to=25) int radius,float scale)
    {
        return rsBlur(source,radius,scale,0);
    }
    
    /**
     * 高斯模糊(RenderScript方式.比FastBlur方式快)
     *
     * @param source
     * @param radius
     * (0,25]
     * @param scale
     * 图缩放比例.通常高斯模糊可以将图缩小后再做运算效率会高很多
     * @param maskColor
     * 蒙层色值(如果不需要则传透明色值)
     */
    public static Bitmap rsBlur(@NonNull Bitmap source,@IntRange(from=0,to=25) int radius,float scale,int maskColor)
    {
        int width=(int)(source.getWidth()*scale);
        int height=(int)(source.getHeight()*scale);
        Bitmap inputBmp=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        return rsBlur(source,inputBmp,radius,maskColor);
    }
    
    /**
     * 高斯模糊(RenderScript方式.比FastBlur方式快)
     *
     * @param source
     * @param radius
     * (0,25]
     * @param maskColor
     * 蒙层色值(如果不需要则传透明色值)
     */
    public static Bitmap rsBlur(@NonNull Bitmap source,@NonNull Bitmap dst,@IntRange(from=0,to=25) int radius,int maskColor)
    {
        if(source==null||source.isRecycled()||dst==null||dst.isRecycled())
        {
            return dst;
        }
        Canvas canvas=new Canvas(dst);
        canvas.drawBitmap(source,new Rect(0,0,source.getWidth(),source.getHeight()),new Rect(0,0,dst.getWidth(),dst.getHeight()),null);
        if(maskColor!=0)
        {
            canvas.drawColor(maskColor);
        }
        /**
         * Renderscript和ScriptIntrinsicBlur对象共享后.部分机型如华为等会有线程同步问题
         * 考虑到图片高斯模糊一般在异步线程执行且RenderScript性能高.因此这里做线程同步的损耗可以忽略
         */
        synchronized(BitmapUtils.class)
        {
            if(renderScript==null)
            {
                /** 有点耗时(大概在一百毫秒左右) */
                renderScript=RenderScript.create(Foundation.getApplication());
            }
            final Allocation input=Allocation.createFromBitmap(renderScript,dst);
            final Allocation output=Allocation.createTyped(renderScript,input.getType());
            if(scriptIntrinsicBlur==null)
            {
                /** 有点耗时(大概在一百毫秒左右) */
                scriptIntrinsicBlur=ScriptIntrinsicBlur.create(renderScript,Element.U8_4(renderScript));
            }
            scriptIntrinsicBlur.setInput(input);
            /** 设置高斯模糊半径 */
            scriptIntrinsicBlur.setRadius(radius);
            /** 开始高斯模糊 */
            scriptIntrinsicBlur.forEach(output);
            /** 拷贝到目标高斯模糊图片中 */
            output.copyTo(dst);
            /** 不销毁跟随应用生命周期 */
            // renderScript.destroy();
        }
        return dst;
    }
    
    /**
     * 高斯模糊
     *
     * @param scaleRadius
     * 图片缩放比例
     * @param blurRadius
     * 模糊倍数
     * @param bitmap
     * 源图片
     * @return 高斯后的图片
     */
    public static Bitmap fastBlur(int scaleRadius,int blurRadius,Bitmap bitmap)
    {
        Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/scaleRadius,bitmap.getHeight()/scaleRadius,false);
        return FastBlur.doBlur(scaledBitmap,blurRadius,true);
    }
    
    /*********************************
     * @function calculateInSampleSize
     * @since JDK 1.7.0-79
     * @describe
     * <p>
     * 计算原图的采样率.如果原图宽度小于屏幕宽度则原图采样.<br>
     * 如果原图宽度大于屏幕宽度则采样率扩大两倍直到原图宽度小于屏幕宽度.
     * @param baseWidth
     * 基准承载宽度
     * @param currentWidth
     * 当前图片宽度
     * @exception
     * @return
     * int 采样率
     * @date
     * @version 1.0.0.0
     * ********************************
     */
    public static int calculateInSampleSize(int baseWidth,int currentWidth)
    {
        int halfWidth=currentWidth;
        int inSampleSize=1;
        while(halfWidth>baseWidth)
        {
            inSampleSize*=2;
            halfWidth/=2;
        }
        return inSampleSize;
    }
}