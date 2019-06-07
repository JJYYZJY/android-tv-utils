package com.fb.jjyyzjy.lib.focus.animator;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.fb.jjyyzjy.lib.focus.AFocusUtils;
import com.fb.jjyyzjy.lib.focus.BaseFocusView;

/**
 * Created by zjy on 2016/9/27.
 */
public class AnimatorFocusUtils extends AFocusUtils {
    public AnimatorFocusUtils(Context context, View actLayout, int bitmapRes) {
        this(context, actLayout, bitmapRes,bitmapRes);
    }

    public AnimatorFocusUtils(Context context, View actLayout, int bitmapRes, boolean isInitMoveHideFocus) {
        this(context, actLayout, bitmapRes, bitmapRes, isInitMoveHideFocus);
    }

    public AnimatorFocusUtils(Context context, View actLayout, int bitmapRes, int bitmapResTwo) {
        this(context, actLayout, bitmapRes, bitmapResTwo, true);
    }

    public AnimatorFocusUtils(Context context, View actLayout, int bitmapRes, int bitmapResTwo, boolean isInitMoveHideFocus) {
        super(context, actLayout, bitmapRes, bitmapResTwo, isInitMoveHideFocus);
        this.context = context;
        initFocusView(actLayout, bitmapRes, bitmapResTwo, isInitMoveHideFocus);
    }

    private ViewGroup root;
    private Context context;
    private AnimatorFocusView focusView;

    @Override
    public void initFocusView(View actLayout, int bitmapRes, int bitmapResTwo, boolean isInitMoveHideFocus) {
        focusView = new AnimatorFocusView(context);
        focusView.initFocusBitmapRes(bitmapRes, bitmapResTwo, isInitMoveHideFocus);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        focusView.setFocusable(false);
        focusView.setClickable(false);
        if (actLayout instanceof ViewGroup) {
            this.root = (ViewGroup) actLayout;
            root.addView(focusView);
        }
    }

    /**
     * 设置焦点位置，如果焦点隐藏则显示焦点
     *
     * @param l 左
     * @param t 上
     * @param r 右
     * @param b 下
     */
    public void setFocusLayout(float l, float t, float r, float b) {
        focusView.setFocusLayout(l, t, r, b);
    }

    /**
     * 设置焦点位置，如果焦点隐藏则显示焦点
     *
     * @param view       焦点位置参考控件
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public void setFocusLayout(View view, boolean isScalable, float scale) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (isScalable) {
            float pL = (view.getWidth() * scale - view.getWidth()) / 2;
            float pT = (view.getHeight() * scale - view.getHeight()) / 2;
            focusView.setFocusLayout((int) (location[0] - pL), (int) (location[1] - pT),
                    (int) (location[0] + view.getWidth() + pL), (int) (location[1] + view.getHeight() + pT));
        } else {
            focusView.setFocusLayout(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
        }
    }

    /**
     * 设置焦点位置，如果焦点隐藏则显示焦点
     *
     * @param view       焦点位置参考控件
     * @param clipRect   焦点位置偏移数据
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public void setFocusLayout(View view, Rect clipRect, boolean isScalable, float scale) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (isScalable) {
            float pL = (view.getWidth() * scale - view.getWidth()) / 2;
            float pT = (view.getHeight() * scale - view.getHeight()) / 2;
            focusView.setFocusLayout((int) (location[0] - pL + clipRect.left), (int) (location[1] - pT + clipRect.top),
                    (int) (location[0] + view.getWidth() + pL - clipRect.right), (int) (location[1] + view.getHeight() + pT - clipRect.bottom));
        } else {
            focusView.setFocusLayout(location[0] + clipRect.left
                    , location[1] + clipRect.top
                    , location[0] + view.getWidth() - clipRect.right
                    , location[1] + view.getHeight() - clipRect.bottom);
        }
    }

    /**
     * 移动焦点
     *
     * @param l 左
     * @param t 上
     * @param r 右
     * @param b 下
     */
    public void startMoveFocus(float l, float t, float r, float b) {
        focusView.focusMove(l, t, r, b);
    }

    /**
     * 移动焦点
     *
     * @param view       焦点位置参考控件
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public void startMoveFocus(View view, boolean isScalable, float scale) {

        startMoveFocus(view, null, isScalable, scale, 0, 0);

    }

    /**
     * 移动焦点
     *
     * @param view       焦点位置参考控件
     * @param clipRect   焦点位置偏移数据
     * @param isScalable 是否缩放
     * @param scale      缩放比例
     */
    public void startMoveFocus(View view, Rect clipRect, boolean isScalable, float scale) {

        startMoveFocus(view, clipRect, isScalable, scale, 0, 0);

    }

    public void startMoveFocus(View view, boolean isScalable, float scale, float scrollerX, float scrollerY) {

        startMoveFocus(view, null, isScalable, scale, scrollerX, scrollerY);
    }

    public void startMoveFocus(View view, Rect clipRect, boolean isScalable, float scale, float scrollerX, float scrollerY) {

        if (view == null) {
            return;
        }

        /**
         * 选中控件位置
         */
        int[] location = new int[2];
        view.getLocationInWindow(location);

        /**
         * 窗口位置，解决窗口布局
         */
        int[] offset = new int[2];
        root.getLocationInWindow(offset);

        /**
         * 放大的偏移量，解决view执行属性动画缩放产生的位置偏差
         * 注：当前计算都是基于以view.getPivot为默认中心点情况下进行，暂不支持其他情况，有需求再加
         */
        int vWidth = view.getWidth();
        float xScaleOff = (vWidth * view.getScaleX() - vWidth)/2;
        int vHeight = view.getHeight();
        float yScaleOff = (vHeight * view.getScaleY() - vHeight)/2;

        int left = (int) (location[0] - offset[0] + xScaleOff + 0.49f);
        int top = (int) (location[1] - offset[1] + yScaleOff + 0.49f);

        if (clipRect == null) {
            if (isScalable) {
                float pL = (view.getWidth() * scale - view.getWidth()) / 2;
                float pT = (view.getHeight() * scale - view.getHeight()) / 2;

                focusView.focusMove((int) (left - pL + scrollerX),
                        (int) (top - pT + scrollerY),
                        (int) (left + view.getWidth() + pL + scrollerX),
                        (int) (top + view.getHeight() + pT + scrollerY));
            } else {
                focusView.focusMove(left + scrollerX,
                        top + scrollerY,
                        left + view.getWidth() + scrollerX,
                        top + view.getHeight() + scrollerY);
            }
        } else {
            if (isScalable) {
                float pL = (view.getWidth() * scale - view.getWidth()) / 2;
                float pT = (view.getHeight() * scale - view.getHeight()) / 2;
                focusView.focusMove((int) (left - pL + clipRect.left + scrollerX),
                        (int) (top - pT + clipRect.top + scrollerY),
                        (int) (left + view.getWidth() + pL - clipRect.right + scrollerX),
                        (int) (top + view.getHeight() + pT - clipRect.bottom + scrollerY));
            } else {
                focusView.focusMove(left + clipRect.left + scrollerX,
                        top + clipRect.top + scrollerY,
                        left + view.getWidth() - clipRect.right + scrollerX,
                        top + view.getHeight() - clipRect.bottom + scrollerY);
            }
        }

    }

    /**
     * 设置X轴偏移量，焦点移动过程中使用
     *
     * @param scrollerX
     */
    public void scrollerFocusX(float scrollerX) {

        focusView.scrollerFocusX(scrollerX);
    }

    /**
     * 设置Y轴偏移量，焦点移动过程中使用
     *
     * @param scrollerY
     */
    public void scrollerFocusY(float scrollerY) {

        focusView.scrollerFocusY(scrollerY);
    }

    /**
     * 翻页过程中调用隐藏焦点,一段指定时间后再次显示
     *
     * @param delayMillis 指定时间后显示焦点
     */
    public void hideFocusForStartMove(long delayMillis) {
        focusView.hideFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                focusView.showFocus();
            }
        }, delayMillis);
    }

    /**
     * 隐藏焦点
     */
    public void hideFocus() {
        focusView.hideFocus();
    }

    /**
     * 显示焦点
     */
    public void showFocus() {
        focusView.showFocus();
    }

    /**
     * 改变焦点图为头部控件效果
     */
    public void changeBitmapForTopView() {
        focusView.setBitmapForTop();
    }

    /**
     * 还原焦点图为默认
     */
    public void clearBitMapForTopView() {
        focusView.clearBitmapForTop();
    }

    /**
     * 设置焦点图片（9patch图）
     *
     * @param resId 图片资源id
     */
    public void setFocusBitmap(int resId) {
        focusView.setFocusBitmap(resId);
    }

    /**
     * 设置焦点滑动速度
     *
     * @param movingNumberDefault   绘制次数 80
     * @param movingVelocityDefault 绘制速度 3
     */
    public void setMoveVelocity(int movingNumberDefault, int movingVelocityDefault) {
        focusView.setMoveVelocity(movingNumberDefault, movingVelocityDefault);
    }

    /**
     * 设置焦点滑动速度(临时，只针对接下来的一次移动)
     *
     * @param movingNumberTemporary   绘制次数 80
     * @param movingVelocityTemporary 绘制速度 3
     */
    public void setMoveVelocityTemporary(int movingNumberTemporary, int movingVelocityTemporary) {
        focusView.setMoveVelocityTemporary(movingNumberTemporary, movingVelocityTemporary);
    }

    /**
     * 设置焦点移动完成监听
     *
     * @param onFocusMoveEndListener
     */
    public void setOnFocusMoveEndListener(BaseFocusView.OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView) {
        focusView.setOnFocusMoveEndListener(onFocusMoveEndListener, changeFocusView);
    }


    public View getFocusView() {
        return focusView;
    }

    @Override
    public void pause() {
        focusView.pause();
    }

    @Override
    public void resume() {
        focusView.resume();
    }
}
