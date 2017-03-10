package com.yy.codex.uikit

import android.content.res.TypedArray
import net.objecthunter.exp4j.ExpressionBuilder

/**
 * Created by cuiminghui on 2017/1/4.
 */

class UIConstraint {

    enum class LayoutRelate {
        RelateToGroup,
        RelateToPrevious
    }

    var disabled = false
    var alignmentRelate = LayoutRelate.RelateToGroup
    var centerHorizontally = false
    var centerVertically = false
    var sizeRelate = LayoutRelate.RelateToGroup
    var width: String? = null
    var height: String? = null
    var pinRelate = LayoutRelate.RelateToGroup
    var top: String? = null
    var left: String? = null
    var right: String? = null
    var bottom: String? = null

    private var needsLayout = true
    private var lastSuperviewFrame = CGRect(0.0, 0.0, 0.0, 0.0)
    private var lastPreviousViewFrame = CGRect(0.0, 0.0, 0.0, 0.0)

    fun setNeedsLayout() {
        needsLayout = true
    }

    fun requestFrame(myView: UIView, superView: UIView?, previousView: UIView?): CGRect {
        if (disabled) {
            return myView.frame
        }
        if (superView != null && lastSuperviewFrame == superView.frame &&
                previousView != null && lastPreviousViewFrame == previousView.frame &&
                !needsLayout) {
            return myView.frame
        }
        needsLayout = false
        lastSuperviewFrame = superView?.frame ?: CGRect(0.0, 0.0, 0.0, 0.0)
        lastPreviousViewFrame = previousView?.frame ?: CGRect(0.0, 0.0, 0.0, 0.0)
        var x = java.lang.Double.NaN
        var y = java.lang.Double.NaN
        var mx = java.lang.Double.NaN
        var my = java.lang.Double.NaN
        var cx = java.lang.Double.NaN
        var cy = java.lang.Double.NaN
        var w = java.lang.Double.NaN
        var h = java.lang.Double.NaN
        var xorw = false
        var yorh = false
        if (width is String) {
            val formula = width as String
            if (sizeRelate == LayoutRelate.RelateToPrevious) {
                w = requestValue(0.0, previousView?.frame?.size?.width ?: 0.0, formula)
            } else {
                w = requestValue(0.0, superView?.frame?.size?.width ?: 0.0, formula)
            }
        } else {
            val iSize = myView.intrinsicContentSize()
            if (iSize.width > 0.0) {
                w = Math.ceil(iSize.width)
            }
        }
        if (height is String) {
            val formula = height as String
            if (sizeRelate == LayoutRelate.RelateToPrevious) {
                h = requestValue(0.0, previousView?.frame?.size?.height ?: 0.0, formula)
            } else {
                h = requestValue(0.0, superView?.frame?.size?.height ?: 0.0, formula)
            }
        } else {
            if (width != null && !java.lang.Double.isNaN(w)) {
                myView.maxWidth = w
            }
            val iSize = myView.intrinsicContentSize()
            if (iSize.height > 0.0) {
                h = Math.ceil(iSize.height)
            }
        }
        if (centerHorizontally && alignmentRelate == LayoutRelate.RelateToGroup) {
            cx = if (superView != null) superView.frame.size.width / 2.0 else 0.0
        } else if (centerHorizontally && alignmentRelate == LayoutRelate.RelateToPrevious) {
            cx = previousView?.center?.x ?: 0.0
        }
        if (centerVertically && alignmentRelate == LayoutRelate.RelateToGroup) {
            cy = if (superView != null) superView.frame.size.height / 2.0 else 0.0
        } else if (centerVertically && alignmentRelate == LayoutRelate.RelateToPrevious) {
            cy = previousView?.center?.y ?: 0.0
        }
        if (top is String) {
            val formula = top as String
            var t = 0.0
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0.0, superView?.frame?.size?.height ?: 0.0, formula)
            } else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0.0, previousView?.frame?.size?.height ?: 0.0, formula)
                if (previousView != null) {
                    t = previousView.frame.origin.y + t
                }
            }
            if (centerVertically) {
                cy += t
            } else {
                y = t
            }
        }
        if (bottom is String) {
            val formula = bottom as String
            var t = 0.0
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0.0, superView?.frame?.size?.height ?: 0.0, formula)
            } else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0.0, previousView?.frame?.size?.height ?: 0.0, formula)
                if (previousView != null) {
                    t = previousView.frame.origin.y + previousView.frame.size.height - t
                }
            }
            if (centerVertically) {
                cy -= t
            } else if (pinRelate == LayoutRelate.RelateToPrevious) {
                y = t
                yorh = true
            } else {
                my = t
            }
        }
        if (left is String) {
            val formula = left as String
            var t = 0.0
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0.0, superView?.frame?.size?.width ?: 0.0, formula)
            } else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0.0, previousView?.frame?.size?.width ?: 0.0, formula)
                if (previousView != null) {
                    t = previousView.frame.origin.x + t
                }
            }
            if (centerHorizontally) {
                cx += t
            } else {
                x = t
            }
        }
        if (right is String) {
            val formula = right as String
            var t = 0.0
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0.0, superView?.frame?.size?.width ?: 0.0, formula)
            } else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0.0, previousView?.frame?.size?.width ?: 0.0, formula)
                if (previousView != null) {
                    t = previousView.frame.origin.x + previousView.frame.size.width - t
                }
            }
            if (centerHorizontally) {
                cx -= t
            } else if (pinRelate == LayoutRelate.RelateToPrevious) {
                x = t
                xorw = true
            } else {
                mx = t
            }
        }
        var newFrame = CGRect(
                if (!java.lang.Double.isNaN(cx)) cx else if (!java.lang.Double.isNaN(x)) x else 0.0,
                if (!java.lang.Double.isNaN(cy)) cy else if (!java.lang.Double.isNaN(y)) y else 0.0,
                if (!java.lang.Double.isNaN(w)) w else 0.0,
                if (!java.lang.Double.isNaN(h)) h else 0.0
        )
        if (!java.lang.Double.isNaN(cx)) {
            newFrame = newFrame.setX(newFrame.origin.x - newFrame.size.width / 2.0)
        }
        if (!java.lang.Double.isNaN(cy)) {
            newFrame = newFrame.setY(newFrame.origin.y - newFrame.size.height / 2.0)
        }
        if (!java.lang.Double.isNaN(mx) && java.lang.Double.isNaN(x)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setX((superView?.frame?.size?.width ?: 0.0) - mx - newFrame.size.width)
            } else {
                newFrame = newFrame.setX((previousView?.frame?.size?.width ?: 0.0) - mx - newFrame.size.width)
            }
        }
        if (!java.lang.Double.isNaN(my) && java.lang.Double.isNaN(y)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setY((superView?.frame?.size?.height ?: 0.0) - my - newFrame.size.height)
            } else {
                newFrame = newFrame.setY((previousView?.frame?.size?.height ?: 0.0) - my - newFrame.size.height)
            }
        }
        if (!java.lang.Double.isNaN(mx) && !java.lang.Double.isNaN(x)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setWidth((superView?.frame?.size?.width ?: 0.0) - mx - x)
            } else {
                newFrame = newFrame.setWidth((previousView?.frame?.size?.width ?: 0.0) - mx - (x - (previousView?.frame?.origin?.x ?: 0.0)))
            }
        }
        if (!java.lang.Double.isNaN(my) && !java.lang.Double.isNaN(y)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setHeight((superView?.frame?.size?.height ?: 0.0) - my - y)
            } else {
                newFrame = newFrame.setHeight((previousView?.frame?.size?.height ?: 0.0) - my - (y - (previousView?.frame?.origin?.y ?: 0.0)))
            }
        }
        if (xorw) {
            newFrame = newFrame.setX(newFrame.origin.x - newFrame.size.width)
        }
        if (yorh) {
            newFrame = newFrame.setY(newFrame.origin.y - newFrame.size.height)
        }
        if (java.lang.Double.isNaN(newFrame.origin.x)) {
            newFrame = newFrame.setX(0.0)
        }
        if (java.lang.Double.isNaN(newFrame.origin.y)) {
            newFrame = newFrame.setY(0.0)
        }
        if (java.lang.Double.isNaN(newFrame.size.width)) {
            newFrame = newFrame.setWidth(0.0)
        }
        if (java.lang.Double.isNaN(newFrame.size.height)) {
            newFrame = newFrame.setHeight(0.0)
        }
        return newFrame
    }

    fun requestValue(origin: Double, length: Double, formula: String): Double {
        var formula = formula
        formula = formula.replace("%", "/100.0*" + length)
        try {
            val e = ExpressionBuilder(formula).build()
            return e.evaluate() + origin
        } catch (e: Exception) {
            return 0.0
        }

    }

    fun sizeCanFit(): Boolean {
        return !centerHorizontally && !centerHorizontally && width == null && height == null &&
                top != null && left != null && bottom != null && right != null
    }

    companion object {

        fun create(attributes: TypedArray): UIConstraint? {
            if (attributes.getBoolean(R.styleable.UIView_constraint_enabled, false)) {
                val constraint = UIConstraint()
                when (attributes.getInt(R.styleable.UIView_constraint_alignmentRelate, 0)) {
                    0 -> constraint.alignmentRelate = LayoutRelate.RelateToGroup
                    1 -> constraint.alignmentRelate = LayoutRelate.RelateToPrevious
                }
                constraint.centerHorizontally = attributes.getBoolean(R.styleable.UIView_constraint_centerHorizontally, false)
                constraint.centerVertically = attributes.getBoolean(R.styleable.UIView_constraint_centerVertically, false)
                when (attributes.getInt(R.styleable.UIView_constraint_sizeRelate, 0)) {
                    0 -> constraint.sizeRelate = LayoutRelate.RelateToGroup
                    1 -> constraint.sizeRelate = LayoutRelate.RelateToPrevious
                }
                constraint.width = attributes.getString(R.styleable.UIView_constraint_width)
                constraint.height = attributes.getString(R.styleable.UIView_constraint_height)
                when (attributes.getInt(R.styleable.UIView_constraint_pinRelate, 0)) {
                    0 -> constraint.pinRelate = LayoutRelate.RelateToGroup
                    1 -> constraint.pinRelate = LayoutRelate.RelateToPrevious
                }
                constraint.top = attributes.getString(R.styleable.UIView_constraint_top)
                constraint.left = attributes.getString(R.styleable.UIView_constraint_left)
                constraint.bottom = attributes.getString(R.styleable.UIView_constraint_bottom)
                constraint.right = attributes.getString(R.styleable.UIView_constraint_right)
                return constraint
            }
            return null
        }

        fun center(): UIConstraint {
            val constraint = UIConstraint()
            constraint.centerHorizontally = true
            constraint.centerVertically = true
            return constraint
        }

        fun full(): UIConstraint {
            val constraint = UIConstraint()
            constraint.centerHorizontally = true
            constraint.centerVertically = true
            constraint.width = "100%"
            constraint.height = "100%"
            return constraint
        }

        fun horizonStack(idx: Int, total: Int): UIConstraint {
            val step = (100 / total)
            val current = step * idx
            val constraint = UIConstraint()
            constraint.left = "$current%"
            constraint.width = "$step%"
            constraint.top = "0"
            constraint.height = "100%"
            return constraint
        }

        fun verticalStack(idx: Int, total: Int): UIConstraint {
            val step = (100 / total)
            val current = step * idx
            val constraint = UIConstraint()
            constraint.top = "$current%"
            constraint.height = "$step%"
            constraint.left = "0"
            constraint.width = "100%"
            return constraint
        }

    }

}
