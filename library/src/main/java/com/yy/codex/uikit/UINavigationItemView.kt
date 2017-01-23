package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/18.
 */

open internal class UINavigationItemView : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    var leftViews: List<UIView> = listOf()
    var titleView: UIView? = null
    var rightViews: List<UIView> = listOf()

    override fun layoutSubviews() {
        super.layoutSubviews()
        titleView?.let {
            val iSize = it.intrinsicContentSize()
            if (iSize.width > 0 && iSize.height > 0) {
                it.frame = CGRect(
                        (frame.size.width - iSize.width) / 2.0,
                        (frame.size.height - iSize.height) / 2.0,
                        iSize.width,
                        iSize.height)
            } else {
                it.frame = CGRect(
                        (frame.size.width - it.frame.size.width) / 2.0,
                        (frame.size.height - it.frame.size.height) / 2.0,
                        it.frame.size.width,
                        it.frame.size.height)
            }
        }
        var x = 0.0
        for (contentView in leftViews) {
            x += contentView.marginInsets.left
            val iSize = contentView.intrinsicContentSize()
            if (iSize.width > 0 && iSize.height > 0) {
                contentView.frame = CGRect(
                        x,
                        0.0,
                        iSize.width,
                        frame.height)
                x += iSize.width + contentView.marginInsets.right
            } else {
                contentView.frame = CGRect(
                        x,
                        (frame.size.height - contentView.frame.size.height) / 2.0,
                        contentView.frame.size.width,
                        contentView.frame.size.height)
                x += contentView.frame.size.width + contentView.marginInsets.right
            }
        }
        var rx = frame.size.width
        for (contentView in rightViews) {
            val iSize = contentView.intrinsicContentSize()
            if (iSize.width > 0 && iSize.height > 0) {
                contentView.frame = CGRect(
                        rx - iSize.width - contentView.marginInsets.right,
                        0.0,
                        iSize.width,
                        frame.height)
                rx -= iSize.width + contentView.marginInsets.right + contentView.marginInsets.left
            } else {
                contentView.frame = CGRect(
                        rx - contentView.frame.size.width - contentView.marginInsets.right,
                        (frame.size.height - contentView.frame.size.height) / 2.0,
                        contentView.frame.size.width,
                        contentView.frame.size.height)
                rx -= contentView.frame.size.width + contentView.marginInsets.right + contentView.marginInsets.left
            }
        }
    }

    open fun animateFromFrontToBack(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            titleView?.let { it.alpha = 1f }
            for (view in leftViews) {
                view.alpha = 1f
            }
            for (view in rightViews) {
                view.alpha = 1f
            }
        } else {
            layoutSubviews()
            titleView?.let { it.frame = it.frame.setX(22.0); it.alpha = 0f; }
            for (view in leftViews) {
                view.alpha = 0f
            }
            for (view in rightViews) {
                view.alpha = 0f
            }
        }
    }

    open fun animateToFront(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            titleView?.let { it.frame = it.frame.setX(frame.width - it.frame.width); it.alpha = 0f; }
            for (view in leftViews) {
                view.alpha = 0f
            }
            for (view in rightViews) {
                view.alpha = 0f
            }
        } else {
            layoutSubviews()
            titleView?.let { it.alpha = 1f }
            for (view in leftViews) {
                view.alpha = 1f
            }
            for (view in rightViews) {
                view.alpha = 1f
            }
        }
    }

    open fun animateFromBackToFront(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            titleView?.let { it.frame = it.frame.setX(22.0); it.alpha = 0f; }
            for (view in leftViews) {
                view.alpha = 0f
            }
            for (view in rightViews) {
                view.alpha = 0f
            }
        } else {
            layoutSubviews()
            titleView?.let { it.alpha = 1f }
            for (view in leftViews) {
                view.alpha = 1f
            }
            for (view in rightViews) {
                view.alpha = 1f
            }
        }
    }

    open fun animateToGone(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            titleView?.let { it.alpha = 1f }
            for (view in leftViews) {
                view.alpha = 1f
            }
            for (view in rightViews) {
                view.alpha = 1f
            }
        } else {
            layoutSubviews()
            titleView?.let { it.frame = it.frame.setX(frame.width - it.frame.width); it.alpha = 0f; }
            for (view in leftViews) {
                view.alpha = 0f
            }
            for (view in rightViews) {
                view.alpha = 0f
            }
        }
    }

}
