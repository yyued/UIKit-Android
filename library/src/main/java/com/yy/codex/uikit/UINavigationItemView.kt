package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by cuiminghui on 2017/1/18.
 */

open class UINavigationItemView : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    var mLeftViews: List<UIView> = listOf()
    var mTitleView: UIView? = null
    var mRightViews: List<UIView> = listOf()

    override fun layoutSubviews() {
        super.layoutSubviews()
        if (mTitleView != null) {
            val iSize = mTitleView!!.intrinsicContentSize()
            if (iSize.width > 0 && iSize.height > 0) {
                mTitleView!!.frame = CGRect(
                        (frame.size.width - iSize.width) / 2.0,
                        (frame.size.height - iSize.height) / 2.0,
                        iSize.width,
                        iSize.height)
            } else {
                mTitleView!!.frame = CGRect(
                        (frame.size.width - mTitleView!!.frame.size.width) / 2.0,
                        (frame.size.height - mTitleView!!.frame.size.height) / 2.0,
                        mTitleView!!.frame.size.width,
                        mTitleView!!.frame.size.height)
            }
        }
        if (mLeftViews != null && mLeftViews!!.size > 0) {
            var x = 0.0
            for (i in mLeftViews!!.indices) {
                val contentView = mLeftViews!![i]
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
        if (mRightViews != null && mRightViews!!.size > 0) {
            var rx = frame.size.width
            for (i in mRightViews!!.indices) {
                val contentView = mRightViews!![i]
                if (contentView != null) {
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
        }
    }

    internal open fun animateFromFrontToBack(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            mTitleView!!.alpha = 1f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 1f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 1f
            }
        } else {
            layoutSubviews()
            mTitleView!!.frame = CGRect(22.0, mTitleView!!.frame.y, mTitleView!!.frame.width, mTitleView!!.frame.height)
            mTitleView!!.alpha = 0f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 0f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 0f
            }
        }
    }

    internal open fun animateToFront(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            mTitleView!!.frame = CGRect(frame.width - mTitleView!!.frame.width, mTitleView!!.frame.y, mTitleView!!.frame.width, mTitleView!!.frame.height)
            mTitleView!!.alpha = 0f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 0f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 0f
            }
        } else {
            layoutSubviews()
            mTitleView!!.alpha = 1f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 1f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 1f
            }
        }
    }

    internal open fun animateFromBackToFront(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            mTitleView!!.frame = CGRect(22.0, mTitleView!!.frame.y, mTitleView!!.frame.width, mTitleView!!.frame.height)
            mTitleView!!.alpha = 0f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 0f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 0f
            }
        } else {
            layoutSubviews()
            mTitleView!!.alpha = 1f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 1f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 1f
            }
        }
    }

    internal open fun animateToGone(reset: Boolean) {
        if (!reset) {
            layoutSubviews()
            mTitleView!!.alpha = 1f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 1f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 1f
            }
        } else {
            layoutSubviews()
            mTitleView!!.frame = CGRect(frame.width - mTitleView!!.frame.width, mTitleView!!.frame.y, mTitleView!!.frame.width, mTitleView!!.frame.height)
            mTitleView!!.alpha = 0f
            for (i in mLeftViews!!.indices) {
                mLeftViews!![i].alpha = 0f
            }
            for (i in mRightViews!!.indices) {
                mRightViews!![i].alpha = 0f
            }
        }
    }

}
