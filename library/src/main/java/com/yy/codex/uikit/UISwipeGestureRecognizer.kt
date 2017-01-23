package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/13.
 */

class UISwipeGestureRecognizer : UIGestureRecognizer {

    enum class Direction {
        Right,
        Left,
        Up,
        Down
    }

    var direction = Direction.Right

    private var mOriginalPoint = CGPoint(0.0, 0.0)
    private var mVelocityPoint = CGPoint(0.0, 0.0)

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: Array<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        if (touches.size > 0) {
            mOriginalPoint = touches[0].absolutePoint
        } else {
            state = UIGestureRecognizerState.Failed
        }
    }

    override fun touchesMoved(touches: Array<UITouch>, event: UIEvent) {
        if (touches.size <= 0) {
            state = UIGestureRecognizerState.Failed
            return
        }
        if (state == UIGestureRecognizerState.Possible) {
            resetVelocity(touches)
        }
        super.touchesMoved(touches, event)
        if (direction == Direction.Right) {
            val distance = touches[0].absolutePoint.x - mOriginalPoint.x
            if (distance < -22.0) {
                state = UIGestureRecognizerState.Failed
            } else {
                if (distance > 100.0 && mVelocityPoint.x > 1000.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                } else if (mVelocityPoint.x > 500.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                }
            }
        } else if (direction == Direction.Left) {
            val distance = touches[0].absolutePoint.x - mOriginalPoint.x
            if (distance > 22.0) {
                state = UIGestureRecognizerState.Failed
            } else {
                if (distance < -100.0 && mVelocityPoint.x < -1000.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                } else if (mVelocityPoint.x < -500.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                }
            }
        } else if (direction == Direction.Down) {
            val distance = touches[0].absolutePoint.y - mOriginalPoint.y
            if (distance < -22.0) {
                state = UIGestureRecognizerState.Failed
            } else {
                if (distance > 100.0 && mVelocityPoint.y > 1000.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                } else if (mVelocityPoint.y > 500.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                }
            }
        } else if (direction == Direction.Up) {
            val distance = touches[0].absolutePoint.y - mOriginalPoint.y
            if (distance > 22.0) {
                state = UIGestureRecognizerState.Failed
            } else {
                if (distance < -100.0 && mVelocityPoint.y < -1000.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                } else if (mVelocityPoint.y < -500.0) {
                    state = UIGestureRecognizerState.Ended
                    sendActions()
                }
            }
        }
    }

    override fun touchesEnded(touches: Array<UITouch>, event: UIEvent) {
        super.touchesEnded(touches, event)
        if (state != UIGestureRecognizerState.Ended) {
            state = UIGestureRecognizerState.Failed
        }
    }

    fun velocity(): CGPoint {
        return mVelocityPoint
    }

    private fun resetVelocity(nextTouches: Array<UITouch>) {
        if (lastPoints.size > 0 && nextTouches.size > 0) {
            val ts = (nextTouches[0].timestamp - lastPoints[0].timestamp) / 1000.0
            if (ts == 0.0) {
            } else {
                val vx = (nextTouches[0].absolutePoint.x - lastPoints[0].absolutePoint.x) / ts
                val vy = (nextTouches[0].absolutePoint.y - lastPoints[0].absolutePoint.y) / ts
                mVelocityPoint = CGPoint(vx, vy)
            }
        }
    }

}
