package com.fb.jjyyzjy.lib.focus.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.fb.jjyyzjy.lib.focus.BaseFocusView;

/**
 * 焦点移动控制类
 * Created by zjy on 2016/9/27.
 */
public class AnimatorExecutor {

    private AnimatorFocusView focusView;
    protected long mAnimatorDuration;
    private int animatorDuration = 250;
    private int animatorDurationTemporary = -1;

    public AnimatorExecutor(AnimatorFocusView focusView){
        if (focusView == null){
            throw new NullPointerException("focusView not is null");
        }
        this.focusView = focusView;
    }

    public void layout(float l, float t, int w, int h) {
//        DebugLog.e("layout");
        this.fL = this.endL;
        this.fT = this.endT;
        this.fW = this.endW;
        this.fH = this.endH;
        this.endL = l;
        this.endT = t;
        this.endW = w;
        this.endH = h;
        if (animatorSet.isStarted()){
            animatorSet.cancel();
        }
        focusView.setX(endL);
        focusView.setY(endT);
        focusView.setWidth(endW);
        focusView.setHeight(endH);
    }

    public void move(float l, float t, int w, int h) {
//        DebugLog.e("move");
        this.fL = this.endL;
        this.fT = this.endT;
        this.fW = this.endW;
        this.fH = this.endH;
        this.endL = l;
        this.endT = t;
        this.endW = w;
        this.endH = h;
        startAnimator();
    }

    public void moveX(float scrollerX) {
//        DebugLog.e("moveX");
        if (scrollerX == 0){
            return;
        }
        this.endL += scrollerX;
        startAnimator();
    }

    public void moveY(float scrollerY) {
//        DebugLog.e("moveY");
        if (scrollerY == 0){
            return;
        }
        this.endT += scrollerY;
        startAnimator();
    }

    private float fL;
    private float fT;
    private int fW;
    private int fH;
    private float endL;
    private float endT;
    private int endW;
    private int endH;

    private ObjectAnimator transAnimatorX;
    private ObjectAnimator transAnimatorY;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;
    private AnimatorSet animatorSet;

    private FloatEvaluator floatEvaluator;
    private Interpolator decelerateInterpolator = new LinearInterpolator();

    {
        animatorSet = new AnimatorSet();
    }

    private void startAnimator(){
        animatorSet.cancel();
        if (transAnimatorX == null) {
            transAnimatorX = ObjectAnimator.ofFloat(focusView,
                    "x", fL, endL);
            transAnimatorY = ObjectAnimator.ofFloat(focusView,
                    "y", fT, endT);
            scaleX = ObjectAnimator.ofInt(focusView,
                    "width", fW, endW);
            scaleY = ObjectAnimator.ofInt(focusView,
                    "height", fH, endH);
            floatEvaluator = new FloatEvaluator();
            animatorSet.playTogether(transAnimatorX, transAnimatorY, scaleX, scaleY);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (focusView.isMoveHideFocus){
                        focusView.isMoveHideFocus = false;
                        focusView.showFocus();
                    }
                    if (onFocusMoveEndListener != null){
                        onFocusMoveEndListener.focusEnd(changeFocusView);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        } else {
            transAnimatorX.setEvaluator(floatEvaluator);
            transAnimatorY.setEvaluator(floatEvaluator);

            transAnimatorX.setFloatValues(fL, endL);
            transAnimatorY.setFloatValues(fT, endT);
            scaleX.setIntValues(fW, endW);
            scaleY.setIntValues(fH, endH);

        }

        animatorSet.setInterpolator(decelerateInterpolator);
        if (animatorDurationTemporary != -1){
            mAnimatorDuration = animatorDurationTemporary;
            animatorDurationTemporary = -1;
        }else {
            mAnimatorDuration = animatorDuration;
        }
        animatorSet.setDuration(this.mAnimatorDuration);
        animatorSet.start();
    }

    private BaseFocusView.OnFocusMoveEndListener onFocusMoveEndListener;//焦点移动结束监听
    private View changeFocusView;//与焦点移动结束监听绑定的view
    public void setOnFocusMoveEndListener(BaseFocusView.OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView) {
        this.onFocusMoveEndListener = onFocusMoveEndListener;
        this.changeFocusView = changeFocusView;
    }

    public void setAnimatorDuration(int animatorDuration) {
        this.animatorDuration = animatorDuration;
    }

    public void setAnimatorDurationTemporary(int animatorDurationTemporary) {
        this.animatorDurationTemporary = animatorDurationTemporary;
    }

    public void changeFocusRect(Rect rectBitmap, Rect rectBitmapNew) {
        this.fL = this.endL;
        this.fT = this.endT;
        this.fW = this.endW;
        this.fH = this.endH;
        this.endL -= rectBitmapNew.left - rectBitmap.left;
        this.endT -= rectBitmapNew.top - rectBitmap.top;
        this.endW += rectBitmapNew.right - rectBitmap.right + rectBitmapNew.left - rectBitmap.left;
        this.endH += rectBitmapNew.bottom - rectBitmap.bottom + rectBitmapNew.top - rectBitmap.top;
        if (animatorSet.isStarted()){
            animatorSet.cancel();
        }
        focusView.setX(endL);
        focusView.setY(endT);
        focusView.setWidth(endW);
        focusView.setHeight(endH);
    }
}
