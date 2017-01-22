package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/19.
 */

class UINavigationItemView_MaterialDesign : UINavigationItemView {

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
        if (mLeftViews != null && mLeftViews.count() > 0) {
            for (i in 0..mLeftViews.count() - 1) {
                val contentView = mLeftViews[i]
                if (contentView != null) {
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
            }
        }
        if (mTitleView != null) {
            if (mLeftViews != null && mLeftViews.count() > 0) {
                x += 0.0
            } else {
                x += 12.0
            }
            val iSize = mTitleView!!.intrinsicContentSize()
            if (iSize.width > 0 && iSize.height > 0) {
                mTitleView!!.frame = CGRect(
                        x,
                        (frame.size.height - iSize.height) / 2.0,
                        iSize.width,
                        iSize.height)
            } else {
                mTitleView!!.frame = CGRect(
                        x,
                        (frame.size.height - mTitleView!!.frame.size.height) / 2.0,
                        mTitleView!!.frame.size.width,
                        mTitleView!!.frame.size.height)
            }
        }
    }

    override fun animateFromBackToFront(reset: Boolean) {
        if (!reset) {
            alpha = 0f
        } else {
            alpha = 1f
        }
    }

    override fun animateToFront(reset: Boolean) {
        if (!reset) {
            alpha = 0f
        } else {
            alpha = 1f
        }
    }

    override fun animateFromFrontToBack(reset: Boolean) {
        if (!reset) {
            alpha = 1f
        } else {
            alpha = 0f
        }
    }

    override fun animateToGone(reset: Boolean) {
        if (!reset) {
            alpha = 1f
        } else {
            alpha = 0f
        }
    }

}
