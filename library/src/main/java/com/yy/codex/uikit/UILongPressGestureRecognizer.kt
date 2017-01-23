package com.yy.codex.uikit

import android.os.Handler

import java.util.Timer
import java.util.TimerTask

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

class UILongPressGestureRecognizer : UIGestureRecognizer {

    private var startTouches: List<UITouch> = listOf()
    private var startTimer: Timer? = null

    var minimumPressDuration = 0.5
    var allowableMovement = 10.0
    var numberOfTouchesRequired = 1

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        startTouches = touches.toList()
        if (touches.size >= numberOfTouchesRequired) {
            setupTimer()
        }
    }

    private fun setupTimer() {
        val self = this
        val handler = Handler()
        startTimer = Timer()
        startTimer?.schedule(object : TimerTask() {
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
        }, (minimumPressDuration * 1000).toLong())
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        super.touchesMoved(touches, event)
        if (state == UIGestureRecognizerState.Possible && touches.size > startTouches.size && touches.size >= numberOfTouchesRequired) {
            startTouches = touches.toList()
            setupTimer()
        } else if (state == UIGestureRecognizerState.Possible && startTouches.size >= numberOfTouchesRequired && moveOutOfBounds(touches)) {
            state = UIGestureRecognizerState.Failed
            startTimer?.let(Timer::cancel)
        } else if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            state = UIGestureRecognizerState.Changed
            sendActions()
        }
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        startTimer?.let(Timer::cancel)
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
            for (j in startTouches.indices) {
                val p1 = startTouches[j].locationInView(view)
                if (p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++
                    break
                }
            }
        }
        return accepted < numberOfTouchesRequired
    }

}
