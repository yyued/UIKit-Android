package com.yy.codex.uikit

import android.view.MotionEvent

/**
 * Created by cuiminghui on 2017/1/16.
 */

internal object UIViewHelpers {

    fun hitTest(view: UIView, point: CGPoint, event: MotionEvent): UIView? {
        val views = view.subviews
        if (!view.isUserInteractionEnabled && view.alpha <= 0) {
            return null
        }
        if (pointInside(view, point)) {
            for (subview in views) {
                if (!subview.isUserInteractionEnabled || subview.alpha <= 0) {
                    continue
                }
                val convertedPoint = view.convertPoint(point, subview)
                val hitTestView = subview.hitTest(convertedPoint, event)
                if (hitTestView != null) {
                    return hitTestView
                }
            }
            return view
        }
        return null
    }

    fun pointInside(view: UIView, point: CGPoint): Boolean {
        val h = view.frame.size.height
        val w = view.frame.size.width
        val touchX = point.x
        val touchY = point.y
        if (touchY <= h && touchX <= w && touchY >= 0 && touchX >= 0) {
            return true
        }
        return false
    }

    fun convertPoint(view: UIView, point: CGPoint, toView: UIView): CGPoint {
        if (view === toView) {
            return point
        }
        var viewRoot = view
        var viewX = viewRoot.frame.origin.x
        var viewY = viewRoot.frame.origin.y
        while (viewRoot.superview != null) {
            viewRoot.superview?.let {
                viewRoot = it
            }
            viewX += viewRoot.frame.origin.x
            viewY += viewRoot.frame.origin.y
        }
        var toViewRoot = toView
        var toViewX = toViewRoot.frame.origin.x
        var toViewY = toViewRoot.frame.origin.y
        while (toViewRoot.superview != null) {
            toViewRoot.superview?.let {
                toViewRoot = it
            }
            toViewX += toViewRoot.frame.origin.x
            toViewY += toViewRoot.frame.origin.y
        }
        if (viewRoot !== toViewRoot) {
            return CGPoint(0.0, 0.0)
        }
        return CGPoint(viewX - toViewX + point.x, viewY - toViewY + point.y)
    }

}
