package com.yy.codex.uikit

import android.os.Handler

import java.util.Timer
import java.util.TimerTask

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

class UILongPressGestureRecognizer : UIGestureRecognizer {

    private var mStartTouches: List<UITouch> = listOf()
    private var mStartTimer: Timer? = null

    var mMinimumPressDuration = 0.5
    var mAllowableMovement = 10.0
    var mNumberOfTouchesRequired = 1

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        mStartTouches = touches.toList()
        if (touches.size >= mNumberOfTouchesRequired) {
            setupTimer()
        }
    }

    private fun setupTimer() {
        val self = this
        val handler = Handler()
        mStartTimer = Timer()
        mStartTimer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (state != UIGestureRecognizerState.Failed) {
                        state = UIGestureRecognizerState.Began
                        sendActions()
                        val looper = looper
                        if (looper != null) {
                            looper.checkState(self)
                            looper.markFailed()
                        }
                    }
                }
            }
        }, (mMinimumPressDuration * 1000).toLong())
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        super.touchesMoved(touches, event)
        if (state == UIGestureRecognizerState.Possible && touches.size > mStartTouches.size && touches.size >= mNumberOfTouchesRequired) {
            mStartTouches = touches.toList()
            setupTimer()
        } else if (state == UIGestureRecognizerState.Possible && mStartTouches.size >= mNumberOfTouchesRequired && moveOutOfBounds(touches)) {
            state = UIGestureRecognizerState.Failed
            mStartTimer?.let(Timer::cancel)
        } else if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            state = UIGestureRecognizerState.Changed
            sendActions()
        }
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        mStartTimer?.let(Timer::cancel)
        if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            state = UIGestureRecognizerState.Ended
            sendActions()
        } else {
            state = UIGestureRecognizerState.Failed
        }
    }

    private fun moveOutOfBounds(touches: List<UITouch>): Boolean {
        val view = view ?: return true
        var accepted = 0
        for (i in touches.indices) {
            val p0 = touches[i].locationInView(view)
            for (j in mStartTouches.indices) {
                val p1 = mStartTouches[j].locationInView(view)
                if (p0.inRange(mAllowableMovement, mAllowableMovement, p1)) {
                    accepted++
                    break
                }
            }
        }
        return accepted < mNumberOfTouchesRequired
    }

}
