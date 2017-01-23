package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

import com.yy.codex.foundation.NSInvocation

import java.util.ArrayList
import java.util.EnumSet
import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/13.
 */

open class UIControl : UIView {

    enum class Event {
        TouchDown,
        TouchDownRepeat,
        TouchDragInside,
        TouchDragOutside,
        TouchDragEnter,
        TouchDragExit,
        TouchUpInside,
        TouchUpOutside,
        TouchCancel,
        ValueChanged
    }

    enum class State {
        Normal,
        Highlighted,
        Disabled,
        Selected
    }

    enum class ContentVerticalAlignment {
        Center,
        Top,
        Bottom,
        Fill
    }

    enum class ContentHorizontalAlignment {
        Center,
        Left,
        Right,
        Fill
    }

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    private val invocations = HashMap<Event, List<NSInvocation>>()
    private val runnable = HashMap<Event, List<Runnable>>()

    fun addTarget(target: Any, selector: String, event: Event) {
        if (!invocations.containsKey(event)) {
            invocations.put(event, listOf())
        }
        invocations[event]?.let {
            val mutableList = it.toMutableList()
            mutableList.add(NSInvocation(target, selector))
            invocations.put(event, mutableList.toList())
        }
    }

    fun removeTarget(target: Any?, selector: String?, event: Event?) {
        if (target == null && selector == null && event == null) {
            invocations.clear()
        } else if (target == null && selector == null && event != null) {
            invocations.remove(event)
        } else if (target != null && selector == null && event == null) {
            for ((key, invocations) in invocations) {
                this.invocations.put(key, invocations.filter { it.target !== target })
            }
        } else if (target != null && selector != null && event == null) {
            for ((key, invocations) in invocations) {
                this.invocations.put(key, invocations.filter { it.target !== target || it.selector !== selector })
            }
        } else if (target == null && selector != null && event == null) {
            for ((key, invocations) in invocations) {
                this.invocations.put(key, invocations.filter { it.selector !== selector })
            }
        } else if (target == null && selector != null && event != null) {
            if (!invocations.containsKey(event)) {
                return
            }
            val invocations = invocations[event]
            invocations?.let {
                this.invocations.put(event, invocations.filter { it.target !== target })
            }
        } else if (target != null && selector != null && event != null) {
            if (!invocations.containsKey(event)) {
                return
            }
            val invocations = invocations[event]
            invocations?.let {
                this.invocations.put(event, invocations.filter { it.target !== target || it.selector !== selector })
            }
        }
    }

    fun addBlock(runnable: Runnable, event: Event) {
        if (!this.runnable.containsKey(event)) {
            this.runnable.put(event, listOf())
        }
        val allRunnable: MutableList<Runnable> = this.runnable[event]!!.toMutableList()
        allRunnable.add(runnable)
        this.runnable.put(event, allRunnable.toList())
    }

    override fun init() {
        super.init()
        isUserInteractionEnabled = true
        val longPressGestureRecognizer = UILongPressGestureRecognizer(this, "onLongPressed:")
        longPressGestureRecognizer.mMinimumPressDuration = 0.05
        addGestureRecognizer(longPressGestureRecognizer)
        val tapGestureRecognizer = UITapGestureRecognizer(this, "onTapped:")
        addGestureRecognizer(tapGestureRecognizer)
    }

    var isTouchInside = true
        private set

    protected open fun onLongPressed(sender: UILongPressGestureRecognizer) {
        if (sender.state == UIGestureRecognizerState.Began) {
            isTouchInside = true
            onEvent(Event.TouchDown)
            isTracking = true
            isHighlighted = true
            resetState()
        } else if (sender.state == UIGestureRecognizerState.Changed) {
            if (isPointInside(sender.location(this))) {
                onEvent(Event.TouchDragInside)
                if (!isTouchInside) {
                    onEvent(Event.TouchDragEnter)
                    isTouchInside = true
                    isHighlighted = true
                    resetState()
                }
            } else {
                onEvent(Event.TouchDragOutside)
                if (isTouchInside) {
                    onEvent(Event.TouchDragExit)
                    isTouchInside = false
                    isHighlighted = false
                    resetState()
                }
            }
        } else if (sender.state == UIGestureRecognizerState.Ended) {
            if (isPointInside(sender.location(this))) {
                onEvent(Event.TouchUpInside)
            } else {
                onEvent(Event.TouchUpOutside)
            }
            isTracking = false
            isHighlighted = false
            resetState()
        }
    }

    protected fun onTapped(sender: UITapGestureRecognizer) {
        onEvent(Event.TouchUpInside)
    }

    protected open fun onEvent(event: Event) {
        val invocations = invocations[event]
        if (invocations != null) {
            for (i in invocations.indices) {
                invocations[i].invoke(arrayOf<Any>(this))
            }
        }
        val runnables = runnable[event]
        if (runnables != null) {
            for (i in runnables.indices) {
                runnables[i].run()
            }
        }
    }

    protected fun isPointInside(point: CGPoint): Boolean {
        val xRange = this.frame.size.width / 2.0
        val yRange = this.frame.size.height / 2.0
        return point.inRange(xRange + 22, yRange + 22, CGPoint(xRange, yRange))
    }

    /* Enabled */

    private var mEnabled = true

    override fun setEnabled(enabled: Boolean) {
        this.mEnabled = enabled
        isUserInteractionEnabled = enabled
        resetState()
    }

    override fun isEnabled(): Boolean {
        return mEnabled
    }

    /* Selected */

    private var mSelected = false

    override fun setSelected(selected: Boolean) {
        this.mSelected = selected
        resetState()
    }

    override fun isSelected(): Boolean {
        return mSelected
    }

    /* Highlighted */

    var isHighlighted = false
        private set

    /* State */

    var state = EnumSet.of(State.Normal)
        private set

    protected open fun resetState() {
        val state = EnumSet.of(State.Normal)
        if (isTracking && isHighlighted) {
            state.add(State.Highlighted)
        }
        if (isSelected) {
            state.remove(State.Normal)
            state.add(State.Selected)
        }
        if (!isEnabled) {
            state.add(State.Disabled)
        }
        this.state = state
    }

    /* Tracking */

    var isTracking = false
        private set

    /* ContentAlignment */

    var contentVerticalAlignment = ContentVerticalAlignment.Center

    var contentHorizontalAlignment = ContentHorizontalAlignment.Center

}
