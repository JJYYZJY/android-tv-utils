package com.fb.jjyyzjy.lib.view.bring;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by JJYYZJY on 2017/9/7.
 */
public class BringToFrontLinear extends LinearLayout{

    public BringToFrontLinear(Context context) {
        super(context);
        mInit();
    }

    public BringToFrontLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInit();
    }

    public BringToFrontLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BringToFrontLinear(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mInit();
    }

    private BringToFrontHelper bringToFrontHelper;

    private void mInit() {
        Log.i("zhangjy","init");
        setWillNotDraw(true);
        setChildrenDrawingOrderEnabled(true);
        bringToFrontHelper = new BringToFrontHelper();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        Log.i("zhangjy","getChildDrawingOrder"+i);
        return bringToFrontHelper.getChildDrawingOrder(childCount,i);
    }

    @Override
    public void bringChildToFront(View child) {
        Log.i("zhangjy","bringChildToFront");
//        super.bringChildToFront(child);
        bringToFrontHelper.bringChildToFront(this,child);
    }
}
