package com.yy.codex.uikit

import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by cuiminghui on 2017/1/13.
 */

internal class UIGestureRecognizerLooper internal constructor(internal var hitTestedView: UIView) {

    internal var gestureRecognizers: List<UIGestureRecognizer>
    internal var isFinished = false

    init {
        gestureRecognizers = getGestureRecognizers(hitTestedView)
        for (i in gestureRecognizers.indices) {
            gestureRecognizers[i].looper = this
        }
        gestureRecognizers = gestureRecognizers.sortedWith(Comparator { a, b -> if (a.gesturePriority() > b.gesturePriority()) 1 else -1 })
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

    internal fun onTouchesCancelled(touches: List<UITouch>, event: UIEvent) {
        val copyList = ArrayList(gestureRecognizers)
        copyList.indices
                .filter { checkState(copyList[it]) }
                .forEach { copyList[it].touchesCancelled() }
    }

    internal fun checkState(gestureRecognizer: UIGestureRecognizer): Boolean {
        val mutableList = gestureRecognizers.toMutableList()
        if (gestureRecognizer.state == UIGestureRecognizerState.Failed || gestureRecognizer.state == UIGestureRecognizerState.Cancelled) {
            mutableList.remove(gestureRecognizer)
            gestureRecognizers = mutableList.toList()
            return false
        }
        else if (gestureRecognizer.state == UIGestureRecognizerState.Ended) {
            isFinished = true
        }
        return true
    }

    internal fun markFailed() {
        val recognizedGestures = gestureRecognizers.filter { (it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
        if (recognizedGestures.size > 0) {
            val stealable = recognizedGestures.any { it.stealable && (it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
            val stealed = recognizedGestures.any { it.stealer && (it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
            if (stealable) {
                if (!stealed) {
                    gestureRecognizers
                            .filter { !it.stealer && !it.stealable && !(it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
                            .forEach { it.state = UIGestureRecognizerState.Failed }
                }
                else {
                    gestureRecognizers
                            .filter { !it.stealer && !it.stealable && !(it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
                            .forEach { it.state = UIGestureRecognizerState.Failed }
                    gestureRecognizers.forEach {
                        if (it.stealable && (it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended)) {
                            it.touchesCancelled()
                        }
                    }
                }
            }
            else {
                gestureRecognizers
                        .filter { !(it.state == UIGestureRecognizerState.Began || it.state == UIGestureRecognizerState.Changed || it.state == UIGestureRecognizerState.Ended) }
                        .forEach { it.state = UIGestureRecognizerState.Failed }
            }
        }
    }

    private fun getGestureRecognizers(view: UIView): List<UIGestureRecognizer> {
        if (!view.userInteractionEnabled) {
            return listOf()
        } else {
            var gestureRecognizers: MutableList<UIGestureRecognizer> = view.gestureRecognizers.toMutableList()
            val superview = view.superview
            if (superview != null) {
                val superGestureRecognizers = getGestureRecognizers(superview)
                gestureRecognizers.addAll(superGestureRecognizers)
            }
            gestureRecognizers = removeConflictRecognizers(gestureRecognizers.toList()).toMutableList()
            return gestureRecognizers.toList()
        }
    }

    private fun removeConflictRecognizers(gestureRecognizers: List<UIGestureRecognizer>): List<UIGestureRecognizer> {
        val longPressHash: HashMap<Int, Boolean> = hashMapOf()
        return gestureRecognizers.filter {
            (it as? UILongPressGestureRecognizer)?.let {
                val aKey = (it.minimumPressDuration * 1000).toInt()
                if (longPressHash.containsKey(aKey)) {
                    return@filter false
                }
                else {
                    longPressHash.put(aKey, true)
                    return@filter true
                }
            }
            return@filter true
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
