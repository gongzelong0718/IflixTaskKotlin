package com.gongzelong.iflixtask

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

import java.util.Date

class MainActivity : AppCompatActivity(), Pi.OnPiChangedInterface, View.OnClickListener {
    private lateinit var mResultTv: TextView
    private lateinit var mPauseTv: TextView
    private lateinit var mResetTv: TextView
    private lateinit var mStartTv: TextView
    private lateinit var mElapsedTimeTv: TextView
    private var mPi: Pi? = null
    private var mIsPause = false
    private var mIsReset = false
    private var mScale = 1
    private var mElapsedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mScale = savedInstanceState.getInt(PI_SCALE)
            mElapsedTime = savedInstanceState.getLong(PI_ELAPSE_TIME)
            mIsPause = savedInstanceState.getBoolean(PI_IS_PAUSE)
            mIsReset = savedInstanceState.getBoolean(PI_IS_RESET)
        }

        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//强制竖屏

        mPi = Pi()
        mPi!!.setOnPiChangedInterface(this@MainActivity)

        mResultTv = findViewById(R.id.result)
        mPauseTv = findViewById(R.id.pause)
        mResetTv = findViewById(R.id.reset)
        mStartTv = findViewById(R.id.start)
        mElapsedTimeTv = findViewById(R.id.elapsed_time)
        mPauseTv.setOnClickListener(this)
        mResetTv.setOnClickListener(this)
        mStartTv.setOnClickListener(this)
    }

    private fun getPi() {
        startLoop(mPi!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PI_SCALE, mScale)
        outState.putLong(PI_ELAPSE_TIME, mElapsedTime)
        outState.putBoolean(PI_IS_PAUSE, mIsPause)
        outState.putBoolean(PI_IS_RESET, mIsReset)
        super.onSaveInstanceState(outState)
    }


    private fun startLoop(pi: Pi) {
        while (mScale > 0) {
            val startTime = Date(System.currentTimeMillis()).time
            if (mIsPause) break
            if (mIsReset) {
                runOnUiThread {
                    mResultTv.text = "0"
                    mElapsedTimeTv.setText(R.string.elapse_time_zero)
                    mScale = 1
                }
                break
            }
            pi.pi(mScale)

            try {
                Thread.sleep(1000)

                val endTime = Date(System.currentTimeMillis()).time
                mElapsedTime += endTime - startTime
                val elapsedTimeString = (mElapsedTime / 1000).toString() + "s"
                runOnUiThread {
                    mElapsedTimeTv.text = String.format("%s%s",
                            getString(R.string.title_elapse_time), elapsedTimeString)
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            mScale += 1
        }
    }

    override fun onPiChanged(pi: String) {
        runOnUiThread { mResultTv.text = pi }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.pause -> if (v.isSelected) {
                Log.d(TAG, "click resume")
                v.isSelected = false
                mPauseTv.setText(R.string.pause_text)
                mIsPause = false
                Thread(Runnable { getPi() }).start()
            } else {
                Log.d(TAG, "click pause")
                v.isSelected = true
                (v as TextView).setText(R.string.resume_text)
                mIsPause = true
            }
            R.id.reset -> {
                Log.d(TAG, "click reset")
                mScale = 1
                mElapsedTime = 0L
                mIsReset = true
                mIsPause = false
                mPauseTv.isSelected = false
                mPauseTv.setText(R.string.pause_text)
                mResultTv.text = "0"
                mElapsedTimeTv.setText(R.string.elapse_time_zero)
            }
            R.id.start -> {
                Log.d(TAG, "click start")
                mIsReset = false
                mIsPause = false

                Thread(Runnable { getPi() }).start()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PI_SCALE = "pi_scale"
        private const val PI_ELAPSE_TIME = "pi_elapse_time"
        private const val PI_IS_PAUSE = "pi_elapse_time"
        private const val PI_IS_RESET = "pi_elapse_time"
    }
}
