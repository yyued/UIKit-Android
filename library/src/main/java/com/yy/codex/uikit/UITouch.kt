package com.yy.codex.uikit

import java.util.ArrayList

/**
 * Created by cuiminghui on 2017/1/11.
 */

class UITouch(val hitTestedView: UIView, val relativePoint: CGPoint, val absolutePoint: CGPoint, private val mPhase: UITouch.Phase, eventID: Long) {

    enum class Phase {
        Began, // whenever a finger touches the surface.
        Moved, // whenever a finger moves on the surface.
        Stationary, // whenever a finger is touching the surface but hasn't moved since the previous event.
        Ended, // whenever a finger leaves the surface.
        Cancelled
        // whenever a touch doesn't end but we need to stop tracking (e.g. putting device to face)
    }

    var timestamp: Long = 0
    var tapCount = 1
        private set
    private var eventID: Long = 0

    init {
        timestamp = System.currentTimeMillis()
        this.eventID = eventID
        resetTapCount()
    }

    fun resetTapCount() {
        val newTapCountStore = ArrayList<UITouch>()
        var found = false
        for (i in tapCountStore.indices) {
            if (tapCountStore[i].absolutePoint.inRange(22.0, 22.0, this.absolutePoint)) {
                if (tapCountStore[i].eventID != this.eventID) {
                    if (tapCountStore[i].timestamp <= System.currentTimeMillis() - 300) {
                        tapCount = 1
                    } else {
                        tapCount = tapCountStore[i].tapCount + 1
                    }
                } else {
                    tapCount = tapCountStore[i].tapCount
                }
                newTapCountStore.add(this)
                found = true
                break
            }
        }
        if (!found) {
            tapCount = 1
            newTapCountStore.add(this)
        }
        tapCountStore = newTapCountStore
    }

    fun locationInView(view: UIView): CGPoint {
        val hitTestedView = hitTestedView
        if (hitTestedView != null) {
            if (hitTestedView === view) {
                return relativePoint
            } else {
                return hitTestedView.convertPoint(relativePoint, view)
            }
        }
        return CGPoint(0.0, 0.0)
    }

    override fun toString(): String {
        return "UITouch { RelativeView=" + this.hitTestedView + " x=" + this.relativePoint.x + " y=" + this.relativePoint.y + " Phase=" + this.mPhase
    }

    companion object {
        internal var tapCountStore = ArrayList<UITouch>()
    }
}
