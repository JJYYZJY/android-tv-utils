package com.fb.jjyyzjy.lib.focus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 获取焦点效果框(画Bitmap形式)
 * Created by ZJY on 2016/5/23.
 */
public class FocusViewForBitmap extends View implements BaseFocusView{

    private final static String TAG = "FocusView";

    private int frameColor = Color.YELLOW;
    private int lightColor = Color.BLUE;
    private int frameWidth = 4;
    private int lightWidth = 30;
    private int lightDrawFrequency = 20;//绘制频次
    private int lightStrokeWidth;
    private float roundX = 10;
    private float roundY = 10;

    private Paint paintDrawBitmap;
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
    private int movingNumber = 25;//执行次数
    private int movingVelocity = 8;//执行速度
    private int movingNumberDefault = 25;//执行次数(默认)
    private int movingVelocityDefault = 8;//执行速度(默认)
    private int movingNumberTemporary = -1;//执行次数(临时)
    private int movingVelocityTemporary = -1;//执行速度(临时)

    private Bitmap bitmapMain;
    private Rect rectBitmapMain;
    private NinePatch ninePatchMain;

    private Bitmap bitmapTopView;
    private Rect rectBitmapTopView;
    private NinePatch ninePatchTopView;

    private Bitmap bitmap;
    private Rect rectBitmap;
    private NinePatch ninePatch;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setVisibility(View.VISIBLE);
        }
    };
    //    private boolean isInvisible;
    private Thread thread = new Thread();
    private boolean isThreadRun;
    private boolean isMoveHideFocus = false;

    public FocusViewForBitmap(Context context) {
        this(context,null);
    }
    public FocusViewForBitmap(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public FocusViewForBitmap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FocusViewForBitmap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        if (visibility == INVISIBLE){
//            isInvisible = true;
//        }else {
//            isInvisible = false;
//        }
//    }

    private void init() {
//        lightAlphaCopy = lightAlpha;
//        initFramePaint();
//        initLightPaint();

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

    /**
     * 初始化焦点图片
     * @param imageRes
     */
    public void initFocusBitmapRes(int imageRes){
        initFocusBitmapRes(imageRes,imageRes);
    }

    public void initFocusBitmapRes(int imageRes, boolean isMoveHideFocus){
        this.isMoveHideFocus = isMoveHideFocus;
        initFocusBitmapRes(imageRes,imageRes);
    }

    public void initFocusBitmapRes(int imageRes,int imageResTwo, boolean isMoveHideFocus){
        this.isMoveHideFocus = isMoveHideFocus;
        initFocusBitmapRes(imageRes,imageResTwo);
    }

    /**
     * 初始化焦点图片
     * @param imageRes
     * @param imageResTwo
     */
    public void initFocusBitmapRes(int imageRes, int imageResTwo){
        bitmapMain = BitmapFactory.decodeResource(getResources(), imageRes);
        rectBitmapMain = new Rect();
        Drawable drawableMain = getResources().getDrawable(imageRes);
        if (drawableMain != null)
            drawableMain.getPadding(rectBitmapMain);
        ninePatchMain = new NinePatch(bitmapMain,bitmapMain.getNinePatchChunk());

        if (imageResTwo == 0 ){
            imageResTwo = imageRes;
        }
        bitmapTopView = BitmapFactory.decodeResource(getResources(), imageResTwo);
        rectBitmapTopView = new Rect();
        Drawable drawableTopView = getResources().getDrawable(imageResTwo);
        if (drawableTopView != null)
            drawableTopView.getPadding(rectBitmapTopView);
        ninePatchTopView = new NinePatch(bitmapTopView,bitmapTopView.getNinePatchChunk());

        bitmap = bitmapMain;
        rectBitmap = rectBitmapMain;
        ninePatch = ninePatchMain;

        if (isMoveHideFocus){
            hideFocus();
        }

    }

    public void setBitmapForTop(){
        bitmap = bitmapTopView;
        rectBitmap = rectBitmapTopView;
        ninePatch = ninePatchTopView;
    }

    public void clearBitmapForTop(){
        bitmap = bitmapMain;
        rectBitmap = rectBitmapMain;
        ninePatch = ninePatchMain;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        clearInit();
//        drawFrame(canvas);
//        drawLight(canvas);
        drawBitmap(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        Rect rect = new Rect();
        rect.left = (int) (frameLeft - rectBitmap.left);
        rect.top = (int) (frameTop - rectBitmap.top);
        rect.right = (int) (frameRight + rectBitmap.right);
        rect.bottom = (int) (frameBottom + rectBitmap.bottom);
//        canvas.drawRect(rect.left,rect.top,rect.right,rect.bottom,paintLight);
//        canvas.drawBitmap(bitmap, null , rect , null);

        ninePatch.draw(canvas, rect);
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

        this.frameLeftMoveEnd = l;
        this.frameTopMoveEnd = t;
        this.frameRightMoveEnd = r;
        this.frameBottomMoveEnd = b;

        this.frameLeft = l;
        this.frameTop = t;
        this.frameRight = r;
        this.frameBottom = b;
        invalidate();
//        setVisibility(VISIBLE);
//        handler.sendMessage(handler.obtainMessage());
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

        if (movingNumberTemporary == -1 || movingVelocityTemporary == -1){
            movingNumber = movingNumberDefault;
            movingVelocity = movingVelocityDefault;
        }else {
            movingNumber = movingNumberTemporary;
            movingVelocity = movingVelocityTemporary;
            movingNumberTemporary = -1;
            movingVelocityTemporary = -1;
        }

        this.frameLeftMove = (frameLeftMoveEnd - frameLeft)/movingNumber;
        this.frameTopMove = (frameTopMoveEnd - frameTop)/movingNumber;
        this.frameRightMove = (frameRightMoveEnd - frameRight)/movingNumber;
        this.frameBottomMove = (frameBottomMoveEnd - frameBottom)/movingNumber;
//        if (thread != null && thread.isAlive()){
//            isThreadRun = false;
//            thread.interrupt();
//        }
        if (isThreadRun){
            return;
        }
        isThreadRun = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                frameLeftMove = (frameLeftMoveEnd - frameLeft)/movingNumber;
//                frameTopMove = (frameTopMoveEnd - frameTop)/movingNumber;
//                frameRightMove = (frameRightMoveEnd - frameRight)/movingNumber;
//                frameBottomMove = (frameBottomMoveEnd - frameBottom)/movingNumber;
//                if (isInvisible){
//                    handler.sendMessage(handler.obtainMessage());
//                }else {
//                    isInvisible = false;
//                }
                while (true){
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
                        postInvalidateDelayed(movingVelocity);
//                        handler.sendMessageDelayed(handler.obtainMessage(),250);
                        if (isMoveHideFocus){
                            handler.sendMessage(handler.obtainMessage());
                            isMoveHideFocus = false;
                        }
                        if (onFocusMoveEndListener != null){
                            onFocusMoveEndListener.focusEnd(changeFocusView);
                        }
                        isThreadRun = false;
                        return;
                    }else {
                        frameLeft += frameLeftMove;
                        frameTop += frameTopMove;
                        frameRight += frameRightMove;
                        frameBottom += frameBottomMove;
//                    invalidate();
//                    new Handler().postDelayed(this,movingVelocity);
                        postInvalidateDelayed(movingVelocity);
                    }
                    try {
                        synchronized (this) {
                            wait(movingVelocity);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        thread.start();
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
//        this.frameLeftMove = (frameLeftMoveEnd - frameLeft)/movingNumber;
//        this.frameTopMove = (frameTopMoveEnd - frameTop)/movingNumber;
//        this.frameRightMove = (frameRightMoveEnd - frameRight)/movingNumber;
//        this.frameBottomMove = (frameBottomMoveEnd - frameBottom)/movingNumber;
    }

    public void scrollerFocusX(float scrollerX){
        if (scrollerX == 0){
            return;
        }
        changeMoveEnd(scrollerX,0,scrollerX,0);
//        setVisibility(View.INVISIBLE);
    }

    public void scrollerFocusY(float scrollerY){
        if (scrollerY == 0){
            return;
        }
        changeMoveEnd(0,scrollerY,0,scrollerY);
//        setVisibility(View.INVISIBLE);
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
     * 设置焦点图(9patch图)
     * @param resId 资源id
     */
    public void setFocusBitmap(int resId){
        try {
            Bitmap bitmapNew = BitmapFactory.decodeResource(getResources(), resId);
            Rect rectBitmapNew = new Rect();
            Drawable drawableNew = getResources().getDrawable(resId);
            drawableNew.getPadding(rectBitmapNew);
            NinePatch ninePatchNew = new NinePatch(bitmapNew,bitmapNew.getNinePatchChunk());

            bitmap = bitmapNew;
            rectBitmap = rectBitmapNew;
            ninePatch = ninePatchNew;
        }catch (Exception e){
            throw new RuntimeException("resId == null");
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

    private OnFocusMoveEndListener onFocusMoveEndListener;//焦点移动结束监听
    private View changeFocusView;//与焦点移动结束监听绑定的view
    @Override
    public void setOnFocusMoveEndListener(OnFocusMoveEndListener onFocusMoveEndListener, View changeFocusView) {
        this.onFocusMoveEndListener = onFocusMoveEndListener;
        this.changeFocusView = changeFocusView;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}
