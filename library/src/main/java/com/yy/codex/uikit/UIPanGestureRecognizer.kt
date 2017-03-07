package com.yy.codex.uikit

import com.yy.codex.foundation.NSLog

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

open class UIPanGestureRecognizer : UIGestureRecognizer {

    internal var maxTouches: List<UITouch> = listOf()
    internal var translatePoint: List<CGPoint> = listOf()
    internal var velocityPoint = CGPoint(0.0, 0.0)
    internal var lastTouches: List<UITouch> = listOf()

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        if (touches.size > 1) {
            state = UIGestureRecognizerState.Failed
            return
        }
        maxTouches = touches.toList()
        setTranslation(CGPoint(0.0, 0.0))
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        if (lastTouches.count() == touches.count()) {
            var same = true
            lastTouches.forEachIndexed { idx, oldTouch ->
                val newTouch = touches[idx]
                if (Math.abs(oldTouch.absolutePoint.x - newTouch.absolutePoint.x) >= 1.0 || Math.abs(oldTouch.absolutePoint.y - newTouch.absolutePoint.y) >= 1.0) {
                    same = false
                }
            }
            if (same) {
                return
            }
        }
        lastTouches = touches
        if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            resetVelocity(touches)
        }
        super.touchesMoved(touches, event)
        if (state == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            setTranslation(CGPoint(-translation().x, -translation().y))
            state = UIGestureRecognizerState.Began
            sendActions()
        } else if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            if (touches.size > maxTouches.size) {
                maxTouches = touches.toList()
                bonusTranslation()
            } else if (touches.size < maxTouches.size) {
                state = UIGestureRecognizerState.Ended
                sendActions()
                return
            }
            state = UIGestureRecognizerState.Changed
            sendActions()
        }
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
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
        if (lastPoints.size > 0 && lastPoints.size == translatePoint.size) {
            var sumX = 0.0
            var sumY = 0.0
            for (i in 0..lastPoints.size - 1) {
                sumX += lastPoints[i].absolutePoint.x - translatePoint[i].x
                sumY += lastPoints[i].absolutePoint.y - translatePoint[i].y
            }
            return CGPoint(sumX, sumY)
        }
        return CGPoint(0.0, 0.0)
    }

    fun setTranslation(point: CGPoint) {
        if (lastPoints.size > 0) {
            translatePoint = lastPoints.map { CGPoint(it.absolutePoint.x + point.x, it.absolutePoint.y + point.y) }
        }
    }

    fun bonusTranslation() {
        if (translatePoint.size < lastPoints.size) {
            val translatePoint: MutableList<CGPoint> = mutableListOf()
            for (i in 0..lastPoints.size - 1) {
                if (i < this.translatePoint.size) {
                    translatePoint.add(this.translatePoint[i])
                } else {
                    translatePoint.add(lastPoints[i].absolutePoint)
                }
            }
            this.translatePoint = translatePoint.toList()
        }
    }

    fun velocity(): CGPoint {
        return velocityPoint
    }

    private fun resetVelocity(nextTouches: List<UITouch>) {
        if (lastPoints.size > 0 && nextTouches.size > 0) {
            val ts = (nextTouches[0].timestamp - lastPoints[0].timestamp).toDouble() / 1000.0
            if (ts == 0.0) {
            } else {
                val vx = (nextTouches[0].absolutePoint.x - lastPoints[0].absolutePoint.x) / ts
                val vy = (nextTouches[0].absolutePoint.y - lastPoints[0].absolutePoint.y) / ts
                velocityPoint = CGPoint(vx, vy)
            }
        }
    }

    private fun moveOutOfBounds(touches: List<UITouch>): Boolean {
        val view = view ?: return true
        var accepted = 0
        val allowableMovement = 8.0
        for (i in touches.indices) {
            val p0 = touches[i].locationInView(view)
            for (j in maxTouches.indices) {
                val p1 = maxTouches[j].locationInView(view)
                if (!p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++
                    break
                }
            }
        }
        return accepted > 0
    }

}
