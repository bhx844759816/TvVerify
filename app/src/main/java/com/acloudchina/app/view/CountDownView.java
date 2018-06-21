package com.acloudchina.app.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.acloudchina.app.R;
import com.acloudchina.app.utils.DensityUtil;

public class CountDownView extends View {

    private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEFAULT_SIZE = 50;
    private static final int DEFAULT_CIRCLE_STROKE = 4;
    private static final int DEFAULT_TEXT_SIZE = 15;
    private Paint mTextPaint;
    private Paint mColorPaint;
    private RectF mRectF;
    private String mText;
    private int mArcStartAngle;
    private int mArcAngle;
    private int mColor;
    private int mTextColor;
    private int mSize;
    private int mStrokeWidth;
    private int mTextSize;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , R.styleable.CountDownView, defStyleAttr, 0);
        mColor = typedArray.getColor(R.styleable.CountDownView_color, DEFAULT_COLOR);
        mTextColor = typedArray.getColor(R.styleable.CountDownView_text_color, DEFAULT_COLOR);
        mSize = typedArray.getDimensionPixelSize(R.styleable.CountDownView_size,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SIZE,
                        getResources().getDisplayMetrics()));
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CountDownView_circle_stroke,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_CIRCLE_STROKE,
                        getResources().getDisplayMetrics()));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CountDownView_text_size,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE,
                        getResources().getDisplayMetrics()));
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mArcStartAngle = 270;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setDither(true);
        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setDither(true);
        mColorPaint.setStyle(Paint.Style.STROKE);
        mColorPaint.setColor(mColor);
        mColorPaint.setStrokeWidth(mStrokeWidth);
        mTextPaint.setTextSize(mTextSize);
        mRectF = new RectF();
        mRectF.left = DensityUtil.dip2px(getContext(), 4) * 2;
        mRectF.right = mSize - DensityUtil.dip2px(getContext(), 4) * 2;
        mRectF.top = DensityUtil.dip2px(getContext(), 4) * 2;
        mRectF.bottom = mSize - DensityUtil.dip2px(getContext(), 4) * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
        drawArc(canvas);
        super.onDraw(canvas);
    }

    public void setText(int count) {
        mText = String.valueOf(count);
        postInvalidate();
    }

    public void startCountDown(long duration) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 360);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcAngle = (int) (animation.getAnimatedFraction() * 360);
                postInvalidate();
            }
        });
        animator.start();
    }

    /**
     * 画文本
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float x = mSize / 2;
        float y = mSize / 2 - metrics.top / 2 - metrics.bottom / 2;
        if (mText != null)
            canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawArc(Canvas canvas) {
        canvas.drawArc(mRectF, mArcStartAngle + mArcAngle, 360 - mArcAngle, false, mColorPaint);
    }
}
