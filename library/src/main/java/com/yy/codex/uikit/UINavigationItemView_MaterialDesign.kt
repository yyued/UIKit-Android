package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/19.
 */

internal class UINavigationItemView_MaterialDesign : UINavigationItemView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
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
        titleView?.let {
            if (leftViews.count() > 0) {
                x += 0.0
            } else {
                x += 12.0
            }
            val iSize = it.intrinsicContentSize()
            if (iSize.width > 0 && iSize.height > 0) {
                it.frame = CGRect(
                        x,
                        (frame.size.height - iSize.height) / 2.0,
                        iSize.width,
                        iSize.height)
            } else {
                it.frame = CGRect(
                        x,
                        (frame.size.height - it.frame.size.height) / 2.0,
                        it.frame.size.width,
                        it.frame.size.height)
            }
        }
    }

    override fun animateFromBackToFront(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            titleView?.let { it.frame = it.frame.setX(-it.frame.width); it.alpha = 0f; }
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

    override fun animateToFront(reset: Boolean) {
        if (!reset) {
            titleView?.let { it.frame = it.frame.setX(frame.width); it.alpha = 0f; }
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

    override fun animateFromFrontToBack(reset: Boolean) {
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
            titleView?.let { it.frame = it.frame.setX(-it.frame.width); it.alpha = 0f; }
            for (view in leftViews) {
                view.alpha = 0f
            }
            for (view in rightViews) {
                view.alpha = 0f
            }
        }
    }

    override fun animateToGone(reset: Boolean) {
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
            titleView?.let { it.frame = it.frame.setX(frame.width); it.alpha = 0f; }
            for (view in leftViews) {
                view.alpha = 0f
            }
            for (view in rightViews) {
                view.alpha = 0f
            }
        }
    }

}
