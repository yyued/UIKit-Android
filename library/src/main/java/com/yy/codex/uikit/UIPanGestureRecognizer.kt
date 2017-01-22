package com.yy.codex.uikit

import com.yy.codex.foundation.NSLog

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

open class UIPanGestureRecognizer : UIGestureRecognizer {

    internal var mMaxTouches: List<UITouch> = listOf()
    internal var mTranslatePoint: List<CGPoint> = listOf()
    internal var mVelocityPoint = CGPoint(0.0, 0.0)

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: Array<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        if (touches.size > 1) {
            state = UIGestureRecognizerState.Failed
            return
        }
        mMaxTouches = touches.toList()
        setTranslation(CGPoint(0.0, 0.0))
    }

    override fun touchesMoved(touches: Array<UITouch>, event: UIEvent) {
        if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            resetVelocity(touches)
        }
        super.touchesMoved(touches, event)
        if (state == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            setTranslation(CGPoint(-translation().x, -translation().y))
            state = UIGestureRecognizerState.Began
            sendActions()
        } else if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            if (touches.size > mMaxTouches.size) {
                mMaxTouches = touches.toList()
                bonusTranslation()
            } else if (touches.size < mMaxTouches.size) {
                state = UIGestureRecognizerState.Ended
                sendActions()
                return
            }
            state = UIGestureRecognizerState.Changed
            sendActions()
        }
    }

    override fun touchesEnded(touches: Array<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            state = UIGestureRecognizerState.Ended
            sendActions()
        } else if (state == UIGestureRecognizerState.Ended) {
            // Because as least one finger touch up, turns Ended during Moved.
        } else {
            state = UIGestureRecognizerState.Failed
        }
    }

    fun translation(): CGPoint {
        if (lastPoints.size > 0 && lastPoints.size == mTranslatePoint.size) {
            var sumX = 0.0
            var sumY = 0.0
            for (i in 0..lastPoints.size - 1) {
                sumX += lastPoints[i].absolutePoint.x - mTranslatePoint[i].x
                sumY += lastPoints[i].absolutePoint.y - mTranslatePoint[i].y
            }
            return CGPoint(sumX, sumY)
        }
        return CGPoint(0.0, 0.0)
    }

    fun setTranslation(point: CGPoint) {
        if (lastPoints.size > 0) {
            mTranslatePoint = lastPoints.map { CGPoint(it.absolutePoint.x + point.x, it.absolutePoint.y + point.y) }
        }
    }

    fun bonusTranslation() {
        if (mTranslatePoint.size < lastPoints.size) {
            val translatePoint: MutableList<CGPoint> = mutableListOf()
            for (i in 0..lastPoints.size - 1) {
                if (i < mTranslatePoint.size) {
                    translatePoint[i] = mTranslatePoint[i]
                } else {
                    translatePoint[i] = lastPoints[i].absolutePoint
                }
            }
            mTranslatePoint = translatePoint.toList()
        }
    }

    fun velocity(): CGPoint {
        return mVelocityPoint
    }

    private fun resetVelocity(nextTouches: Array<UITouch>) {
        if (lastPoints.size > 0 && nextTouches.size > 0) {
            val ts = (nextTouches[0].timestamp - lastPoints[0].timestamp).toDouble() / 1000.0
            if (ts == 0.0) {
            } else {
                val vx = (nextTouches[0].absolutePoint.x - lastPoints[0].absolutePoint.x) / ts
                val vy = (nextTouches[0].absolutePoint.y - lastPoints[0].absolutePoint.y) / ts
                mVelocityPoint = CGPoint(vx, vy)
            }
        }
    }

    private fun moveOutOfBounds(touches: Array<UITouch>): Boolean {
        val view = view ?: return true
        var accepted = 0
        val allowableMovement = 8.0
        for (i in touches.indices) {
            val p0 = touches[i].locationInView(view)
            for (j in mMaxTouches.indices) {
                val p1 = mMaxTouches[j].locationInView(view)
                if (!p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++
                    break
                }
            }
        }
        return accepted > 0
    }

}
