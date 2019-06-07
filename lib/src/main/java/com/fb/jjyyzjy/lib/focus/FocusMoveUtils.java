package com.fb.jjyyzjy.lib.focus;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 滑动焦点工具类
 * Created by zjy on 2016/10/18.
 */
public class FocusMoveUtils {

    /**
     * 获取父view在执行属性动画缩放时view的坐标
     * @param view 指定的view
     * @param viewP 指定view的父
     * @return
     */
    public static RectF getViewLocation(View view,View viewP){
        return getViewLocation(null,view,viewP,null,false,1.0f,0.0f,0.0f);
    }

    public static RectF getViewLocation(View view,View viewP,boolean isScalable, float scale){
        return getViewLocation(null,view,viewP,null,isScalable,scale,0.0f,0.0f);
    }

    public static RectF getViewLocation(View view,View viewP,boolean isScalable, float scale, float scrollerX, float scrollerY){
        return getViewLocation(null,view,viewP,null,isScalable,scale,scrollerX,scrollerY);
    }

    /**
     * 获取焦点坐标
     * @param root 视图根布局，焦点在dialog等窗口上使用需要
     * @param view 需要焦点框的view
     * @param viewP 真实获取焦点的view，需要焦点框的是该view的孩子且该view有执行属性动画缩放时需要
     * @param clipRect 微调焦点框需要
     * @param isScalable 焦点是否执行了放大效果
     * @param scale 焦点放大系数
     * @param scrollerX 焦点框向X轴偏移
     * @param scrollerY 焦点框向Y轴偏移
     * @return
     */
    public static RectF getViewLocation(ViewGroup root, View view, View viewP, RectF clipRect, boolean isScalable, float scale, float scrollerX, float scrollerY){

        final RectF rect = new RectF();
        if(view != null) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            if (location[0] < 0 ) location[0]--;
            if (location[1] < 0 ) location[1]--;
            int[] offset = new int[2];
            if (root != null){
                root.getLocationInWindow(offset);
                if (offset[0] < 0 ) offset[0]--;
                if (offset[1] < 0 ) offset[1]--;
            }
            final int vWidth = view.getWidth();
            final int vHeight = view.getHeight();
            final float vScaleX = viewP == null ? view.getScaleX() : viewP.getScaleX();
            final float vScaleY = viewP == null ? view.getScaleY() : viewP.getScaleY();
//            Log.e("FocusMoveUtils","vScaleX=="+vScaleX+ "vScaleY=="+vScaleY);
            int left = location[0] - offset[0];
            int top = location[1] - offset[1];
//            Log.e("FocusMoveUtils","left=="+left+ "top=="+top);
            if(isScalable) {
                PointF pointF;
                if (viewP != null){
                    int[] locationP = new int[2];
                    viewP.getLocationInWindow(locationP);
                    if (viewP.getPivotX() == 0 && viewP.getPivotY() == 0){
                        viewP.setPivotX(viewP.getHeight() * 0.5F);
                        viewP.setPivotY(viewP.getHeight() * 0.5F);
                    }
                    //解决放大中心点不是view中心产生焦点框位置有偏差问题
                    if (vScaleX != 1.0F || vScaleY != 1.0F){
                        //解决执行属性缩放过程中获取坐标不准问题
                        pointF = scaleByPoint(left, top, locationP[0] - offset[0] + viewP.getPivotX(), locationP[1] - offset[1] + viewP.getPivotY(), 2.0F - vScaleX, 2.0F - vScaleY);
                        left = (int)(pointF.x + 0.5F);
                        top =  ((int)(pointF.y + 0.5F));
                    }
                    pointF = scaleByPoint(left, top, locationP[0] - offset[0] + viewP.getPivotX(), locationP[1] - offset[1] + viewP.getPivotY(), scale, scale);
                }else {
                    if (view.getPivotX() == 0 && view.getPivotY() == 0){
                        view.setPivotX(view.getHeight() * 0.5F);
                        view.setPivotY(view.getHeight() * 0.5F);
                    }
                    pointF = scaleByPoint(left, top, left + view.getPivotX(), top + view.getPivotY(), scale - vScaleX + 1.0F, scale - vScaleY + 1.0F);
                }
                rect.left = pointF.x + scrollerX;
                rect.top = pointF.y + scrollerY;
                rect.right = rect.left + vWidth * scale;
                rect.bottom = rect.top + vHeight * scale;
            } else {
                rect.left = left + scrollerX;
                rect.top = top + scrollerY;
                rect.right = rect.left + vWidth;
                rect.bottom = rect.top + vHeight;
            }
            if(clipRect != null) {
                rect.left += clipRect.left;
                rect.top += clipRect.top ;
                rect.right -= clipRect.right;
                rect.bottom -= clipRect.bottom ;
            }
            //转int损失的精度
            rect.left += 0.5F;
            rect.top += 0.5F;
        }
//        Log.e("FocusMoveUtils","RectF.Left=="+rect.left + "RectF.Top=="+rect.top + "RectF.Right=="+rect.right + "RectF.Bottom=="+rect.bottom);
        return rect;
    }

    /**
     * 一个坐标点，以某个点为缩放中心，缩放指定倍数，求这个坐标点在缩放后的新坐标值。
     * @param targetPointX 坐标点的X
     * @param targetPointY 坐标点的Y
     * @param scaleCenterX 缩放中心的X
     * @param scaleCenterY 缩放中心的Y
     * @param scaleX 缩X轴放倍数
     * @param scaleY 缩Y轴放倍数
     * @return 坐标点的新坐标
     */
    private static PointF scaleByPoint(float targetPointX, float targetPointY, float scaleCenterX, float scaleCenterY, float scaleX, float scaleY){
//        Log.e("FocusMoveUtils","targetPointX=="+targetPointX+"==targetPointY=="+targetPointY+"==scaleCenterX=="+scaleCenterX+"==scaleCenterY=="+scaleCenterY+"==scaleX=="+scaleX+"==scaleY=="+scaleY);
        Matrix matrix = new Matrix();
        // 将Matrix移到到当前圆所在的位置，
        // 然后再以某个点为中心进行缩放
        matrix.preTranslate(targetPointX,targetPointY);
        matrix.postScale(scaleX,scaleY,scaleCenterX,scaleCenterY);
        float[] values = new float[9];
        matrix.getValues(values);
        return new PointF(values[Matrix.MTRANS_X],values[Matrix.MTRANS_Y]);
    }

    public static ViewGroup getActivityRoot(Activity activity) {
        ViewGroup root = null;
        try {
            root = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        }catch (Exception e){
            Log.e("FocusMoveUtils","Activity getRoot Err");
        }
        return root;
    }

}
