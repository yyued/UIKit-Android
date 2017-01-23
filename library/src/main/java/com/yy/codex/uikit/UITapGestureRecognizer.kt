package com.yy.codex.uikit

import java.util.Timer
import java.util.TimerTask

/**
 * Created by cuiminghui on 2017/1/11.
 */

class UITapGestureRecognizer : UIGestureRecognizer {

    private var mStartTouches: List<UITouch> = listOf()

    var numberOfTapsRequired = 1
    var numberOfTouchesRequired = 1

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        mStartTouches = touches.toList()
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        super.touchesMoved(touches, event)
        if (touches.size > mStartTouches.size) {
            mStartTouches = touches.toList()
        }
        if (moveOutOfBounds(touches)) {
            state = UIGestureRecognizerState.Failed
        }
    }

    private var multiTapTimer: Timer? = null

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        if (state == UIGestureRecognizerState.Failed) {
            return
        }
        var tapCountPass = false
        var touchCountPass = false
        for (i in touches.indices) {
            if (touches[i].tapCount == numberOfTapsRequired) {
                tapCountPass = true
            }
        }
        if (mStartTouches.size >= numberOfTouchesRequired) {
            touchCountPass = true
        }

        if (tapCountPass && touchCountPass) {
            multiTapTimer?.let(Timer::cancel)
            if (mGestureRecognizersRequiresFailed.count() > 0) {
                waitOtherGesture(Runnable {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                })
            } else {
                state = UIGestureRecognizerState.Ended
                sendActions()
            }
        } else {
            if (numberOfTapsRequired > 1) {
                multiTapTimer?.let(Timer::cancel)
                multiTapTimer = Timer()
                multiTapTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        state = UIGestureRecognizerState.Failed
                    }
                }, 300)
            } else {
                state = UIGestureRecognizerState.Failed
            }
        }
    }

    private fun moveOutOfBounds(touches: List<UITouch>): Boolean {
        val view = view ?: return true
        val allowableMovement = 12.0
        var accepted = 0
        for (i in touches.indices) {
            val p0 = touches[i].locationInView(view)
            for (j in mStartTouches.indices) {
                val p1 = mStartTouches[j].locationInView(view)
                if (p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++
                    break
                }
            }
        }
        return accepted < numberOfTouchesRequired
    }

    override fun gesturePriority(): Int {
        return numberOfTapsRequired
    }

}
