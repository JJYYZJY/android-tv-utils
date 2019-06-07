package com.fb.jjyyzjy.lib.focus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import static java.lang.Thread.sleep;

/**
 * 获取焦点效果框(画Bitmap形式)
 * Created by ZJY on 2016/5/23.
 */
public class FocusViewForBitmapWithSurfaceView extends SurfaceView implements BaseFocusView,Runnable,SurfaceHolder.Callback{

    private final static String TAG = "FocusView";

    private float frameLeft = 100 ;
    private float frameTop = 100;
    private float frameRight = 1000;
    private float frameBottom = 1000;
    private float frameLeftMove;
    private float frameTopMove;
    private float frameRightMove;
    private float frameBottomMove;
    private float frameLeftMoveEnd;
    private float frameTopMoveEnd;
    private float frameRightMoveEnd;
    private float frameBottomMoveEnd;
    private int movingTime = 1000;//效果执行完成需要的时间
    private int movingNumber = 40;//执行次数
    private int movingVelocity = 3;//执行速度
    private int movingNumberDefault = 40;//执行次数(默认)
    private int movingVelocityDefault = 3;//执行速度(默认)
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

    private Thread thread = new Thread();
    private boolean isThreadRun;

    private SurfaceHolder surfaceHolder;
    private Canvas mCanvas;
    private boolean isCanRun;

    private Thread threadDraw;
    private boolean isDraw;

    public FocusViewForBitmapWithSurfaceView(Context context) {
        this(context,null);
    }
    public FocusViewForBitmapWithSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public FocusViewForBitmapWithSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FocusViewForBitmapWithSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        this.setZOrderOnTop(true);//设置画布  背景透明
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

    }

    /**
     * 初始化焦点图片
     * @param imageRes
     */
    public void initFocusBitmapRes(int imageRes){
        initFocusBitmapRes(imageRes,imageRes);
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

//        DebugLog.i("rectBitmap=="+rectBitmap.left+"=="+rectBitmap.top+"=="+rectBitmap.right+"=="+rectBitmap.bottom);
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

    private void drawBitmap(Canvas canvas) {
        Rect rect = new Rect();
        rect.left = (int) (frameLeft - rectBitmap.left);
        rect.top = (int) (frameTop - rectBitmap.top);
        rect.right = (int) (frameRight + rectBitmap.right);
        rect.bottom = (int) (frameBottom + rectBitmap.bottom);
        Log.i("SurfaceView","drawBitmap+left=="+rect.left+"==right=="+rect.right+"==top=="+rect.top+"==bottom=="+rect.bottom);

        ninePatch.draw(canvas, rect);
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
        setVisibility(VISIBLE);
//        FocusViewForBitmapWithSurfaceView.this.run();

    }

    /**
     * 设置焦点框移动到的位置
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void focusMove(float l, float t, float r, float b){
//        DebugLog.i(" l=="+l+"== t=="+t+"== r=="+r+"== b=="+b);
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

        if (isThreadRun){
            synchronized (FocusViewForBitmapWithSurfaceView.class){
                if (isThreadRun){
                    return;
                }
            }
        }

        isThreadRun = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    Log.i("SurfaceView","ThreadStart");
                    Log.i("SurfaceView","frameLeftMove=="+frameLeftMove+"==frameTopMove=="+frameTopMove+"==frameRightMove=="+frameRightMove+"==frameBottomMove=="+frameBottomMove
                    +"/n"+"==frameLeft=="+frameLeft+"==frameTop=="+frameTop+"==frameRight=="+frameRight+"==frameBottom=="+frameBottom
                    +"/n"+"==frameLeftMoveEnd=="+frameLeftMoveEnd+"==frameTopMoveEnd=="+frameTopMoveEnd+"==frameRightMoveEnd=="+frameRightMoveEnd+"==frameBottomMoveEnd=="+frameBottomMoveEnd);
                    if (checkMoveOk()){
                        synchronized (FocusViewForBitmapWithSurfaceView.class){
                            if (checkMoveOk()){
                                frameLeft = frameLeftMoveEnd;
                                frameTop = frameTopMoveEnd;
                                frameRight = frameRightMoveEnd;
                                frameBottom = frameBottomMoveEnd;

                                threadDraw = new Thread(FocusViewForBitmapWithSurfaceView.this);
                                threadDraw.start();
                                isDraw = true;

                                Log.i("SurfaceView","ThreadEnd");
                                if (onFocusMoveEndListener != null){
                                    onFocusMoveEndListener.focusEnd(changeFocusView);
                                }

                                isThreadRun = false;
                                return;
                            }
                        }

                    }

                    frameLeft += frameLeftMove;
                    frameTop += frameTopMove;
                    frameRight += frameRightMove;
                    frameBottom += frameBottomMove;
                    Log.i("SurfaceView","Thread===frameLeft=="+frameLeft+"==frameTop="+frameTop+"==frameRight=="+frameRight+"==frameBottom=="+frameBottom);

                    if (!isDraw){
                        threadDraw = new Thread(FocusViewForBitmapWithSurfaceView.this);
                        threadDraw.start();
                        isDraw = true;
                    }

                    try {
                        sleep(movingVelocity);
                        Log.i("SurfaceView","ThreadSleep");
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
//        DebugLog.i(" l=="+l+"== t=="+t+"== r=="+r+"== b=="+b);
        this.frameLeftMoveEnd += l;
        this.frameTopMoveEnd += t;
        this.frameRightMoveEnd += r;
        this.frameBottomMoveEnd += b;
        startMoveFocus();
    }

    public void scrollerFocusX(float scrollerX){
//        DebugLog.i("scrollerFocusX"+scrollerX);
        if (scrollerX == 0){
            return;
        }
        changeMoveEnd(scrollerX,0,scrollerX,0);
    }

    public void scrollerFocusY(float scrollerY){
//        DebugLog.i("scrollerFocusY"+scrollerY);
        if (scrollerY == 0){
            return;
        }
        changeMoveEnd(0,scrollerY,0,scrollerY);
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("SurfaceView","surfaceCreated");
        isCanRun = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("SurfaceView","surfaceDestroyed");
        isCanRun = false;
    }

    @Override
    public void run() {
        Log.i("SurfaceView","run");
        synchronized (this){
            if (isHide){
                return;
            }
            if (isCanRun){
                Log.i("SurfaceView","run==ok");
                mCanvas = null;
                try {
                    mCanvas = surfaceHolder.lockCanvas();
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    drawBitmap(mCanvas);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        if (mCanvas != null){
                            surfaceHolder.unlockCanvasAndPost(mCanvas);
                        }
                    }catch (Exception e){
                    }finally {
                        isDraw = false;
                    }

                }
            }
        }
    }

    private boolean checkMoveOk(){
        if (frameLeftMove > 0 && frameLeft + frameLeftMove >= frameLeftMoveEnd ||
                frameLeftMove < 0 && frameLeft + frameLeftMove <= frameLeftMoveEnd ||
                frameTopMove > 0 && frameTop + frameTopMove >= frameTopMoveEnd ||
                frameTopMove < 0 && frameTop + frameTopMove <= frameTopMoveEnd ||
                frameRightMove > 0 && frameRight + frameRightMove >= frameRightMoveEnd ||
                frameRightMove < 0 && frameRight + frameRightMove <= frameRightMoveEnd ||
                frameBottomMove > 0 && frameBottom + frameBottomMove >= frameBottomMoveEnd ||
                frameBottomMove < 0 && frameBottom + frameBottomMove <= frameBottomMoveEnd ) {
            return true;
        }
        return false;
    }

    boolean isHide;
    public void hideFocus(){
//        if (isHide){
//            return;
//        }
//        if (isCanRun){
//            Log.i("SurfaceView","hideFocus==run==ok");
//            mCanvas = null;
//            try {
//                mCanvas = surfaceHolder.lockCanvas();
//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                try {
//                    if (mCanvas != null){
//                        surfaceHolder.unlockCanvasAndPost(mCanvas);
//                        isHide = true;
//                    }
//                }catch (Exception e){
//                }
//            }
//        }
    };
    public void showFocus(){
//        if (!isHide){
//            return;
//        }
//        if (isCanRun){
//            Log.i("SurfaceView","showFocus==run==ok");
//            mCanvas = null;
//            try {
//                mCanvas = surfaceHolder.lockCanvas();
//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                drawBitmap(mCanvas);
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                try {
//                    if (mCanvas != null){
//                        surfaceHolder.unlockCanvasAndPost(mCanvas);
//                    }
//                }catch (Exception e){
//                }finally {
//                    isHide = false;
//                    isDraw = false;
//                }
//
//            }
//        }
    }

    /*
                            Log.i("SurfaceView","frameLeftMove > 0 && frameLeft + frameLeftMove >= frameLeftMoveEnd");
                        if (frameLeftMove < 0 && frameLeft + frameLeftMove <= frameLeftMoveEnd)
                        {
                            Log.i("SurfaceView","frameLeftMove < 0 && frameLeft + frameLeftMove <= frameLeftMoveEnd");
                        }
                        if (frameTopMove > 0 && frameTop + frameTopMove >= frameTopMoveEnd){
                            Log.i("SurfaceView","frameTopMove > 0 && frameTop + frameTopMove >= frameTopMoveEnd");
                        }
                        if (frameTopMove < 0 && frameTop + frameTopMove <= frameTopMoveEnd){
                            Log.i("SurfaceView","frameTopMove < 0 && frameTop + frameTopMove <= frameTopMoveEnd");
                        }
                        if (frameRightMove > 0 && frameRight + frameRightMove >= frameRightMoveEnd){
                            Log.i("SurfaceView","frameRightMove > 0 && frameRight + frameRightMove >= frameRightMoveEnd");
                        }
                        if (frameRightMove < 0 && frameRight + frameRightMove <= frameRightMoveEnd){
                            Log.i("SurfaceView","frameRightMove < 0 && frameRight + frameRightMove <= frameRightMoveEnd");
                        }
                        if (frameBottomMove > 0 && frameBottom + frameBottomMove >= frameBottomMoveEnd){
                            Log.i("SurfaceView","frameBottomMove > 0 && frameBottom + frameBottomMove >= frameBottomMoveEnd");
                        }
                        if (frameBottomMove < 0 && frameBottom + frameBottomMove <= frameBottomMoveEnd){
                            Log.i("SurfaceView","frameBottomMove < 0 && frameBottom + frameBottomMove <= frameBottomMoveEnd");
                        }*/
}
