package com.townspriter.android.foundation.utils.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path Foundation:ShadowDrawable
 * @Describe
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 2021-05-05
 * CopyRight(C)2021 小镇精灵科技版权所有
 * *****************************************************************************
 */
public class ShadowDrawable extends Drawable
{
    private final Paint mShadowPaint;
    private final Paint mBgPaint;
    private final int mShadowRadius;
    private final int mShapeRadius;
    private final int mOffsetX;
    private final int mOffsetY;
    private final int mBgColor;
    private RectF mShadowRect;
    
    public ShadowDrawable(int bgColor,int shapeRadius,int shadowColor,int shadowRadius,int offsetX,int offsetY)
    {
        mBgColor=bgColor;
        mShapeRadius=shapeRadius;
        mShadowRadius=shadowRadius;
        mOffsetX=offsetX;
        mOffsetY=offsetY;
        mShadowPaint=new Paint();
        mShadowPaint.setColor(Color.TRANSPARENT);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(shadowRadius,offsetX,offsetY,shadowColor);
        mBgPaint=new Paint();
        mBgPaint.setAntiAlias(true);
    }
    
    @Override
    public void setBounds(int left,int top,int right,int bottom)
    {
        super.setBounds(left,top,right,bottom);
        mShadowRect=new RectF(left+mOffsetX+mShadowRadius,top+mOffsetY+mShadowRadius,right-mShadowRadius-mOffsetX,bottom-mShadowRadius-mOffsetY);
    }
    
    @Override
    public void draw(@NonNull Canvas canvas)
    {
        mBgPaint.setColor(mBgColor);
        canvas.drawRoundRect(mShadowRect,mShapeRadius,mShapeRadius,mShadowPaint);
        canvas.drawRoundRect(mShadowRect,mShapeRadius,mShapeRadius,mBgPaint);
    }
    
    @Override
    public void setAlpha(int alpha)
    {
        mShadowPaint.setAlpha(alpha);
    }
    
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter)
    {
        mShadowPaint.setColorFilter(colorFilter);
    }
    
    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }
    
    public static class Builder
    {
        private int mShapeRadius;
        private int mShadowColor;
        private int mShadowRadius;
        private int mOffsetX;
        private int mOffsetY;
        private int mBgColor;
        
        public Builder()
        {}
        
        public Builder setShapeRadius(int shapeRadius)
        {
            mShapeRadius=shapeRadius;
            return this;
        }
        
        public Builder setShadowColor(int shadowColor)
        {
            mShadowColor=shadowColor;
            return this;
        }
        
        public Builder setShadowRadius(int shadowRadius)
        {
            mShadowRadius=shadowRadius;
            return this;
        }
        
        public Builder setOffsetX(int offsetX)
        {
            mOffsetX=offsetX;
            return this;
        }
        
        public Builder setOffsetY(int offsetY)
        {
            mOffsetY=offsetY;
            return this;
        }
        
        public Builder setBgColor(int bgColor)
        {
            mBgColor=bgColor;
            return this;
        }
        
        public ShadowDrawable builder()
        {
            return new ShadowDrawable(mBgColor,mShapeRadius,mShadowColor,mShadowRadius,mOffsetX,mOffsetY);
        }
    }
}
