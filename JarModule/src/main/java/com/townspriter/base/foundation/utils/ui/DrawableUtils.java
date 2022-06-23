package com.townspriter.base.foundation.utils.ui;

import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.ui.ShadowDrawable.Builder;
import android.R.attr;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.graphics.drawable.DrawableCompat;

/******************************************************************************
 * @path DrawableUtils
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021年11月22日 15:37:51
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class DrawableUtils
{
    public static ShapeDrawable getRectShapeDrawable(int color,int alpha,float strokeWidth)
    {
        RectShape rectShape=new RectShape();
        ShapeDrawable shapeDrawable=new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setStyle(Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(strokeWidth);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setAlpha(alpha);
        return shapeDrawable;
    }
    
    public static ShapeDrawable getRectShapeDrawableWithWH(int color,int width,int height)
    {
        RectShape rectShape=new RectShape();
        ShapeDrawable shapeDrawable=new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.setBounds(0,0,width,height);
        return shapeDrawable;
    }
    
    public static ShapeDrawable getRectShapeDrawableUseFill(int color)
    {
        RectShape rectShape=new RectShape();
        ShapeDrawable shapeDrawable=new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setStyle(Style.FILL);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }
    
    /** 点击的背景变化效果 */
    public static Drawable getPressStateListDrawable(int defaultColor,int pressedColor)
    {
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{attr.state_pressed},new ColorDrawable(pressedColor));
        stateListDrawable.addState(new int[]{},new ColorDrawable(defaultColor));
        return stateListDrawable;
    }
    
    public static ShapeDrawable getRoundRectShapeDrawable(int radius,int color)
    {
        return getRoundRectShapeDrawable(radius,radius,radius,radius,color);
    }
    
    public static ShapeDrawable getRoundRectShapeDrawable(int radiusTopLeft,int radiusTopRight,int radiusBottomLeft,int radiusBottomRight,int color)
    {
        float[] outerR={radiusTopLeft,radiusTopLeft,radiusTopRight,radiusTopRight,radiusBottomLeft,radiusBottomLeft,radiusBottomRight,radiusBottomRight};
        RoundRectShape roundRectShape=new RoundRectShape(outerR,null,null);  // 构造一个圆角矩形,可以使用其他形状，这样ShapeDrawable 就会根据形状来绘制。
        ShapeDrawable bgDrawable=new ShapeDrawable();
        bgDrawable.setShape(roundRectShape);
        bgDrawable.getPaint().setColor(color);
        return bgDrawable;
    }
    
    /**
     * 获取圆角渐变资源
     *
     * @param bgColor
     * @param radius
     * @return
     */
    public static GradientDrawable getGradientDrawable(int bgColor,float radius)
    {
        GradientDrawable drawable=new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(bgColor);
        return drawable;
    }
    
    public static GradientDrawable getGradientDrawable(int strokeColor,int bgColor,float radius)
    {
        GradientDrawable drawable=new GradientDrawable();
        drawable.setStroke(1,strokeColor);
        drawable.setCornerRadius(radius);
        drawable.setColor(bgColor);
        return drawable;
    }
    
    public static GradientDrawable getGradientDrawable(int strokeColor,int bgColor,int stroke,float radius)
    {
        GradientDrawable drawable=new GradientDrawable();
        drawable.setStroke(stroke,strokeColor);
        drawable.setCornerRadius(radius);
        drawable.setColor(bgColor);
        return drawable;
    }
    
    /**
     * 两边是半圆的胶囊型背景(只有边框)
     *
     * @param size
     * 半圆直径
     */
    public static GradientDrawable getCapsuleDrawable(int size,int strokeWidth,int color)
    {
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(size/2);
        gradientDrawable.setStroke(strokeWidth,color);
        gradientDrawable.setColor(Color.TRANSPARENT);
        return gradientDrawable;
    }
    
    public static ShapeDrawable getCircleDrawable(int color,int width)
    {
        return getCircleDrawable(color,width,width,true);
    }
    
    /**
     * @param color
     * @param width
     * 不包含边线
     * @param height
     * @param fill
     * @return
     */
    public static ShapeDrawable getCircleDrawable(int color,int width,int height,boolean fill)
    {
        int stokeWidth=0;
        if(!fill)
        {
            stokeWidth=ResHelper.dpToPxI(2);
        }
        ArcShape arcShape=new ArcShape(0,360);
        ShapeDrawable drawable=new ShapeDrawable(arcShape);
        drawable.setIntrinsicWidth(width+stokeWidth);
        drawable.setIntrinsicHeight(height+stokeWidth);
        drawable.getPaint().setColor(color);
        if(fill)
        {
            drawable.getPaint().setStyle(Style.FILL);
        }
        else
        {
            drawable.getPaint().setStyle(Style.STROKE);
            drawable.getPaint().setStrokeWidth(stokeWidth);
        }
        return drawable;
    }
    
    /**
     * 两边是半圆的胶囊型背景(实心)
     *
     * @param size
     * 半圆直径
     */
    public static GradientDrawable getCapsuleDrawable(int size,int color)
    {
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(size/2);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }
    
    public static GradientDrawable getCornerDrawable(int radius,int color)
    {
        return getGradientDrawable(0,color,0,radius);
    }
    
    public static GradientDrawable getCornerDrawable(int size,int radius,int color)
    {
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }
    
    public static StateListDrawable getStateListDrawable(int strokeColor,int bgColor,float radius)
    {
        int pressedColor=Color.argb(128,Color.red(strokeColor),Color.green(strokeColor),Color.blue(strokeColor));
        Drawable pressedDrawable=getGradientDrawable(pressedColor,bgColor,radius);
        Drawable defaultDrawable=getGradientDrawable(strokeColor,bgColor,radius);
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{attr.state_pressed},pressedDrawable);
        stateListDrawable.addState(new int[]{},defaultDrawable);
        return stateListDrawable;
    }
    
    public static StateListDrawable getStateListDrawable(int strokeNormalColor,int strokePressColor,int bgColor)
    {
        return getStateListDrawable(strokeNormalColor,strokePressColor,bgColor,0);
    }
    
    public static StateListDrawable getStateListDrawable(int strokeNormalColor,int strokePressColor,int bgColor,float radius)
    {
        Drawable pressedDrawable=getGradientDrawable(strokePressColor,bgColor,radius);
        Drawable defaultDrawable=getGradientDrawable(strokeNormalColor,bgColor,radius);
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{attr.state_pressed},pressedDrawable);
        stateListDrawable.addState(new int[]{},defaultDrawable);
        return stateListDrawable;
    }
    
    public static StateListDrawable getRoundCornerRectDrawable(int strokeColor,int defaultSolidColor,int pressedSolidColor,int radius)
    {
        GradientDrawable pressedDrawable=getGradientDrawable(strokeColor,pressedSolidColor,radius);
        GradientDrawable defaultDrawable=getGradientDrawable(strokeColor,defaultSolidColor,radius);
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        defaultDrawable.setShape(GradientDrawable.RECTANGLE);
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{attr.state_pressed},pressedDrawable);
        stateListDrawable.addState(new int[]{},defaultDrawable);
        return stateListDrawable;
    }
    
    public static StateListDrawable getColorDrawable(int normalColor,int pressedColor)
    {
        StateListDrawable dr=new StateListDrawable();
        dr.addState(new int[]{attr.state_pressed},new ColorDrawable(pressedColor));
        dr.addState(new int[]{},new ColorDrawable(normalColor));
        return dr;
    }
    
    public static StateListDrawable getClickableRoundRectDrawable(int bgColor,int radius)
    {
        int pressedColor=Color.argb(128,Color.red(bgColor),Color.green(bgColor),Color.blue(bgColor));
        Drawable pressedDrawable=getRoundRectShapeDrawable(pressedColor,radius);
        Drawable defaultDrawable=getRoundRectShapeDrawable(bgColor,radius);
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{attr.state_pressed},pressedDrawable);
        stateListDrawable.addState(new int[]{},defaultDrawable);
        return stateListDrawable;
    }
    
    public static ColorStateList getColorStateList(int normalColor)
    {
        int pressedColor=Color.argb(128,Color.red(normalColor),Color.green(normalColor),Color.blue(normalColor));
        return getColorStateList(normalColor,pressedColor,normalColor);
    }
    
    public static ColorStateList getColorStateList(int normal,int pressed,int disable)
    {
        ColorStateList myColorStateList=new ColorStateList(new int[][]{new int[]{attr.state_pressed}, // 1
        new int[]{attr.state_focused},new int[]{attr.state_enabled},new int[]{-attr.state_enabled}},new int[]{pressed,pressed,normal,disable});
        return myColorStateList;
    }
    
    public static Drawable getRoundRectShapeBackgroundDrawable(int defaultColorRes,int pressColorRes,int r)
    {
        StateListDrawable drawable=new StateListDrawable();
        Drawable pressedDrawable=getRoundRectShapeDrawable(r,pressColorRes);
        drawable.addState(new int[]{attr.state_pressed},pressedDrawable);
        Drawable defaultDrawable=getRoundRectShapeDrawable(r,defaultColorRes);
        drawable.addState(new int[]{},defaultDrawable);
        return drawable;
    }
    
    /** 获取被染色过的图标.填充的颜色会随日夜间变换.常用于图标 */
    public static Drawable getDyeDrawableByResId(int resId,@ColorRes int colorResId)
    {
        Drawable drawable=ResHelper.getDrawable(resId);
        if(drawable!=null)
        {
            drawable.setColorFilter(new LightingColorFilter(0xFF000000,ResHelper.getColor(colorResId)));
        }
        return drawable;
    }
    
    public static Drawable getDyeDrawable(int resId,@ColorInt int colorValue)
    {
        return deepTintDrawable(resId,colorValue);
    }
    
    /**
     * 如果不希望与其他资源共享染色状态.使用该方法
     *
     * @param resId
     * @param colorValue
     * @return
     */
    public static Drawable getDyeMutableDrawable(int resId,@ColorInt int colorValue)
    {
        Drawable drawable=ResHelper.getDrawable(resId);
        if(drawable!=null)
        {
            drawable=drawable.mutate();
            drawable.setColorFilter(new LightingColorFilter(0xFF000000,colorValue));
        }
        return drawable;
    }
    
    public static Drawable getDyeDrawable(Drawable drawable,@ColorInt int colorValue)
    {
        return deepTintDrawable(drawable,colorValue);
    }
    
    /**
     * @param color
     * 原始颜色
     * @param alpha
     * 0~255
     */
    public static ColorDrawable getAlphaColorDrawable(int color,int alpha)
    {
        int dst=Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));
        return new ColorDrawable(dst);
    }
    
    /**
     * 获取两种颜色之间的中间态
     *
     * @param srcColor
     * 初始颜色
     * @param desColor
     * 最终颜色
     * @param factor
     * [0,1]
     * @return
     */
    public static int getTemporalColor(int srcColor,int desColor,float factor)
    {
        int SA=(srcColor&0xFF000000)>>>24;
        int SR=(srcColor&0x00FF0000)>>>16;
        int SG=(srcColor&0x0000FF00)>>>8;
        int SB=(srcColor&0x000000FF);
        int DA=(desColor&0xFF000000)>>>24;
        int DR=(desColor&0x00FF0000)>>>16;
        int DG=(desColor&0x0000FF00)>>>8;
        int DB=(desColor&0x000000FF);
        int A=(int)(SA+(DA-SA)*factor);
        int R=(int)(SR+(DR-SR)*factor);
        int G=(int)(SG+(DG-SG)*factor);
        int B=(int)(SB+(DB-SB)*factor);
        int temporalColor=A<<24|R<<16|G<<8|B;
        return temporalColor;
    }
    
    public static int[] getDrawableSize(int resId)
    {
        int[] ret;
        Drawable d=ResHelper.getDrawable(resId);
        if(null==d)
        {
            ret=new int[]{0,0};
        }
        else
        {
            ret=new int[]{d.getIntrinsicWidth(),d.getIntrinsicHeight()};
        }
        return ret;
    }
    
    public static ShapeDrawable getRoundRectStokeShapeDrawable(int radius,int color,int stokeWidth)
    {
        float[] outerR={radius,radius,radius,radius,radius,radius,radius,radius};
        RoundRectShape roundRectShape=new RoundRectShape(outerR,null,null);
        ShapeDrawable bgDrawable=new ShapeDrawable();
        bgDrawable.setShape(roundRectShape);
        bgDrawable.getPaint().setColor(color);
        bgDrawable.getPaint().setStyle(Style.STROKE);
        bgDrawable.getPaint().setStrokeWidth(stokeWidth);
        return bgDrawable;
    }
    
    public static ShapeDrawable getShapeDrawable(int colorResId,float aRInDp)
    {
        float r=ResHelper.dpToPxF(aRInDp);
        float[] outerR={r,r,r,r,r,r,r,r};
        ShapeDrawable drawable=new ShapeDrawable(new RoundRectShape(outerR,null,null));
        drawable.getPaint().setColor(ResHelper.getColor(colorResId));
        return drawable;
    }
    
    public static int getGradientColor(int endColor,int startColor,float progress)
    {
        float red=Color.red(endColor)*(1-progress)+Color.red(startColor)*progress;
        float green=Color.green(endColor)*(1-progress)+Color.green(startColor)*progress;
        float blue=Color.blue(endColor)*(1-progress)+Color.blue(startColor)*progress;
        float alpha=Color.alpha(endColor)*(1-progress)+Color.alpha(startColor)*progress;
        return Color.argb((int)alpha,(int)red,(int)green,(int)blue);
    }
    
    public static ShadowDrawable getShadowDrawable(int bgColor,int shapeRadius,int shadowColor,int shadowRadius,int offsetX,int offsetY)
    {
        ShadowDrawable drawable=new Builder().setBgColor(bgColor).setShapeRadius(shapeRadius).setShadowColor(shadowColor).setShadowRadius(shadowRadius).setOffsetX(offsetX).setOffsetY(offsetY).builder();
        return drawable;
    }
    
    /**
     * 修改资源大小
     *
     * @param originDrawable
     * 源Drawable
     * @param newWidth
     * 目标宽度
     * @param newHeight
     * 目标高度
     * @return
     */
    public static Drawable resizeBitmapDrawable(Drawable originDrawable,int newWidth,int newHeight)
    {
        AssertUtil.mustOk(originDrawable instanceof BitmapDrawable);
        Drawable newDrawable=originDrawable;
        if(originDrawable instanceof BitmapDrawable)
        {
            int originWidth=originDrawable.getIntrinsicWidth();
            int originHeight=originDrawable.getIntrinsicHeight();
            Bitmap originBitmap=((BitmapDrawable)originDrawable).getBitmap();
            if(null!=originBitmap&&!originBitmap.isRecycled()&&originWidth>0&&originHeight>0)
            {
                Matrix matrix=new Matrix();
                float scaleWidth=((float)newWidth/originWidth);
                float scaleHeight=((float)newHeight/originHeight);
                matrix.postScale(scaleWidth,scaleHeight);
                Bitmap newbmp=Bitmap.createBitmap(originBitmap,0,0,originBitmap.getWidth(),originBitmap.getHeight(),matrix,true);
                newDrawable=new BitmapDrawable(null,newbmp);
                ((BitmapDrawable)newDrawable).setTargetDensity(newbmp.getDensity());
            }
        }
        return newDrawable;
    }
    
    public static Drawable deepTintDrawable(Drawable drawable,@ColorInt int color)
    {
        Drawable wrappedDrawable=null;
        if(drawable!=null)
        {
            wrappedDrawable=DrawableCompat.wrap(drawable).mutate();
            DrawableCompat.setTintList(wrappedDrawable,ColorStateList.valueOf(color));
        }
        return wrappedDrawable;
    }
    
    public static Drawable deepTintDrawable(int drawableId,@ColorInt int color)
    {
        return deepTintDrawable(ResHelper.getDrawable(drawableId),color);
    }
}
