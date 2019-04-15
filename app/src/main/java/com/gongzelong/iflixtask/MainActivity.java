package com.gongzelong.iflixtask;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        Pi.OnPiChangedInterface, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String PI_SCALE = "pi_scale";
    private static final String PI_ELAPSE_TIME = "pi_elapse_time";
    private static final String PI_IS_PAUSE = "pi_elapse_time";
    private static final String PI_IS_RESET = "pi_elapse_time";
    TextView mResultTv;
    TextView mPauseTv;
    TextView mResetTv;
    TextView mStartTv;
    TextView mElapsedTimeTv;
    private Pi mPi;
    private boolean mIsPause = false;
    private boolean mIsReset = false;
    private int mScale = 1;
    private long mElapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mScale = savedInstanceState.getInt(PI_SCALE);
            mElapsedTime = savedInstanceState.getLong(PI_ELAPSE_TIME);
            mIsPause = savedInstanceState.getBoolean(PI_IS_PAUSE);
            mIsReset = savedInstanceState.getBoolean(PI_IS_RESET);
        }

        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        mPi = new Pi();
        mPi.setOnPiChangedInterface(MainActivity.this);

        mResultTv = findViewById(R.id.result);
        mPauseTv = findViewById(R.id.pause);
        mResetTv = findViewById(R.id.reset);
        mStartTv = findViewById(R.id.start);
        mElapsedTimeTv = findViewById(R.id.elapsed_time);
        mPauseTv.setOnClickListener(this);
        mResetTv.setOnClickListener(this);
        mStartTv.setOnClickListener(this);
    }

    private void getPi() {
        startLoop(mPi);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PI_SCALE, mScale);
        outState.putLong(PI_ELAPSE_TIME, mElapsedTime);
        outState.putBoolean(PI_IS_PAUSE, mIsPause);
        outState.putBoolean(PI_IS_RESET, mIsReset);
        super.onSaveInstanceState(outState);
    }


    private void startLoop(@NonNull final Pi pi) {
        while (mScale > 0) {
            long startTime = new Date(System.currentTimeMillis()).getTime();
            if (mIsPause) break;
            if (mIsReset) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResultTv.setText("0");
                        mElapsedTimeTv.setText(R.string.elapse_time_zero);
                        mScale = 1;
                    }
                });
                break;
            }
            pi.pi(mScale);

            try {
                Thread.sleep(1000);

                long endTime = new Date(System.currentTimeMillis()).getTime();
                mElapsedTime += endTime - startTime;
                final String elapsedTimeString = mElapsedTime / 1000 + "s";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mElapsedTimeTv.setText(String.format("%s%s",
                                getString(R.string.title_elapse_time), elapsedTimeString));

                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mScale += 1;
        }
    }

    @Override
    public void onPiChanged(final String pi) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResultTv.setText(pi);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                if (v.isSelected()) {
                    Log.d(TAG, "click resume");
                    v.setSelected(false);
                    mPauseTv.setText(R.string.pause_text);
                    mIsPause = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getPi();
                        }
                    }).start();
                } else {
                    Log.d(TAG, "click pause");
                    v.setSelected(true);
                    ((TextView) v).setText(R.string.resume_text);
                    mIsPause = true;
                }
                break;
            case R.id.reset:
                Log.d(TAG, "click reset");
                mScale = 1;
                mElapsedTime = 0L;
                mIsReset = true;
                mIsPause = false;
                mPauseTv.setSelected(false);
                mPauseTv.setText(R.string.pause_text);
                mResultTv.setText("0");
                mElapsedTimeTv.setText(R.string.elapse_time_zero);
                break;
            case R.id.start:
                Log.d(TAG, "click start");
                mIsReset = false;
                mIsPause = false;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPi();
                    }
                }).start();
                break;
        }
    }
}
