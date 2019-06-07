package com.fb.jjyyzjy.lib.focus.animator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.fb.jjyyzjy.lib.focus.BaseFocusView;

/**
 * 焦点View
 * Created by zjy on 2016/9/27.
 */
public class AnimatorFocusView extends View implements BaseFocusView {
    private static String TAG = "AnimatorFocusView";

    private AnimatorExecutor animatorExecutor;

    public AnimatorFocusView(Context context) {
        super(context);
        init();
    }

    public AnimatorFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatorFocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnimatorFocusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        animatorExecutor = new AnimatorExecutor(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(100,100);
        this.setLayoutParams(layoutParams);
    }

    int currentFocusImageRes = -1;
    int imageRes;
    int imageResTow;
    boolean isMoveHideFocus;
    Rect rectBitmap;
    Rect rectBitmapMain;
    Rect rectBitmapTwo;


    @Override
    public void initFocusBitmapRes(int imageRes) {
        this.initFocusBitmapRes(imageRes,imageRes);
    }

    public void initFocusBitmapRes(int imageRes, boolean isMoveHideFocus){
        this.initFocusBitmapRes(imageRes,imageRes,isMoveHideFocus);
    }

    public void initFocusBitmapRes(int imageRes,int imageResTwo, boolean isMoveHideFocus){
        this.isMoveHideFocus = isMoveHideFocus;
        if(isMoveHideFocus){
            hideFocus();
        }
        this.initFocusBitmapRes(imageRes,imageResTwo);
    }

    @Override
    public void initFocusBitmapRes(int imageRes, int imageResTwo) {
        this.imageRes = imageRes;
        this.imageResTow = imageResTwo;

        this.rectBitmapMain = new Rect();
        Drawable drawableMain = this.getResources().getDrawable(imageRes);
        if(drawableMain != null) {
            drawableMain.getPadding(this.rectBitmapMain);
        }

        if (imageRes != imageResTwo){
            this.rectBitmapTwo = new Rect();
            Drawable drawableTwo = this.getResources().getDrawable(imageResTwo);
            if(drawableTwo != null) {
                drawableTwo.getPadding(this.rectBitmapTwo);
            }
        }else {
            this.rectBitmapTwo = rectBitmapMain;
        }

        rectBitmap = rectBitmapMain;
        currentFocusImageRes = imageRes;
        setBackgroundResource(currentFocusImageRes);
    }

    @Override
    public void setBitmapForTop() {
        animatorExecutor.changeFocusRect(rectBitmap,rectBitmapTwo);
        rectBitmap = rectBitmapTwo;
        currentFocusImageRes = imageResTow;
        setBackgroundResource(currentFocusImageRes);
    }

    @Override
    public void clearBitmapForTop() {
        animatorExecutor.changeFocusRect(rectBitmap,rectBitmapMain);
        rectBitmap = rectBitmapMain;
        currentFocusImageRes = imageRes;
        setBackgroundResource(currentFocusImageRes);
    }

    @Override
    public void setFocusBitmap(int resId) {
        try {
            Rect rectBitmapNew = new Rect();
            Drawable drawableNew = getResources().getDrawable(resId);
            if (drawableNew != null) {
                drawableNew.getPadding(rectBitmapNew);
            }
            animatorExecutor.changeFocusRect(rectBitmap,rectBitmapNew);
            rectBitmap = rectBitmapNew;
        }catch (Exception e){
            throw new RuntimeException("resId == null ?");
        }
        currentFocusImageRes = resId;
        setBackgroundResource(currentFocusImageRes);
    }

    @Override
    public void setFocusLayout(float l, float t, float r, float b) {
        float ll = l - rectBitmap.left;
        float tt = t - rectBitmap.top;
        float rr = r + rectBitmap.right;
        float bb = b + rectBitmap.bottom;
        animatorExecutor.layout(ll,tt,(int)(rr-ll),(int)(bb-tt));
    }

    @Override
    public void focusMove(float l, float t, float r, float b) {
        float ll = l - rectBitmap.left;
        float tt = t - rectBitmap.top;
        float rr = r + rectBitmap.right;
        float bb = b + rectBitmap.bottom;
        animatorExecutor.move(ll,tt,(int)(rr-ll),(int)(bb-tt));
    }

    @Override
    public void scrollerFocusX(float scrollerX) {
        animatorExecutor.moveX(scrollerX);
    }

    @Override
    public void scrollerFocusY(float scrollerY) {
        animatorExecutor.moveY(scrollerY);
    }

    @Override
    public void hideFocus() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void showFocus() {
        setVisibility(VISIBLE);
    }

    /**
     * 设置焦点滑动速度，永久改变
     * @param movingNumberDefault   绘制次数 80 （该实现中无效）
     * @param movingVelocityDefault 执行时间 250
     */
    @Override
    public void setMoveVelocity(int movingNumberDefault, int movingVelocityDefault) {
        animatorExecutor.setAnimatorDuration(movingVelocityDefault);
    }

    /**
     * 设置焦点滑动速度，临时改变(临时，只针对接下来的一次移动)
     * @param movingNumberTemporary   绘制次数 80（该实现中无效）
     * @param movingVelocityTemporary 执行时间 250
     */
    @Override
    public void setMoveVelocityTemporary(int movingNumberTemporary, int movingVelocityTemporary) {
        animatorExecutor.setAnimatorDurationTemporary(movingVelocityTemporary);
    }

    @Override
    public void setOnFocusMoveEndListener(OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView) {
        animatorExecutor.setOnFocusMoveEndListener(onFocusMoveEndListener,changeFocusView);
    }

    /**
     * 为属性动画提供，改变宽度
     * @param width
     */
    public void setWidth(int width) {
//        DebugLog.e("AnimatorFocusView==setWidth");
        if (getLayoutParams().width != width) {
//            DebugLog.e("AnimatorFocusView==setWidth=="+width);
            getLayoutParams().width = width;
            requestLayout();
        }
    }

    /**
     * 为属性动画提供，改变高度
     * @param height
     */
    public void setHeight(int height) {
//        DebugLog.e("AnimatorFocusView==setHeight==");
        if (getLayoutParams().height != height) {
//            DebugLog.e("AnimatorFocusView==setHeight=="+height);
            getLayoutParams().height = height;
            requestLayout();
        }
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
//        DrawableCompat.setTintList(wrappedDrawable, colors);
        wrappedDrawable.setAlpha(102);
        return wrappedDrawable;
    }

    /**
     * 焦点框进入非活跃状态
     */
    @Override
    public void pause(){
        final Drawable focusDrawable = getBackground();
        if (focusDrawable != null)
            setBackgroundDrawable(tintDrawable(focusDrawable, ColorStateList.valueOf(Color.GRAY)));
    }

    /**
     * 焦点框进入活跃状态
     */
    @Override
    public void resume() {
        if (currentFocusImageRes != -1)
            setBackground(getResources().getDrawable(currentFocusImageRes));
    }

}
