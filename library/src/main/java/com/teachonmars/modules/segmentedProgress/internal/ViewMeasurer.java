package com.teachonmars.modules.segmentedProgress.internal;

import android.graphics.Rect;
import android.view.View.MeasureSpec;

import com.teachonmars.modules.segmentedProgress.SegmentedProgress;

public class ViewMeasurer {
    private final SegmentedProgress view;
    private final DrawingController drawingController;
    private Rect currentBound = new Rect();

    public ViewMeasurer(SegmentedProgress view, DrawingController drawingController) {
        this.view = view;
        this.drawingController = drawingController;
    }

    public Rect measure(int widthMeasureSpec, int heightMeasureSpec) {
        currentBound.set(0, 0, measuredWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        drawingController.setBound(currentBound);
        return currentBound;
    }

    private int measureHeight(int heightMeasureSpec) {
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                return measureHeightContent();
            case MeasureSpec.AT_MOST:
                return Math.min(getParentRequestedSize(heightMeasureSpec), measureHeightContent());
            case MeasureSpec.EXACTLY:
            default:
                return getParentRequestedSize(heightMeasureSpec);
        }
    }

    private int measuredWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return measuredWidthContent();
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
            default:
                return getParentRequestedSize(widthMeasureSpec);
        }
    }

    private int getParentRequestedSize(int sizeMeasureSpec) {
        return MeasureSpec.getSize(sizeMeasureSpec);
    }

    private int measuredWidthContent() {
        return Math.max(drawingController.getWidth(), view.getSuggestedMinimumWidth());
    }

    private int measureHeightContent() {
        return Math.max(drawingController.getHeight(), view.getSuggestedMinimumHeight());
    }

}
