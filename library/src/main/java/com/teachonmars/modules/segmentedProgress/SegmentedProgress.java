package com.teachonmars.modules.segmentedProgress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.teachonmars.modules.segmentedProgress.internal.DrawingController;
import com.teachonmars.modules.segmentedProgress.internal.ViewMeasurer;

public class SegmentedProgress extends View {
    private DrawingController drawingController;
    private ViewMeasurer      viewMeasurer;

    public SegmentedProgress(Context context) {
        this(context, null);
    }

    public SegmentedProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.segmentedProgressStyle);
    }

    public SegmentedProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.SegmentedProgressDefault);
    }

    public SegmentedProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init();
        initFromAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        drawingController = new DrawingController(this);
        viewMeasurer = new ViewMeasurer(this, drawingController);
    }

    private void initFromAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SegmentedProgress, defStyleAttr, defStyleRes);
        drawingController.loadFromResources(a, isInEditMode());
        a.recycle();
    }

    public void setImageDrawable(Drawable drawable) {
        drawingController.setImageDrawable(drawable);
    }

    public void setDrawable(@DrawableRes int resId) {
        drawingController.setDrawable(resId);
    }

    @Override
    public void postInvalidate() {
        if (!drawingController.isPreventInvalidation()) {
            super.postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect bounds = viewMeasurer.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bounds.width(), bounds.height());
    }

    @Override
    public int getSuggestedMinimumHeight() {
        return Math.max(drawingController.getMinHeight(), super.getSuggestedMinimumHeight());
    }

    @Override
    public int getSuggestedMinimumWidth() {
        return Math.max(drawingController.getMinWidth(), super.getSuggestedMinimumWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawingController.draw(canvas);
    }

    public void setProgress(float progressPercent) {
        drawingController.setProgress(progressPercent, true);
        invalidate();
    }

    public void initProgress(float progress) {
        drawingController.setProgress(progress, false);
    }

    public float getProgress() {
        return drawingController.getProgress();
    }
}
