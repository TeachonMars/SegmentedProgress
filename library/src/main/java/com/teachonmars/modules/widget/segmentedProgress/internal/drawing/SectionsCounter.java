package com.teachonmars.modules.widget.segmentedProgress.internal.drawing;

import android.content.res.TypedArray;

import com.teachonmars.modules.widget.segmentedProgress.R;

public class SectionsCounter {
    private int nbSection;
    private int showCount = 2;

    public SectionsCounter() {
    }

    public int getCount() {
        return nbSection;
    }

    public int getShowCount() {
        return showCount;
    }

    public void loadFromResources(TypedArray a, boolean inEditMode) {
        if (!inEditMode) {
            this.nbSection = a.getInt(R.styleable.SegmentedProgress_nbSection, R.integer.segmentedProgressDefaults_nbSection);
        } else {
            this.nbSection = 5;
        }
    }

    int calculateFuturCount(float progressPercent) {
        int newCount = (int) (progressPercent * (float) nbSection);
        return Math.min(newCount, nbSection);
    }

    void setShowCount(int showCount) {
        this.showCount = showCount <= nbSection ? showCount : nbSection;
    }

    public int setProgress(float progressPercent) {
        showCount = calculateFuturCount(progressPercent);
        return showCount;
    }

    public void setNbSection(int nbSection) {
        if (nbSection < 1) {
            nbSection = 1;
        }
        if (showCount > nbSection) {
            showCount = nbSection;
        }
        this.nbSection = nbSection;
    }
}
