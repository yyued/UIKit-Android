package com.yy.codex.uikit

import android.os.Handler

import com.yy.codex.foundation.NSInvocation

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * Created by it on 17/1/4.
 */

open class UIGestureRecognizer {

    internal var looper: UIGestureRecognizerLooper? = null
    var enabled = true
    var stealer = false
    var stealable = false

    private var actions: List<NSInvocation> = listOf()
    private var triggerBlock: Runnable? = null
    var state = UIGestureRecognizerState.Possible

    constructor(target: Any, selector: String) {
        actions = listOf(NSInvocation(target, selector))
    }

    constructor(triggerBlock: Runnable) {
        this.triggerBlock = triggerBlock
    }

    fun addTarget(target: Any, selector: String) {
        val actions = actions.toMutableList()
        actions.add(NSInvocation(target, selector))
        this.actions = actions.toList()
    }

    fun removeTarget(target: Any?, selector: String?) {
        if (target == null && selector == null) {
            actions = listOf()
            return
        }
        actions = actions.filter {
            !(target != null && it.target === target && selector != null && it.selector === selector) &&
            !(target == null && selector != null && it.selector === selector) &&
            !(target != null && it.target === target && selector == null)
        }
    }

    /* Props */

    internal fun didAddToView(view: UIView) {
        this.view = view
    }

    var view: UIView? = null

    /* Events */

    open fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        if (!enabled) {
            state = UIGestureRecognizerState.Failed
        }
        lastPoints = touches.toList()
    }

    open fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        if (!enabled) {
            state = UIGestureRecognizerState.Failed
        }
        lastPoints = touches.toList()
    }

    open fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        if (!enabled) {
            state = UIGestureRecognizerState.Failed
        }
        lastPoints = touches.toList()
    }

    open fun touchesCancelled() {
        state = UIGestureRecognizerState.Cancelled
        sendActions()
    }

    protected open fun sendActions() {
        for (action in actions) {
            action.invoke(arrayOf<Any>(this))
        }
        triggerBlock?.let { it.run() }
    }

    /* Points */

    protected var lastPoints: List<UITouch> = listOf()

    fun location(): CGPoint {
        val view = view
        if (view != null) {
            return location(view, 0)
        }
        return CGPoint(0.0, 0.0)
    }

    @JvmOverloads fun location(inView: UIView, touchIndex: Int = 0): CGPoint {
        if (touchIndex < lastPoints.size) {
            return lastPoints[touchIndex].locationInView(inView)
        }
        return CGPoint(0.0, 0.0)
    }

    fun numberOfTouches(): Int {
        return lastPoints.size
    }

    /* Delegates */

    internal open fun gesturePriority(): Int {
        return 0
    }

    protected var gestureRecognizersRequiresFailed: List<UIGestureRecognizer> = listOf()
    protected var gestureRecognizerRequiresFailedTimer: Timer? = null

    fun requireGestureRecognizerToFail(otherGestureRecognizer: UIGestureRecognizer) {
        val mutableList = gestureRecognizersRequiresFailed.toMutableList()
        mutableList.add(otherGestureRecognizer)
        gestureRecognizersRequiresFailed = mutableList.toList()
    }

    protected fun waitOtherGesture(runnable: Runnable) {
        val handler = Handler()
        if (gestureRecognizersRequiresFailed.size > 0) {
            if (gestureRecognizerRequiresFailedTimer == null) {
                gestureRecognizerRequiresFailedTimer = Timer()
                gestureRecognizerRequiresFailedTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        gestureRecognizerRequiresFailedTimer = null
                        if (state == UIGestureRecognizerState.Failed) {
                            return
                        }
                        if (gestureRecognizersRequiresFailed.none { it.state != UIGestureRecognizerState.Failed }) {
                            handler.post(runnable)
                        } else {
                            handler.post { waitOtherGesture(runnable) }
                        }
                    }
                }, 350)
            }
        } else {
            runnable.run()
        }
    }

}
