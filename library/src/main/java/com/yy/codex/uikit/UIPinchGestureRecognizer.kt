package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/13.
 */

class UIPinchGestureRecognizer : UIGestureRecognizer {

    private var scaleCurrent = 1.0
    private var scaleCurrentPoints: List<CGPoint> = listOf()
    private var scaleInitial = 1.0
    private var scaleInitialPoints: List<CGPoint> = listOf()

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
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

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
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
        get() = scaleCurrent
        set(scale) {
            scaleCurrent = scale
            scaleInitial = scale
            resetScaleInitialPoints()
        }

    private fun resetScale() {
        if (lastPoints.size >= 2) {
            scaleCurrentPoints = listOf(lastPoints[0].absolutePoint, lastPoints[1].absolutePoint)
        }
        if (scaleInitialPoints.count() >= 2) {
            val initialLength = Math.sqrt(Math.pow(scaleInitialPoints[0].x - scaleInitialPoints[1].x, 2.0) + Math.pow(scaleInitialPoints[0].y - scaleInitialPoints[1].y, 2.0))
            val currentLength = Math.sqrt(Math.pow(scaleCurrentPoints[0].x - scaleCurrentPoints[1].x, 2.0) + Math.pow(scaleCurrentPoints[0].y - scaleCurrentPoints[1].y, 2.0))
            val screenLength = Math.sqrt(Math.pow(UIScreen.mainScreen.bounds().size.width, 2.0) + Math.pow(UIScreen.mainScreen.bounds().size.height, 2.0))
            scaleCurrent = scaleInitial + (currentLength - initialLength) / screenLength
        }
    }

    private fun resetScaleInitialPoints() {
        if (lastPoints.size >= 2) {
            scaleInitialPoints = listOf(lastPoints[0].absolutePoint, lastPoints[1].absolutePoint)
        }
    }

}
