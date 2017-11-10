package com.teachonmars.modules.segmentedProgress.appDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.teachonmars.modules.segmentedProgress.SegmentedProgress;

import java.util.ArrayList;
import java.util.Random;

import static com.teachonmars.modules.segmentedProgress.appDemo.R.id.progressView;


public class MainActivity extends AppCompatActivity {

    int duration = 2000;
    private SeekBar seek;
    private ArrayList<SegmentedProgress> seeklisteners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSeek();
        initProgress(progressView);
        initProgress(R.id.progressView2);
        initProgressAuto(R.id.progressView3);
        initProgressAuto(R.id.progressView4);
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

    private void initSeek() {
        seek = findViewById(R.id.seek);
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
