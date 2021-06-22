package com.townspriter.android.foundation.utils.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.system.SystemInfo;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/******************************************************************************
 * @path Foundation:ViewUtils
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:56:02
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public final class ViewUtils
{
    private static final String TAG="ViewUtils";
    private static final int RATIOxXY=2;
    /** 最小滑动距离优化.系统认为最小的滑动距离不一定精确.需要斟酌使用 */
    private static final float SLOPxFACTOR=0.45f;
    private static final float SLOPxFACTORxVERTICAL=0.3f;
    private static final float SLOPxFACTORxHORIZONTAL=0.3f;
    private static final String SCROLLBARxDRAWABLExSETxVERTICALxTHUMB="setVerticalThumbDrawable";
    private static final String SCROLLBARxDRAWABLExSETxVERTICALxTRACK="setVerticalTrackDrawable";
    private static final String SCROLLBARxDRAWABLExSETxHORIZONTALxTHUMB="setHorizontalThumbDrawable";
    private static final String SCROLLBARxDRAWABLExSETxHORIZONTALxTRACK="setHorizontalTrackDrawable";
    private static final AtomicInteger NEXTxGENERATEDxID=new AtomicInteger(1);
    
    private ViewUtils()
    {}
    
    public static boolean isDialogFragmentShow(@Nullable DialogFragment dialog)
    {
        return dialog!=null&&dialog.getDialog()!=null&&dialog.getDialog().isShowing();
    }
    
    public static void showKeyboard(@NonNull View view)
    {
        if(view!=null)
        {
            InputMethodManager inputManager=(InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputManager!=null)
            {
                inputManager.showSoftInput(view,0);
            }
            else
            {
                Logger.e(TAG,"showKeyboard-inputManager:NULL");
            }
        }
        else
        {
            Logger.e(TAG,"showKeyboard-view:NULL");
        }
    }
    
    public static void hideKeyboard(@NonNull View view)
    {
        if(view!=null)
        {
            InputMethodManager inputManager=(InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputManager!=null)
            {
                if(inputManager.isActive())
                {
                    inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else
                {
                    Logger.e(TAG,"hideKeyboard-inputManager.isActive():"+inputManager.isActive());
                }
            }
            else
            {
                Logger.e(TAG,"hideKeyboard-inputManager:NULL");
            }
        }
        else
        {
            Logger.e(TAG,"hideKeyboard-view:NULL");
        }
    }
    
    /**
     * getScrollOrientation
     * 根据横纵坐标轴位移量判断滑动方向
     *
     * @param context
     * 环境上下文
     * @param dx
     * 横坐标位移量
     * @param dy
     * 纵坐标位移量
     * @return Orientation
     */
    public static Orientation getScrollOrientation(@NonNull Context context,float dx,float dy)
    {
        int scaledTouchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
        scaledTouchSlop*=SLOPxFACTOR;
        if(Math.abs(dx)>scaledTouchSlop||Math.abs(dy)>scaledTouchSlop)
        {
            if(Math.abs(dy)>Math.abs(dx)*RATIOxXY)
            {
                // 纵向移动
                return dy>0?Orientation.BOTTOM:Orientation.TOP;
            }
            else
            {
                // 横向移动
                return dx>0?Orientation.RIGHT:Orientation.LEFT;
            }
        }
        return Orientation.NONE;
    }
    
    /**
     * getScrollOrientationVertical
     * 根据纵坐标轴位移量判断垂直滑动方向
     *
     * @param context
     * 环境上下文
     * @param dy
     * 纵坐标位移量
     * @return Orientation
     */
    public static Orientation getScrollOrientationVertical(@NonNull Context context,float dy)
    {
        int scaledTouchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
        scaledTouchSlop*=SLOPxFACTORxVERTICAL;
        if(Math.abs(dy)>scaledTouchSlop)
        {
            return dy>0?Orientation.BOTTOM:Orientation.TOP;
        }
        return Orientation.NONE;
    }
    
    /**
     * getScrollOrientationHorizontal
     * 根据横纵坐标轴位移量判断滑动方向
     *
     * @param context
     * 环境上下文
     * @param dx
     * 横坐标位移量
     * @return Orientation
     */
    public static Orientation getScrollOrientationHorizontal(@NonNull Context context,float dx)
    {
        int scaledTouchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
        scaledTouchSlop*=SLOPxFACTORxHORIZONTAL;
        if(Math.abs(dx)>scaledTouchSlop)
        {
            return dx>0?Orientation.RIGHT:Orientation.LEFT;
        }
        return Orientation.NONE;
    }
    
    /**
     * measureViewHeight
     *
     * @param view
     * 控件
     * @return 控件高度
     */
    public static float measureViewHeight(@NonNull View view)
    {
        int widthMeasureSpec=View.MeasureSpec.makeMeasureSpec(SystemInfo.INSTANCE.getDeviceWidth(null),View.MeasureSpec.AT_MOST);
        int heightMeasureSpec=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec,heightMeasureSpec);
        return view.getMeasuredHeight();
    }
    
    /**
     * containsPoints
     * 检测目标点是否在控件内
     *
     * @param view
     * 控件
     * @param x
     * 目标点
     * @param y
     * 目标点
     * @return 是否在控件内
     */
    public static boolean containsPoints(@NonNull View view,int x,int y)
    {
        boolean result;
        int[] location=new int[2];
        view.getLocationOnScreen(location);
        Rect rect=new Rect(location[0],location[1],location[0]+view.getWidth(),location[1]+view.getHeight());
        result=rect.contains(x,y);
        return result;
    }
    
    /**
     * containsPoints
     * 检测目标点是否在控件内
     *
     * @param rect
     * 控件
     * @param x
     * 目标点
     * @param y
     * 目标点
     * @return 是否在控件内
     */
    public static boolean containsPoints(@NonNull Rect rect,int x,int y)
    {
        return rect.contains(x,y);
    }
    
    public static float getAlpha(View view)
    {
        return Honeycomb.getAlpha(view);
    }
    
    public static void setAlpha(View view,float alpha)
    {
        Honeycomb.setAlpha(view,alpha);
    }
    
    public static float getPivotX(View view)
    {
        return Honeycomb.getPivotX(view);
    }
    
    public static void setPivotX(View view,float pivotX)
    {
        Honeycomb.setPivotX(view,pivotX);
    }
    
    public static float getPivotY(View view)
    {
        return Honeycomb.getPivotY(view);
    }
    
    public static void setPivotY(View view,float pivotY)
    {
        Honeycomb.setPivotY(view,pivotY);
    }
    
    public static float getRotation(View view)
    {
        return Honeycomb.getRotation(view);
    }
    
    public static void setRotation(View view,float rotation)
    {
        Honeycomb.setRotation(view,rotation);
    }
    
    public static float getRotationX(View view)
    {
        return Honeycomb.getRotationX(view);
    }
    
    public static void setRotationX(View view,float rotationX)
    {
        Honeycomb.setRotationX(view,rotationX);
    }
    
    public static float getRotationY(View view)
    {
        return Honeycomb.getRotationY(view);
    }
    
    public static void setRotationY(View view,float rotationY)
    {
        Honeycomb.setRotationY(view,rotationY);
    }
    
    public static float getScaleX(View view)
    {
        return Honeycomb.getScaleX(view);
    }
    
    public static void setScaleX(View view,float scaleX)
    {
        Honeycomb.setScaleX(view,scaleX);
    }
    
    public static float getScaleY(View view)
    {
        return Honeycomb.getScaleY(view);
    }
    
    public static void setScaleY(View view,float scaleY)
    {
        Honeycomb.setScaleY(view,scaleY);
    }
    
    public static float getScrollX(View view)
    {
        return Honeycomb.getScrollX(view);
    }
    
    public static void setScrollX(View view,int scrollX)
    {
        Honeycomb.setScrollX(view,scrollX);
    }
    
    public static float getScrollY(View view)
    {
        return Honeycomb.getScrollY(view);
    }
    
    public static void setScrollY(View view,int scrollY)
    {
        Honeycomb.setScrollY(view,scrollY);
    }
    
    public static float getTranslationX(View view)
    {
        return Honeycomb.getTranslationX(view);
    }
    
    public static void setTranslationX(View view,float translationX)
    {
        Honeycomb.setTranslationX(view,translationX);
    }
    
    public static float getTranslationY(View view)
    {
        return Honeycomb.getTranslationY(view);
    }
    
    public static void setTranslationY(View view,float translationY)
    {
        Honeycomb.setTranslationY(view,translationY);
    }
    
    public static float getX(View view)
    {
        return Honeycomb.getX(view);
    }
    
    public static void setX(View view,float x)
    {
        Honeycomb.setX(view,x);
    }
    
    public static float getY(View view)
    {
        return Honeycomb.getY(view);
    }
    
    public static void setY(View view,float y)
    {
        Honeycomb.setY(view,y);
    }
    
    public static Rect getViewScreenLocation(View view)
    {
        if(view==null)
        {
            return null;
        }
        int[] location=new int[2];
        view.getLocationOnScreen(location);
        return new Rect(location[0],location[1],location[0]+view.getWidth(),location[1]+view.getHeight());
    }
    
    /**
     * 4.2的系统不设置.可能造成resolveRtlPropertiesIfNeeded()调用次数过多卡顿
     * 
     * @see {@link View#setLayoutDirection(int)}
     * @param view
     * 需要设置的控件
     * @param layoutDirection
     * normally {@link View#LAYOUT_DIRECTION_LTR}
     */
    public static void setLayoutDirection(View view,int layoutDirection)
    {
        if(VERSION.SDK_INT>=VERSION_CODES.JELLY_BEAN_MR1)
        {
            view.setLayoutDirection(layoutDirection);
        }
    }
    
    public static boolean setScrollbarVerticalThumbDrawable(View view,Drawable newDrawable)
    {
        return setScrollbarDrawable(view,newDrawable,SCROLLBARxDRAWABLExSETxVERTICALxTHUMB);
    }
    
    public static boolean setScrollbarDrawable(View view,Drawable newDrawable,String methodName)
    {
        try
        {
            Object scrollBarDrawable=getScrollBarDrawableObject(view);
            if(scrollBarDrawable!=null)
            {
                Class<?> scrollBarDrawableClass=scrollBarDrawable.getClass();
                Method setVerticalThumbDrawableMethod=scrollBarDrawableClass.getMethod(methodName,Drawable.class);
                setVerticalThumbDrawableMethod.invoke(scrollBarDrawable,newDrawable);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
    }
    
    public static Object getScrollBarDrawableObject(View view)
    {
        try
        {
            Class<?> viewClass=View.class;
            Field viewScrollCacheField=viewClass.getDeclaredField("mScrollCache");
            viewScrollCacheField.setAccessible(true);
            Object scrollCache=viewScrollCacheField.get(view);
            if(scrollCache==null)
            {
                return null;
            }
            Class<?> scrollabilityCacheClass=scrollCache.getClass();
            Field scrollBarDrawableField=scrollabilityCacheClass.getDeclaredField("scrollBar");
            scrollBarDrawableField.setAccessible(true);
            Object scrollBarDrawable=scrollBarDrawableField.get(scrollCache);
            return scrollBarDrawable;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
    
    public static int computeVerticalScrollRange(View view) throws Exception
    {
        if(null!=view)
        {
            Class clazz=view.getClass();
            Method method=getClassMethod(clazz,"computeVerticalScrollRange");
            method.setAccessible(true);
            Object range=method.invoke(view);
            return (Integer)range;
        }
        else
        {
            return 0;
        }
    }
    
    public static int computeVerticalScrollExtent(View view) throws Exception
    {
        if(null!=view)
        {
            Class clazz=view.getClass();
            Method method=getClassMethod(clazz,"computeVerticalScrollExtent");
            Object range=method.invoke(view);
            return (Integer)range;
        }
        else
        {
            return 0;
        }
    }
    
    public static int computeVerticalScrollOffset(View view) throws Exception
    {
        if(null!=view)
        {
            Class clazz=view.getClass();
            Method method=getClassMethod(clazz,"computeVerticalScrollOffset");
            Object range=method.invoke(view);
            return (Integer)range;
        }
        else
        {
            return 0;
        }
    }
    
    private static Method getClassMethod(Class curClass,String methodName,Class<?>...parameterTypes)
    {
        Method method=null;
        try
        {
            method=curClass.getDeclaredMethod(methodName,parameterTypes);
        }
        catch(NoSuchMethodException ignored)
        {}
        if(method==null&&curClass.getSuperclass()!=null)
        {
            return getClassMethod(curClass.getSuperclass(),methodName,parameterTypes);
        }
        if(method!=null)
        {
            method.setAccessible(true);
        }
        return method;
    }
    
    public static void removeViewFromParent(View view)
    {
        if(null!=view)
        {
            if(view.getParent() instanceof ViewGroup)
            {
                ViewGroup parentViewGroup=(ViewGroup)view.getParent();
                parentViewGroup.removeView(view);
            }
        }
    }
    
    public static boolean addViewSafe(ViewGroup parent,View child)
    {
        boolean ret=false;
        if(null!=child)
        {
            removeViewFromParent(child);
            if(null!=parent)
            {
                parent.addView(child);
                ret=true;
            }
        }
        return ret;
    }
    
    public static boolean addViewSafe(ViewGroup parent,View child,int index)
    {
        boolean ret=false;
        if(null!=child)
        {
            removeViewFromParent(child);
            if(null!=parent)
            {
                parent.addView(child,index);
                ret=true;
            }
        }
        return ret;
    }
    
    /**
     * 扩展控件的可点击区域
     *
     * @param view
     * 目标控件
     * @param expandSize
     * 横向纵向扩展尺寸
     */
    public static void expandTouchZone(View view,int expandSize)
    {
        expandTouchZone(view,expandSize,expandSize);
    }
    
    /**
     * 扩展控件的可点击区域
     *
     * @param view
     * 目标控件
     * @param expandX
     * @param expandY
     * 横向纵向扩展尺寸
     */
    public static void expandTouchZone(View view,int expandX,int expandY)
    {
        if(view==null||view.getParent() instanceof ViewGroup)
        {
            return;
        }
        expandTouchZone(view,(ViewGroup)view.getParent(),expandX,expandY);
    }
    
    /**
     * 扩展控件的可点击区域
     *
     * @param view
     * 目标控件
     * @param delegateView
     * @param expandX
     * @param expandY
     * 横向纵向扩展尺寸
     */
    public static void expandTouchZone(View view,ViewGroup delegateView,int expandX,int expandY)
    {
        if(view==null||delegateView==null)
        {
            return;
        }
        Rect touchRect=new Rect();
        touchRect.set(view.getLeft(),view.getTop(),view.getRight(),view.getBottom());
        touchRect.inset(-expandX,-expandY);
        delegateView.setTouchDelegate(new TouchDelegate(touchRect,view));
    }
    
    private static int generateViewIdLowerApiLevel()
    {
        for(;;)
        {
            int result=NEXTxGENERATEDxID.get();
            int newValue=result+1;
            if(newValue>0x00FFFFFF)
            {
                newValue=1;
            }
            if(NEXTxGENERATEDxID.compareAndSet(result,newValue))
            {
                return result;
            }
        }
    }
    
    public static int generateViewId()
    {
        int viewId=-1;
        if(VERSION.SDK_INT<VERSION_CODES.JELLY_BEAN_MR1)
        {
            viewId=generateViewIdLowerApiLevel();
        }
        else
        {
            viewId=View.generateViewId();
        }
        return viewId;
    }
    
    @Nullable
    public static Activity getActivity(@NonNull View view)
    {
        Context context=view.getContext();
        return findActivity(context);
    }
    
    @Nullable
    public static Activity findActivity(@NonNull Context context)
    {
        while(context instanceof ContextWrapper)
        {
            if(context instanceof Activity)
            {
                return (Activity)context;
            }
            context=((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
    
    public enum Orientation
    {
        /**
         * LEFT 左侧滑动
         */
        LEFT,
        /**
         * TOP 上侧滑动
         */
        TOP,
        /**
         * RIGHT 右侧滑动
         */
        RIGHT,
        /**
         * BOTTOM 下侧滑动
         */
        BOTTOM,
        /**
         * NONE 没有滑动
         */
        NONE
    }
    private static final class Honeycomb
    {
        static float getAlpha(View view)
        {
            return view.getAlpha();
        }
        
        static void setAlpha(View view,float alpha)
        {
            view.setAlpha(alpha);
        }
        
        static float getPivotX(View view)
        {
            return view.getPivotX();
        }
        
        static void setPivotX(View view,float pivotX)
        {
            view.setPivotX(pivotX);
        }
        
        static float getPivotY(View view)
        {
            return view.getPivotY();
        }
        
        static void setPivotY(View view,float pivotY)
        {
            view.setPivotY(pivotY);
        }
        
        static float getRotation(View view)
        {
            return view.getRotation();
        }
        
        static void setRotation(View view,float rotation)
        {
            view.setRotation(rotation);
        }
        
        static float getRotationX(View view)
        {
            return view.getRotationX();
        }
        
        static void setRotationX(View view,float rotationX)
        {
            view.setRotationX(rotationX);
        }
        
        static float getRotationY(View view)
        {
            return view.getRotationY();
        }
        
        static void setRotationY(View view,float rotationY)
        {
            view.setRotationY(rotationY);
        }
        
        static float getScaleX(View view)
        {
            return view.getScaleX();
        }
        
        static void setScaleX(View view,float scaleX)
        {
            view.setScaleX(scaleX);
        }
        
        static float getScaleY(View view)
        {
            return view.getScaleY();
        }
        
        static void setScaleY(View view,float scaleY)
        {
            view.setScaleY(scaleY);
        }
        
        static float getScrollX(View view)
        {
            return view.getScrollX();
        }
        
        static void setScrollX(View view,int scrollX)
        {
            view.setScrollX(scrollX);
        }
        
        static float getScrollY(View view)
        {
            return view.getScrollY();
        }
        
        static void setScrollY(View view,int scrollY)
        {
            view.setScrollY(scrollY);
        }
        
        static float getTranslationX(View view)
        {
            return view.getTranslationX();
        }
        
        static void setTranslationX(View view,float translationX)
        {
            view.setTranslationX(translationX);
        }
        
        static float getTranslationY(View view)
        {
            return view.getTranslationY();
        }
        
        static void setTranslationY(View view,float translationY)
        {
            view.setTranslationY(translationY);
        }
        
        static float getX(View view)
        {
            return view.getX();
        }
        
        static void setX(View view,float x)
        {
            view.setX(x);
        }
        
        static float getY(View view)
        {
            return view.getY();
        }
        
        static void setY(View view,float y)
        {
            view.setY(y);
        }
    }
}
