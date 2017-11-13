package com.teachonmars.modules.segmentedProgress.internal;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;

import com.teachonmars.modules.segmentedProgress.SegmentedProgress;
import com.teachonmars.modules.segmentedProgress.internal.drawing.AnimationManager;
import com.teachonmars.modules.segmentedProgress.internal.drawing.DrawableManager;
import com.teachonmars.modules.segmentedProgress.internal.drawing.SectionsCounter;

public class DrawingController {
    private final DrawableManager   drawableManager;
    private       SectionsCounter   sectionsCounter;
    private       AnimationManager  animationManager;
    private       SegmentedProgress view;

    public DrawingController(SegmentedProgress view) {
        this.view = view;
        sectionsCounter = new SectionsCounter();
        drawableManager = new DrawableManager(view);
        animationManager = new AnimationManager(drawableManager, sectionsCounter);
    }

    public void loadFromResources(TypedArray a, boolean inEditMode) {
        sectionsCounter.loadFromResources(a, inEditMode);
        drawableManager.loadFromResources(a, inEditMode);
    }

    public void setImageDrawable(Drawable drawable) {
        drawableManager.setImageDrawable(drawable);
    }

    public void setDrawable(int resId) {
        drawableManager.setDrawable(resId);
    }

    public boolean isPreventInvalidation() {
        return drawableManager.isPreventInvalidation();
    }

    public int getMinHeight() {
        return drawableManager.getMinHeight();
    }

    public int getMinWidth() {
        return drawableManager.getMinWidth() * sectionsCounter.getCount();
    }

    int getWidth() {
        return drawableManager.getIntrinsicWidth() * sectionsCounter.getCount();
    }

    int getHeight() {
        return drawableManager.getIntrinsicHeight();
    }

    public void draw(Canvas canvas) {
        int nbVisibleSection = sectionsCounter.getShowCount();
        int imageWidth = drawableManager.getWidth();
        drawBackground(canvas, nbVisibleSection, imageWidth);
        drawVisible(canvas, nbVisibleSection, imageWidth);
        drawProgress(canvas, imageWidth);
    }

    private void drawBackground(Canvas canvas, int nbVisibleSection, int imageWidth) {
        int total = sectionsCounter.getCount();
        if (nbVisibleSection != total) {
            drawableManager.setState(DrawableManager.STATE_SHOW_BACK);
            canvas.save();
            translateCanvasPadded(canvas, nbVisibleSection * imageWidth, 0);
            for (int i = nbVisibleSection; i < total; i++) {
                drawableManager.draw(canvas);
                translateCanvas(canvas, imageWidth, 0);
            }
            canvas.restore();
        }
    }

    private void drawVisible(Canvas canvas, int nbVisibleSection, int imageWidth) {
        if (nbVisibleSection > 0) {
            drawableManager.setState(DrawableManager.STATE_SHOW_FRONT);
            canvas.save();
            translateCanvasPadded(canvas, 0, 0);
            for (int i = 0; i < nbVisibleSection; i++) {
                drawableManager.draw(canvas);
                translateCanvas(canvas, imageWidth, 0);
            }
            canvas.restore();
        }
    }

    private void drawProgress(Canvas canvas, int imageWidth) {
        if (animationManager.isAnimating()) {
            int currentAnimatedPos = animationManager.getAnimCurrentSectionPos();
            if (currentAnimatedPos > 0) {
                canvas.save();
                translateCanvasPadded(canvas, (currentAnimatedPos - 1) * imageWidth, 0);
                drawableManager.drawProgress(canvas);
                canvas.restore();
            }
        }
    }

    private void translateCanvasPadded(Canvas canvas, int left, int top) {
        translateCanvas(canvas, ViewCompat.getPaddingStart(view) + left, view.getPaddingTop() + top);
    }

    private void translateCanvas(Canvas canvas, int left, int top) {
        canvas.translate(left, top);
    }

    void setBound(Rect viewBound) {
        int availableWidth = viewBound.width() - ViewCompat.getPaddingStart(view) - ViewCompat.getPaddingEnd(view);
        int availableHeight = viewBound.height() - view.getPaddingTop() - ViewCompat.getPaddingEnd(view);
        drawableManager.setBounds(new Rect(0, 0, availableWidth / sectionsCounter.getCount(), availableHeight));
    }

    public void setProgress(float progressPercent, boolean doAnim) {
        animationManager.interrupt(true);
        if (doAnim) {
            animationManager.animProgress(progressPercent, view);
        } else {
            updateProgress(progressPercent);
        }
    }

    private void updateProgress(float progressPercent) {
        int oldCount = sectionsCounter.getShowCount();
        int newCount = sectionsCounter.setProgress(progressPercent);
        if (newCount < oldCount) {
            int tmp = oldCount;
            oldCount = newCount;
            newCount = tmp;
        }
        Rect bounds = drawableManager.getBounds();
        if (bounds != null && bounds.width() != 0) {
            bounds.set(oldCount * bounds.width(), view.getPaddingTop(), newCount * bounds.width(), view.getPaddingBottom() + bounds.height());
            view.invalidate(bounds);
        }
    }

    public float getProgress() {
        return sectionsCounter.getShowCount() / sectionsCounter.getCount();
    }

    public int getShowCount() {
        return sectionsCounter.getShowCount();
    }

    public void setNbSection(int nbSection) {
        sectionsCounter.setNbSection(nbSection);
        view.requestLayout();
    }
}
