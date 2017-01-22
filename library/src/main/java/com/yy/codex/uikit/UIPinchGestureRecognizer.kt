package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/13.
 */

class UIPinchGestureRecognizer : UIGestureRecognizer {

    private var mScaleCurrent = 1.0
    private var mScaleCurrentPoints: List<CGPoint> = listOf()
    private var mScaleInitial = 1.0
    private var mScaleInitialPoints: List<CGPoint> = listOf()

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: Array<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
    }

    override fun touchesMoved(touches: Array<UITouch>, event: UIEvent) {
        super.touchesMoved(touches, event)
        if (state == UIGestureRecognizerState.Possible) {
            if (touches.size < 2) {
                return
            }
            if (touches.size != 2) {
                state = UIGestureRecognizerState.Failed
                return
            }
            resetScaleInitialPoints()
            resetScale()
            state = UIGestureRecognizerState.Began
            sendActions()
        } else if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            state = UIGestureRecognizerState.Changed
            resetScale()
            sendActions()
        }
    }

    override fun touchesEnded(touches: Array<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        if (state == UIGestureRecognizerState.Began || state == UIGestureRecognizerState.Changed) {
            state = UIGestureRecognizerState.Ended
            sendActions()
        }
    }

    override fun gesturePriority(): Int {
        return super.gesturePriority() + 99
    }

    var scale: Double
        get() = mScaleCurrent
        set(scale) {
            mScaleCurrent = scale
            mScaleInitial = scale
            resetScaleInitialPoints()
        }

    private fun resetScale() {
        if (lastPoints.size >= 2) {
            mScaleCurrentPoints = listOf(lastPoints[0].absolutePoint, lastPoints[1].absolutePoint)
        }
        if (mScaleInitialPoints.count() >= 2) {
            val initialLength = Math.sqrt(Math.pow(mScaleInitialPoints[0].x - mScaleInitialPoints[1].x, 2.0) + Math.pow(mScaleInitialPoints[0].y - mScaleInitialPoints[1].y, 2.0))
            val currentLength = Math.sqrt(Math.pow(mScaleCurrentPoints[0].x - mScaleCurrentPoints[1].x, 2.0) + Math.pow(mScaleCurrentPoints[0].y - mScaleCurrentPoints[1].y, 2.0))
            val screenLength = Math.sqrt(Math.pow(UIScreen.mainScreen.bounds().size.width, 2.0) + Math.pow(UIScreen.mainScreen.bounds().size.height, 2.0))
            mScaleCurrent = mScaleInitial + (currentLength - initialLength) / screenLength
        }
    }

    private fun resetScaleInitialPoints() {
        if (lastPoints.size >= 2) {
            mScaleInitialPoints = listOf(lastPoints[0].absolutePoint, lastPoints[1].absolutePoint)
        }
    }

}
