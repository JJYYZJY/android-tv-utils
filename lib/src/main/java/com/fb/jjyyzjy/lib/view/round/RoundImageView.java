package com.fb.jjyyzjy.lib.view.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fb.jjyyzjy.lib.R;

/**
 * Created by JJYYZJY on 2017/4/21.
 */
public class RoundImageView extends ImageView{

    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float defaultRadius = 30;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRelativeLayout);
            float radius = ta.getDimension(R.styleable.RoundRelativeLayout_radius, defaultRadius);
            topLeftRadius = ta.getDimension(R.styleable.RoundRelativeLayout_topLeftRadius, radius);
            topRightRadius = ta.getDimension(R.styleable.RoundRelativeLayout_topRightRadius, radius);
            bottomLeftRadius = ta.getDimension(R.styleable.RoundRelativeLayout_bottomLeftRadius, radius);
            bottomRightRadius = ta.getDimension(R.styleable.RoundRelativeLayout_bottomRightRadius, radius);
            ta.recycle();
        }else {
            topLeftRadius = topRightRadius = bottomLeftRadius = bottomRightRadius = defaultRadius;
        }
        mInit();
    }

    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;
    private RectF rectF;
    private boolean isRound;
    private Paint roundPaint;
    private Paint imagePaint;
    private void mInit() {
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        imagePaint = new Paint();
        imagePaint.setXfermode(null);

        rectF = new RectF();

        isRound = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isRound) {
            rectF.left = getPaddingLeft();
            rectF.top = getPaddingTop();
            rectF.right = canvas.getWidth() - getPaddingRight();
            rectF.bottom = canvas.getHeight() - getPaddingBottom();
            canvas.saveLayer(rectF, imagePaint, Canvas.ALL_SAVE_FLAG);
            super.onDraw(canvas);
            drawTopLeft(canvas);
            drawTopRight(canvas);
            drawBottomLeft(canvas);
            drawBottomRight(canvas);
            canvas.restore();
        }else {
            super.onDraw(canvas);
        }
    }

    private void drawTopLeft(Canvas canvas) {
        if (topLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(getPaddingLeft(), getPaddingTop()+topLeftRadius);
            path.lineTo(getPaddingLeft(), getPaddingTop());
            path.lineTo(getPaddingLeft()+topLeftRadius, getPaddingTop());
            path.arcTo(new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft()+topLeftRadius * 2, getPaddingTop() +topLeftRadius * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if (topRightRadius > 0) {
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - topRightRadius - getPaddingRight(), getPaddingTop());
            path.lineTo(width - getPaddingRight(), getPaddingTop());
            path.lineTo(width - getPaddingRight(), topRightRadius + getPaddingTop());
            path.arcTo(new RectF(width - 2 * topRightRadius - getPaddingRight(), getPaddingTop(), width - getPaddingRight(),
                    topRightRadius * 2 + getPaddingTop()), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if (bottomLeftRadius > 0) {
            int height = getHeight();
            Path path = new Path();
            path.moveTo(getPaddingLeft(), height - bottomLeftRadius - getPaddingBottom());
            path.lineTo(getPaddingLeft(), height - getPaddingBottom());
            path.lineTo(bottomLeftRadius + getPaddingLeft(), height - getPaddingBottom());
            path.arcTo(new RectF(getPaddingLeft(), height - 2 * bottomLeftRadius - getPaddingBottom(),
                    getPaddingLeft() + bottomLeftRadius * 2, height - getPaddingBottom()), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if (bottomRightRadius > 0) {
            int height = getHeight();
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - bottomRightRadius - getPaddingRight(), height - getPaddingBottom());
            path.lineTo(width - getPaddingRight(), height - getPaddingBottom());
            path.lineTo(width - getPaddingRight(), height - bottomRightRadius - getPaddingBottom());
            path.arcTo(new RectF(width - 2 * bottomRightRadius - getPaddingRight(), height - 2
                    * bottomRightRadius - getPaddingBottom(), width - getPaddingRight(), height - getPaddingBottom()), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    public void setIsRound(boolean isRound){
        this.isRound = isRound;
        if (isRound)
            setBackgroundResource(R.drawable.bg_image_round_white);
        else
            setBackground(null);
    }

}
