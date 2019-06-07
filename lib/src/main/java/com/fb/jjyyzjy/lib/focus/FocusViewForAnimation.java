package com.fb.jjyyzjy.lib.focus;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 动画方式移动的焦点
 * Created by ZJY on 2016/6/2.
 */
public class FocusViewForAnimation extends View implements BaseFocusView {
    private final static String TAG = "FocusView";

    private float frameLeft;
    private float frameTop;
    private float frameRight;
    private float frameBottom;
    private float frameLeftMoveEnd;
    private float frameTopMoveEnd;
    private float frameRightMoveEnd;
    private float frameBottomMoveEnd;
    private float frameWidth;
    private float frameHeight;
    private float newFrameWidth;
    private float newFrameHeight;
    private int movingNumber = 80;//执行次数
    private int movingVelocity = 3;//执行速度
    private int movingNumberDefault = 80;//执行次数(默认)
    private int movingVelocityDefault = 3;//执行速度(默认)
    private int movingNumberTemporary = -1;//执行次数(临时)
    private int movingVelocityTemporary = -1;//执行速度(临时)

    private int imageMainRes;
    private Rect rectBitmapMain;

    private int imageTopRes;
    private Rect rectBitmapTopView;

    private int imageRes;
    private Rect rectBitmap;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setVisibility(View.VISIBLE);
        }
    };

    public FocusViewForAnimation(Context context) {
        this(context,null);
    }
    public FocusViewForAnimation(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public FocusViewForAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FocusViewForAnimation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
//        if (getChildCount() > 0){
//            getChildAt(0).layout(-200,-200,-100,-100);
//        }
    }

    private void setFocusLayout(){
        Rect rect = new Rect();
        rect.left = (int) (frameLeft - rectBitmap.left);
        rect.top = (int) (frameTop - rectBitmap.top);
        rect.right = (int) (frameRight + rectBitmap.right);
        rect.bottom = (int) (frameBottom + rectBitmap.bottom);
        this.layout(rect.left,rect.top,rect.right,rect.bottom);
    }


    private void init(Context context) {
    }

    @Override
    public void initFocusBitmapRes(int imageRes) {
        initFocusBitmapRes(imageRes,imageRes);
    }

    /**
     * 初始化焦点图片
     * @param imageRes
     * @param imageResTwo
     */
    public void initFocusBitmapRes(int imageRes, int imageResTwo){

        imageMainRes = imageRes;
        imageTopRes = imageResTwo;
        this.imageRes = imageRes;
        this.setBackgroundResource(imageRes);

        rectBitmapMain = new Rect();
        Drawable drawableMain = getResources().getDrawable(imageRes);
        if (drawableMain != null) {
            drawableMain.getPadding(rectBitmapMain);
        }
//
        rectBitmapTopView = new Rect();
        Drawable drawableTopView = getResources().getDrawable(imageResTwo);
        if (drawableTopView != null) {
            drawableTopView.getPadding(rectBitmapTopView);
        }
//
        rectBitmap = rectBitmapMain;
        Log.e("zxcFocus","rectBitmap=="+rectBitmap.left+"=="+rectBitmap.top+"=="+rectBitmap.right+"=="+rectBitmap.bottom);
//
    }

    public void setBitmapForTop(){
        imageRes = imageTopRes;
        rectBitmap = rectBitmapTopView;
        changeFocusBg();
    }

    public void clearBitmapForTop(){
        imageRes = imageMainRes;
        rectBitmap = rectBitmapMain;
        changeFocusBg();
    }

    private void changeFocusBg(){
        this.setBackgroundResource(imageRes);
    }

    private void clearInit() {
    }

    /**
     * 设置焦点框位置
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void setFocusLayout(float l, float t, float r, float b){

//        this.frameLeftMoveEnd = l;
//        this.frameTopMoveEnd = t;
//        this.frameRightMoveEnd = r;
//        this.frameBottomMoveEnd = b;
//
//        this.frameLeft = l;
//        this.frameTop = t;
//        this.frameRight = r;
//        this.frameBottom = b;
//
//        frameWidth = r-l;
//        frameHeight = b-t;
//
//        setFocusLayout();
//
        setVisibility(VISIBLE);
    }

    /**
     * 设置焦点框移动到的位置
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void focusMove(float l, float t, float r, float b){

        this.frameLeftMoveEnd = l;
        this.frameTopMoveEnd = t;
        this.frameRightMoveEnd = r;
        this.frameBottomMoveEnd = b;

        newFrameWidth = r-l;
        newFrameHeight = b-t;

        startMoveFocus();
    }


    private AnimatorSet mCurrentAnimatorSet;
    /**
     * 开始移动
     */
    private void startMoveFocus() {
        if (mCurrentAnimatorSet != null)
            mCurrentAnimatorSet.cancel();

        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(this, "translationX",frameLeftMoveEnd - rectBitmap.left);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(this, "translationY",frameTopMoveEnd - rectBitmap.top);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(this), "width", (int)frameWidth + rectBitmap.left + rectBitmap.right,
                (int) newFrameWidth + rectBitmap.left + rectBitmap.right);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(this), "height", (int)frameHeight + rectBitmap.top + rectBitmap.bottom,
                (int) newFrameHeight + rectBitmap.top + rectBitmap.bottom);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(200);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.i("FocusAnimation","Start");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i("FocusAnimation","End");
                if (onFocusMoveEndListener != null){
                    onFocusMoveEndListener.focusEnd(changeFocusView);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Log.i("FocusAnimation","Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                Log.i("FocusAnimation","Repeat");
            }
        });
        mAnimatorSet.start();
        mCurrentAnimatorSet = mAnimatorSet;
        frameWidth = newFrameWidth;
        frameHeight = newFrameHeight;
        frameLeft = frameLeftMoveEnd;
        frameTop = frameTopMoveEnd;
        frameRight = frameRightMoveEnd;
        frameBottom = frameBottomMoveEnd;
    }

    /**
     * 改变终点位置
     */
    private void changeMoveEnd(float l, float t, float r, float b){
        this.frameLeftMoveEnd += l;
        this.frameTopMoveEnd += t;
        this.frameRightMoveEnd += r;
        this.frameBottomMoveEnd += b;
        startMoveFocus();
    }

    public void scrollerFocusX(float scrollerX){
        if (scrollerX == 0){
            return;
        }
        changeMoveEnd(scrollerX,0,scrollerX,0);
    }

    public void scrollerFocusY(float scrollerY){
        if (scrollerY == 0){
            return;
        }
        changeMoveEnd(0,scrollerY,0,scrollerY);
    }

    @Override
    public void hideFocus() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void showFocus() {
        setVisibility(VISIBLE);
    }

    public void setFocusBitmap(int resId){
        try {
            Rect rectBitmapNew = new Rect();
            Drawable drawableNew = getResources().getDrawable(resId);
            drawableNew.getPadding(rectBitmapNew);
            imageRes = resId;
            rectBitmap = rectBitmapNew;
            changeFocusBg();
        }catch (Exception e){
        }
    }

    /**
     * 设置焦点滑动速度
     * @param movingNumberDefault 绘制次数 80
     * @param movingVelocityDefault 绘制速度 3
     */
    public void setMoveVelocity(int movingNumberDefault, int movingVelocityDefault){
        this.movingNumberDefault = movingNumberDefault;
        this.movingVelocityDefault = movingVelocityDefault;
    }

    /**
     * 设置焦点滑动速度(临时，只针对接下来的一次移动)
     * @param movingNumberTemporary 绘制次数 80
     * @param movingVelocityTemporary 绘制速度 3
     */
    public void setMoveVelocityTemporary(int movingNumberTemporary, int movingVelocityTemporary){
        this.movingNumberTemporary = movingNumberTemporary;
        this.movingVelocityTemporary = movingVelocityTemporary;
    }

    private OnFocusMoveEndListener onFocusMoveEndListener;
    private View changeFocusView;
    public void setOnFocusMoveEndListener(OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView){
        this.onFocusMoveEndListener = onFocusMoveEndListener;
        this.changeFocusView = changeFocusView;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * 用於放大的view
     */
    public class ScaleView {
        private View view;
        private int width;
        private int height;

        public ScaleView(View view) {
            this.view = view;
        }

        public int getWidth() {
            return view.getLayoutParams().width;
        }

        public void setWidth(int width) {
            this.width = width;
            view.getLayoutParams().width = width;
            view.requestLayout();
        }

        public int getHeight() {
            return view.getLayoutParams().height;
        }

        public void setHeight(int height) {
            this.height = height;
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
    }
}
