package com.teachonmars.modules.segmentedProgress.internal.drawing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;

import com.teachonmars.modules.segmentedProgress.SegmentedProgress;

import java.util.ArrayList;

public class AnimationManager {
    private AnimatorSet     levelAnimator;
    private DrawableManager drawableManager;
    private SectionsCounter sectionsCounter;
    private int currenSectionPos = 0;

    public AnimationManager(DrawableManager drawableManager, SectionsCounter sectionsCounter) {
        this.drawableManager = drawableManager;

        this.sectionsCounter = sectionsCounter;
    }

    public boolean isAnimating() {
        return levelAnimator != null && levelAnimator.isRunning();
    }

    public void interrupt(boolean goToEnd) {
        if (levelAnimator != null) {
            if (goToEnd) {
                levelAnimator.end();
            } else {
                levelAnimator.cancel();
            }
        }
    }

    public void animProgress(final float progressPercent, final SegmentedProgress view) {
        final ClipDrawable drawable = drawableManager.getProgressDrawable();
        int start = sectionsCounter.getShowCount();
        int end = sectionsCounter.calculateFuturCount(progressPercent);
        if (start != end) {
            ArrayList<Animator> items = new ArrayList<>();
            drawable.setState(DrawableManager.STATE_SHOW_FRONT);
            if (start < end) {
                for (int i = start + 1; i <= end; i++) {
                    items.add(buildAnim(drawable, 0, 10000, view, i));
                }
            } else {
                for (int i = start; i > end; i--) {
                    items.add(buildAnim(drawable, 10000, 0, view, i));
                }
            }
            levelAnimator = new AnimatorSet();
            levelAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currenSectionPos = 0;
                }
            });
            levelAnimator.playSequentially(items);
            levelAnimator.start();
        }
    }

    private ObjectAnimator buildAnim(final Drawable drawable, final int min, final int max, final SegmentedProgress view, final int pos) {
        ObjectAnimator result = ObjectAnimator.ofInt(drawable, "level", min, max);
        result.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                currenSectionPos = pos;
                if (max == 0) {
                    sectionsCounter.setShowCount(currenSectionPos - 1);
                    view.invalidate();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (min == 0) {
                    sectionsCounter.setShowCount(currenSectionPos);
                    view.invalidate();
                }
            }
        });
        result.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.invalidate();
            }
        });
        return result;
    }

    public int getAnimCurrentSectionPos() {
        return currenSectionPos;
    }
}
