package com.fb.jjyyzjy.lib.view.zoom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.fb.jjyyzjy.lib.focus.FocusScaleUtils;
import com.fb.jjyyzjy.lib.view.round.RoundRelativeLayout;

/**
 * Created by JJYYZJY on 2017/4/22.
 */
public class ZoomRoundRelativeLayout extends RoundRelativeLayout {
    private FocusScaleUtils scaleUtils;

    public ZoomRoundRelativeLayout(Context context) {
        this(context, null);
    }

    public ZoomRoundRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomRoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaleUtils = new FocusScaleUtils();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus){
            scaleUtils.scaleToLarge(this);
        }else {
            scaleUtils.scaleToNormal(this);
        }
    }

}
