package com.teachonmars.modules.widget.segmentedProgress.appDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.teachonmars.modules.widget.segmentedProgress.SegmentedProgress;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    int duration = 2000;
    private ArrayList<SegmentedProgress> seeklisteners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControllers();
        initProgress(R.id.progressView);
        initProgress(R.id.progressView2);
        initProgressAuto(R.id.progressView3);
        initProgressAuto(R.id.progressView4);
    }

    private void initControllers() {
        initProgressSeek();
        initNbSectionSeek();
    }

    private void initNbSectionSeek() {
        SeekBar nbSectionSeek = findViewById(R.id.nbSection);
        float currentValue = ((SegmentedProgress) findViewById(R.id.progressView)).getShowCount();
        nbSectionSeek.setProgress((int) currentValue);
        nbSectionSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int nbSection, boolean fromUser) {
                for (SegmentedProgress seeklistener : seeklisteners) {
                    seeklistener.setNbSection(nbSection);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initProgress(int progressViewId) {
        final SegmentedProgress progressView = findViewById(progressViewId);
        progressView.initProgress(0);
        seeklisteners.add(progressView);
    }

    private void initProgressAuto(int progressViewId) {
        final SegmentedProgress progressView = findViewById(progressViewId);
        progressView.initProgress(0);
        progressView.postDelayed(buildAutoChanger(progressView), duration);
    }

    private void initProgressSeek() {
        SeekBar seek = findViewById(R.id.progress);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (SegmentedProgress seeklistener : seeklisteners) {
                    seeklistener.setProgress(progress / 10f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Runnable buildAutoChanger(final SegmentedProgress progressView) {
        return new Runnable() {
            private Random randomizer = new Random();
            private float percent = 0f;

            @Override
            public void run() {
                progressView.setProgress(percent);
                percent = randomizer.nextFloat();
                progressView.postDelayed(this, duration);
            }
        };
    }
}
