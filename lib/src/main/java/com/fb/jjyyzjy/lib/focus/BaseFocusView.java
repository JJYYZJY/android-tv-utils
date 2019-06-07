package com.fb.jjyyzjy.lib.focus;

import android.view.View;

/**
 * Created by ZJY on 2016/6/21.
 */
public interface BaseFocusView {

        /**
         * 初始化焦点图片
         * @param imageRes
         */
        public void initFocusBitmapRes(int imageRes);

        /**
         * 初始化焦点图片
         * @param imageRes
         * @param imageResTwo
         */
        public void initFocusBitmapRes(int imageRes, int imageResTwo);

        public void setBitmapForTop();

        public void clearBitmapForTop();

        /**
         * 设置焦点框位置
         * @param l
         * @param t
         * @param r
         * @param b
         */
        public void setFocusLayout(float l, float t, float r, float b);

        /**
         * 设置焦点框移动到的位置
         * @param l
         * @param t
         * @param r
         * @param b
         */
        public void focusMove(float l, float t, float r, float b);



        public void scrollerFocusX(float scrollerX);

        public void scrollerFocusY(float scrollerY);

        public void hideFocus();
        public void showFocus();

        /**
         * 设置焦点图(9patch图)
         * @param resId 资源id
         */
        public void setFocusBitmap(int resId);

        /**
         * 设置焦点滑动速度
         * @param movingNumberDefault 绘制次数 80
         * @param movingVelocityDefault 绘制速度 3
         */
        public void setMoveVelocity(int movingNumberDefault, int movingVelocityDefault);

        /**
         * 设置焦点滑动速度(临时，只针对接下来的一次移动)
         * @param movingNumberTemporary 绘制次数 80
         * @param movingVelocityTemporary 绘制速度 3
         */
        public void setMoveVelocityTemporary(int movingNumberTemporary, int movingVelocityTemporary);

        /**
         * 设置焦点移动结束监听
         * @param onFocusMoveEndListener 焦点移动结束监听
         * @param changeFocusView 与监听绑定的view
         */
        public void setOnFocusMoveEndListener(OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView);

        /**
         * 焦点移动结束监听接口
         */
        public interface OnFocusMoveEndListener{
            void focusEnd(View changeFocusView);
        }

        public void pause();

        public void resume();
}
