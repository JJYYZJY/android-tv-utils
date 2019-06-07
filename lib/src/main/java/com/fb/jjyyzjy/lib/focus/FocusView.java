package com.fb.jjyyzjy.lib.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 获取焦点效果框(绘制)
 * Created by ZJY on 2016/5/16.
 */
public class FocusView extends View{

    private final static String TAG = "FocusView";

    private int frameColor = Color.YELLOW;
    private int lightColor = Color.BLUE;
    private int frameWidth = 4;
    private int lightWidth = 30;
    private int lightDrawFrequency = 20;//绘制频次
    private int lightStrokeWidth;
    private float roundX = 10;
    private float roundY = 10;

    private Paint paintFrame;//画框的画笔
    private Paint paintLight;//画光的画笔
    private float lightAlpha = 100;//光起始透明的
    private float lightAlphaCopy;
    private float frameLeft;
    private float frameTop;
    private float frameRight;
    private float frameBottom;
    private float frameLeftMove;
    private float frameTopMove;
    private float frameRightMove;
    private float frameBottomMove;
    private float frameLeftMoveEnd;
    private float frameTopMoveEnd;
    private float frameRightMoveEnd;
    private float frameBottomMoveEnd;
    private int movingTime = 1000;//效果执行完成需要的时间
    private int movingNumber = 10;//执行次数
    private int movingVelocity = 1;//执行速度

    public FocusView(Context context) {
        this(context,null);
    }
    public FocusView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FocusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        lightAlphaCopy = lightAlpha;
        initFramePaint();
        initLightPaint();
    }
    private void initFramePaint() {
        paintFrame = new Paint();
        paintFrame.setAntiAlias(true);
        paintFrame.setColor(frameColor);
        paintFrame.setStrokeWidth(frameWidth);
        paintFrame.setStyle(Paint.Style.STROKE);
    }
    private void initLightPaint() {
        lightStrokeWidth = lightWidth / lightDrawFrequency;
        paintLight = new Paint();
        paintLight.setAntiAlias(true);
        paintLight.setColor(lightColor);
        paintLight.setStrokeWidth(lightStrokeWidth);
        paintLight.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        clearInit();
        drawFrame(canvas);
//        drawLight(canvas);
    }

    private void clearInit() {
        lightAlpha = lightAlphaCopy;
    }

    private void drawFrame(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = frameLeft;
        rectF.top = frameTop;
        rectF.right = frameRight;
        rectF.bottom = frameBottom;
        canvas.drawRoundRect(rectF, roundX, roundY, paintFrame);
    }

    private void drawLight(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = frameLeft;
        rectF.top = frameTop;
        rectF.right = frameRight;
        rectF.bottom = frameBottom;
        drawLightRoundRect(rectF,roundX,roundY, canvas);
    }

    private void drawLightRoundRect(RectF rectF,float roundX, float roundY, Canvas canvas) {
        roundX += 1;
        roundY += 1;
        rectF.left -= lightStrokeWidth;
        rectF.top -= lightStrokeWidth;
        rectF.right += lightStrokeWidth;
        rectF.bottom += lightStrokeWidth;
        lightAlpha -= lightAlpha/(lightDrawFrequency > 1 ? lightDrawFrequency-- : lightDrawFrequency );
        paintLight.setAlpha(lightAlpha > 0 ? (int) lightAlpha : 0);
        canvas.drawRoundRect(rectF, roundX, roundY, paintLight);
        if (rectF.left >= 0){
            drawLightRoundRect(rectF,roundX,roundY,canvas);
        }
    }

    /**
     * 设置焦点框位置
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void setFocusLayout(float l, float t, float r, float b){
        this.frameLeft = l;
        this.frameTop = t;
        this.frameRight = r;
        this.frameBottom = b;
        invalidate();
//        layout(0, 0, t-l+lightWidth+lightWidth, b-t+lightWidth+lightWidth);
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

        startMoveFocus();
    }

    /**
     * 开始移动
     */
    private void startMoveFocus() {
        this.frameLeftMove = (frameLeftMoveEnd - frameLeft)/movingNumber;
        this.frameTopMove = (frameTopMoveEnd - frameTop)/movingNumber;
        this.frameRightMove = (frameRightMoveEnd - frameRight)/movingNumber;
        this.frameBottomMove = (frameBottomMoveEnd - frameBottom)/movingNumber;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (frameLeftMove > 0 && frameLeft + frameLeftMove >= frameLeftMoveEnd ||
                        frameLeftMove < 0 && frameLeft + frameLeftMove <= frameLeftMoveEnd ||
                        frameTopMove > 0 && frameTop + frameTopMove >= frameTopMoveEnd ||
                        frameTopMove < 0 && frameTop + frameTopMove <= frameTopMoveEnd ||
                        frameRightMove > 0 && frameRight + frameRightMove >= frameRightMoveEnd ||
                        frameRightMove < 0 && frameRight + frameRightMove <= frameRightMoveEnd ||
                        frameBottomMove > 0 && frameBottom + frameBottomMove >= frameBottomMoveEnd ||
                        frameBottomMove < 0 && frameBottom + frameBottomMove <= frameBottomMoveEnd ){
                    frameLeft = frameLeftMoveEnd;
                    frameTop = frameTopMoveEnd;
                    frameRight = frameRightMoveEnd;
                    frameBottom = frameBottomMoveEnd;
                    invalidate();
                    setVisibility(View.VISIBLE);
                    return;
                }else {
                    frameLeft += frameLeftMove;
                    frameTop += frameTopMove;
                    frameRight += frameRightMove;
                    frameBottom += frameBottomMove;
                    invalidate();
                    new Handler().postDelayed(this,movingVelocity);
                }
            }
        },movingVelocity);
    }

    /**
     * 改变终点位置
     */
    public void changeMoveEnd(float l, float t, float r, float b){
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
        setVisibility(View.INVISIBLE);
    }

    public void scrollerFocusY(float scrollerY){
        if (scrollerY == 0){
            return;
        }
        changeMoveEnd(0,scrollerY,0,scrollerY);
        setVisibility(View.INVISIBLE);
    }
}
