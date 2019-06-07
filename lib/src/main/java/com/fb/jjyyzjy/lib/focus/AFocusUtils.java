package com.fb.jjyyzjy.lib.focus;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by zjy on 2016/9/27.
 */
public abstract class AFocusUtils {
    public AFocusUtils(Context context, View actLayout, int bitmapRes) {}

    public AFocusUtils(Context context, View actLayout, int bitmapRes, boolean isInitMoveHideFocus) {}

    public AFocusUtils(Context context, View actLayout, int bitmapRes, int bitmapResTwo) {}

    public AFocusUtils(Context context, View actLayout, int bitmapRes, int bitmapResTwo, boolean isInitMoveHideFocus) {}

    /**
     * 初始化焦点
     *
     * @param actLayout
     */
    public abstract void initFocusView(View actLayout, int bitmapRes, int bitmapResTwo, boolean isInitMoveHideFocus);

    /**
     * 设置焦点位置，如果焦点隐藏则显示焦点
     *
     * @param l 左
     * @param t 上
     * @param r 右
     * @param b 下
     */
    public abstract void setFocusLayout(float l, float t, float r, float b);

    /**
     * 设置焦点位置，如果焦点隐藏则显示焦点
     *
     * @param view       焦点位置参考控件
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public abstract void setFocusLayout(View view, boolean isScalable, float scale);

    /**
     * 设置焦点位置，如果焦点隐藏则显示焦点
     *
     * @param view       焦点位置参考控件
     * @param clipRect   焦点位置偏移数据
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public abstract void setFocusLayout(View view, Rect clipRect, boolean isScalable, float scale);

    /**
     * 移动焦点
     *
     * @param l 左
     * @param t 上
     * @param r 右
     * @param b 下
     */
    public abstract void startMoveFocus(float l, float t, float r, float b);

    /**
     * 移动焦点
     *
     * @param view       焦点位置参考控件
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public abstract void startMoveFocus(View view, boolean isScalable, float scale);

    /**
     * 移动焦点
     *
     * @param view       焦点位置参考控件
     * @param clipRect   焦点位置偏移数据
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public abstract void startMoveFocus(View view, Rect clipRect, boolean isScalable, float scale);

    public abstract void startMoveFocus(View view, boolean isScalable, float scale, float scrollerX, float scrollerY);

    public abstract void startMoveFocus(View view, Rect clipRect, boolean isScalable, float scale, float scrollerX, float scrollerY);

    /**
     * 设置X轴偏移量，焦点移动过程中使用
     *
     * @param scrollerX
     */
    public abstract void scrollerFocusX(float scrollerX);

    /**
     * 设置Y轴偏移量，焦点移动过程中使用
     *
     * @param scrollerY
     */
    public abstract void scrollerFocusY(float scrollerY);

    /**
     * 翻页过程中调用隐藏焦点,一段指定时间后再次显示
     *
     * @param delayMillis 指定时间后显示焦点
     */
    public abstract void hideFocusForStartMove(long delayMillis);

    /**
     * 隐藏焦点
     */
    public abstract void hideFocus();

    /**
     * 显示焦点
     */
    public abstract void showFocus();

    /**
     * 改变焦点图为头部控件效果
     */
    public abstract void changeBitmapForTopView();

    /**
     * 还原焦点图为默认
     */
    public abstract void clearBitMapForTopView();

    /**
     * 设置焦点图片（9patch图）
     *
     * @param resId 图片资源id
     */
    public abstract void setFocusBitmap(int resId);

    /**
     * 设置焦点滑动速度
     *
     * @param movingNumberDefault   绘制次数 80
     * @param movingVelocityDefault 绘制速度 3
     */
    public abstract void setMoveVelocity(int movingNumberDefault, int movingVelocityDefault);

    /**
     * 设置焦点滑动速度(临时，只针对接下来的一次移动)
     *
     * @param movingNumberTemporary   绘制次数 80
     * @param movingVelocityTemporary 绘制速度 3
     */
    public abstract void setMoveVelocityTemporary(int movingNumberTemporary, int movingVelocityTemporary);

    /**
     * 设置焦点移动完成监听
     *
     * @param onFocusMoveEndListener
     */
    public abstract void setOnFocusMoveEndListener(BaseFocusView.OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView);


    public abstract View getFocusView();

    public abstract void pause();

    public abstract void resume();
}
