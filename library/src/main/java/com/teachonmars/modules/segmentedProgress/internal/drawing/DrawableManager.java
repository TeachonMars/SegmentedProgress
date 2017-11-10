package com.teachonmars.modules.segmentedProgress.internal.drawing;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;

import com.teachonmars.modules.segmentedProgress.R;
import com.teachonmars.modules.segmentedProgress.SegmentedProgress;

import static android.os.Build.VERSION.SDK_INT;

public class DrawableManager {
    public static final int[] STATE_SHOW_FRONT = new int[]{R.attr.segmentedProgressStyle_stateShowFront};
    public static final int[] STATE_SHOW_BACK  = new int[]{R.attr.segmentedProgressStyle_stateShowBack};
    private SegmentedProgress view;
    private StateListDrawable stateDrawable;
    private boolean           preventInvalidation;
    private int backgroundColor = Color.BLUE;
    private ClipDrawable progressDrawable;

    public DrawableManager(SegmentedProgress view) {
        this.view = view;
    }

    public void loadFromResources(TypedArray a) {
        preventInvalidation = true;
        retrieveImageDrawable(a, R.styleable.SegmentedProgress_android_src);
        preventInvalidation = false;
        backgroundColor = a.getColor(R.styleable.SegmentedProgress_colorEmpty,Color.GRAY);
    }

    public void draw(Canvas canvas) {
        if (stateDrawable != null) {
            stateDrawable.draw(canvas);
        }
    }

    public void setState(int[] state) {
        if (stateDrawable != null) {
            stateDrawable.setState(state);
        }
    }

    public void setDrawable(@DrawableRes int resId) {
        if (resId != 0) {
            final Drawable d = ContextCompat.getDrawable(view.getContext(), resId);
            setImageDrawable(d);
        } else {
            failImageNotProvided();
        }
    }

    public void setImageDrawable(Drawable drawable) {
        cleanDrawables();
        ensureNewDrawable(drawable);
        stateDrawable = buildStateDrawable(drawable);
        stateDrawable.setCallback(view);
        stateDrawable.setState(STATE_SHOW_FRONT);
        progressDrawable = buildProgressDrawable(stateDrawable);
        progressDrawable.setState(STATE_SHOW_FRONT);
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int layoutDirection = view.getLayoutDirection();
            DrawableCompat.setLayoutDirection(stateDrawable, layoutDirection);
            DrawableCompat.setLayoutDirection(progressDrawable, layoutDirection);
        }
    }

    private void retrieveImageDrawable(TypedArray array, int index) {
        if (array.hasValue(index)) {
            final int resId = array.getResourceId(index, 0);
            if (resId != 0) {
                setDrawable(resId);
            }
        } else {
            failImageNotProvided();
        }
    }

    private StateListDrawable buildStateDrawable(Drawable drawable) {
        StateListDrawable result = new StateListDrawable();
        result.addState(STATE_SHOW_FRONT, drawable);
        Drawable background = drawable.getConstantState().newDrawable().mutate();
        DrawableCompat.setTint(background, backgroundColor);
        result.addState(STATE_SHOW_BACK, background);

        return result;
    }

    private ClipDrawable buildProgressDrawable(Drawable drawable) {
        return new ClipDrawable(drawable.getConstantState().newDrawable().mutate(), GravityCompat.START, ClipDrawable.HORIZONTAL);
    }

    private void ensureNewDrawable(Drawable drawable) {
        if (drawable == null) {
            failImageNotProvided();
        }
    }

    private void cleanDrawables() {
        cleanDrawable(stateDrawable);
    }

    private void cleanDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
            view.unscheduleDrawable(drawable);
        }
    }

    private void failImageNotProvided() {
        throw new RuntimeException(view.getContext().getString(R.string.error_needAnimatedVector));
    }

    public boolean isPreventInvalidation() {
        return preventInvalidation;
    }


    public void setBounds(Rect bounds) {
        stateDrawable.setBounds(bounds);
        progressDrawable.setBounds(bounds);
    }

    public Rect getBounds() {
        if (stateDrawable != null) {
            return new Rect(stateDrawable.getBounds());
        }
        return null;
    }

    public int getIntrinsicHeight() {
        return stateDrawable != null ? stateDrawable.getIntrinsicHeight() : 0;
    }

    public int getIntrinsicWidth() {
        return stateDrawable != null ? stateDrawable.getIntrinsicWidth() : 0;
    }

    public int getMinHeight() {
        return stateDrawable != null ? stateDrawable.getMinimumHeight() : 0;
    }

    public int getMinWidth() {
        return stateDrawable != null ? stateDrawable.getMinimumWidth() : 0;
    }

    public int getWidth() {
        return stateDrawable != null ? stateDrawable.getBounds().width() : 0;
    }

    ClipDrawable getProgressDrawable() {
        return progressDrawable;
    }

    public void drawProgress(Canvas canvas) {
        if (progressDrawable != null) {
            progressDrawable.draw(canvas);
        }
    }
}
