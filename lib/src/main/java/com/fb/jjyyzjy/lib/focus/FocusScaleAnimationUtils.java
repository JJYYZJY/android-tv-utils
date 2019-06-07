package com.fb.jjyyzjy.lib.focus;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by ZJY on 2016/6/7.
 */
public class FocusScaleAnimationUtils {
    private View oldView;
    /**
     * 放大用时
     */
    private int durationLarge = 300;
    /**
     * 缩小用时
     */
    private int durationSmall = 500;
    private float scale = 1.1f;
    private ScaleAnimation scaleAnimation;
    private Interpolator interpolatorLarge;
    private Interpolator interpolatorSmall;

    public FocusScaleAnimationUtils(){
        this.interpolatorLarge = new AccelerateInterpolator(1.5f);
        this.interpolatorSmall = new DecelerateInterpolator(1.5f);
    }

    public FocusScaleAnimationUtils(int duration, float scale, Interpolator interpolator){
        this(duration,duration,scale,interpolator,interpolator);
    }

    public FocusScaleAnimationUtils(int durationLarge, int durationSmall, float scale, Interpolator interpolatorLarge, Interpolator interpolatorSmall){
        this.durationLarge = durationLarge;
        this.durationSmall = durationSmall;
        this.scale = scale;
        this.interpolatorLarge = interpolatorLarge;
        this.interpolatorSmall = interpolatorSmall;
    }

    /**
     * 放大指定view
     * @param item
     */
    public void scaleToLarge(View item) {
        if (!item.isFocused()) {
            return;
        }

        scaleAnimation = new ScaleAnimation(1f,scale,1f,scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(interpolatorLarge);
        scaleAnimation.setDuration(durationLarge);
        scaleAnimation.setStartOffset(10);
        item.startAnimation(scaleAnimation);

        oldView = item;
    }

    /**
     * 还原指定view
     * @param item
     */
    public void scaleToNormal(View item) {
        if (scaleAnimation == null || item == null) {
            return;
        }
        scaleAnimation.cancel();
//        ScaleAnimation scaleAnimation2 = new ScaleAnimation(scale,1f,scale,1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation2.setInterpolator(interpolatorSmall);
//        scaleAnimation2.setDuration(durationSmall);
//        item.startAnimation(scaleAnimation2);

        oldView = null;
    }

    /**
     * 还原上一次执行放大的view
     */
    public void scaleToNormal() {
        scaleToNormal(oldView);
    }

}
