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
        val actions: MutableList<NSInvocation> = mutableListOf()
        for (action in this.actions) {
            if (target != null && action.target === target && selector != null && action.selector === selector) {
                continue
            } else if (target == null && selector != null && action.selector === selector) {
                continue
            } else if (target != null && action.target === target && selector == null) {
                continue
            } else {
                actions.add(action)
            }
        }
        this.actions = actions.toList()
    }

    /* Props */

    internal fun didAddToView(view: UIView) {
        this.view = view
    }

    var view: UIView? = null

    /* Events */

    open fun touchesBegan(touches: Array<UITouch>, event: UIEvent) {
        if (!enabled) {
            state = UIGestureRecognizerState.Failed
        }
        lastPoints = touches.toList()
    }

    open fun touchesMoved(touches: Array<UITouch>, event: UIEvent) {
        if (!enabled) {
            state = UIGestureRecognizerState.Failed
        }
        lastPoints = touches.toList()
    }

    open fun touchesEnded(touches: Array<UITouch>, event: UIEvent) {
        if (!enabled) {
            state = UIGestureRecognizerState.Failed
        }
        lastPoints = touches.toList()
    }

    fun touchesCancelled(touches: Array<UITouch>, event: UIEvent) {
        lastPoints = touches.toList()
        state = UIGestureRecognizerState.Cancelled
    }

    protected fun sendActions() {
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

    protected var mGestureRecognizersRequiresFailed: List<UIGestureRecognizer> = listOf()
    protected var mGestureRecognizerRequiresFailedTimer: Timer? = null

    fun requireGestureRecognizerToFail(otherGestureRecognizer: UIGestureRecognizer) {
        val mutableList = mGestureRecognizersRequiresFailed.toMutableList()
        mutableList.add(otherGestureRecognizer)
        mGestureRecognizersRequiresFailed = mutableList.toList()
    }

    protected fun waitOtherGesture(runnable: Runnable) {
        val handler = Handler()
        if (mGestureRecognizersRequiresFailed.size > 0) {
            if (mGestureRecognizerRequiresFailedTimer == null) {
                mGestureRecognizerRequiresFailedTimer = Timer()
                mGestureRecognizerRequiresFailedTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        mGestureRecognizerRequiresFailedTimer = null
                        if (state == UIGestureRecognizerState.Failed) {
                            return
                        }
                        var allFailed = true
                        for (recognizer in mGestureRecognizersRequiresFailed) {
                            if (recognizer.state != UIGestureRecognizerState.Failed) {
                                allFailed = false
                            }
                        }
                        if (allFailed) {
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
