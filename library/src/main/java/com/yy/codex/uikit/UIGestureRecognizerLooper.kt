package com.yy.codex.uikit

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

/**
 * Created by cuiminghui on 2017/1/13.
 */

internal class UIGestureRecognizerLooper internal constructor(internal var hitTestedView: UIView) {

    internal var gestureRecognizers: ArrayList<UIGestureRecognizer>
    internal var isFinished = false

    init {
        gestureRecognizers = getGestureRecognizers(hitTestedView)
        for (i in gestureRecognizers.indices) {
            gestureRecognizers[i].looper = this
        }
        Collections.sort(gestureRecognizers) { gestureRecognizer, t1 -> if (gestureRecognizer.gesturePriority() > t1.gesturePriority()) 1 else -1 }
        resetState()
    }

    internal fun onTouchesBegan(touches: List<UITouch>, event: UIEvent) {
        val copyList = ArrayList(gestureRecognizers)
        for (i in copyList.indices) {
            if (checkState(copyList[i])) {
                copyList[i].touchesBegan(touches, event)
                checkState(copyList[i])
            }
        }
        markFailed()
    }

    internal fun onTouchesMoved(touches: List<UITouch>, event: UIEvent) {
        val copyList = ArrayList(gestureRecognizers)
        for (i in copyList.indices) {
            if (checkState(copyList[i])) {
                copyList[i].touchesMoved(touches, event)
                checkState(copyList[i])
            }
        }
        markFailed()
    }

    internal fun onTouchesEnded(touches: List<UITouch>, event: UIEvent) {
        val copyList = ArrayList(gestureRecognizers)
        for (i in copyList.indices) {
            if (checkState(copyList[i])) {
                copyList[i].touchesEnded(touches, event)
                checkState(copyList[i])
            }
        }
        markFailed()
    }

    internal fun checkState(gestureRecognizer: UIGestureRecognizer): Boolean {
        if (gestureRecognizer.state == UIGestureRecognizerState.Failed || gestureRecognizer.state == UIGestureRecognizerState.Cancelled) {
            gestureRecognizers.remove(gestureRecognizer)
            return false
        } else if (gestureRecognizer.state == UIGestureRecognizerState.Ended) {
            isFinished = true
        }
        return true
    }

    internal fun markFailed() {
        var hasRecognized = false
        for (i in gestureRecognizers.indices) {
            hasRecognized = gestureRecognizers[i].state == UIGestureRecognizerState.Began || gestureRecognizers[i].state == UIGestureRecognizerState.Changed || gestureRecognizers[i].state == UIGestureRecognizerState.Ended
            if (hasRecognized) {
                break
            }
        }
        if (hasRecognized) {
            gestureRecognizers
                .filter { !(it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
                .forEach { it.state = UIGestureRecognizerState.Failed }
        }
    }

    private fun getGestureRecognizers(view: UIView): ArrayList<UIGestureRecognizer> {
        if (!view.isUserInteractionEnabled) {
            return ArrayList()
        } else {
            val gestureRecognizers = ArrayList(view.gestureRecognizers)
            val superview = view.superview
            if (superview != null) {
                val superGestureRecognizers = getGestureRecognizers(superview)
                gestureRecognizers.addAll(superGestureRecognizers)
            }
            return gestureRecognizers
        }
    }

    private fun resetState() {
        gestureRecognizers.forEach { it.state = UIGestureRecognizerState.Possible }
    }

    companion object {

        internal fun isHitTestedView(touches: List<UITouch>, theView: UIView): Boolean {
            if (touches.size > 0) {
                return touches[0].hitTestedView === theView
            }
            return false

        }

    }

}
