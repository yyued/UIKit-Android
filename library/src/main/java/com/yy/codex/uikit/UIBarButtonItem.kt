package com.yy.codex.uikit

import android.content.Context

import com.yy.codex.foundation.NSInvocation

/**
 * Created by cuiminghui on 2017/1/18.
 */

class UIBarButtonItem(val target: Any?, val action: String?) : UIBarItem() {

    var isSystemBackItem = false
        internal set
    var customView: UIView? = null
    var width = 0.0
    var insets: UIEdgeInsets = UIEdgeInsets(0.0, 8.0, 0.0, 8.0)

    constructor(title: String?, target: Any?, action: String?) : this(target, action) {
        this.title = title
    }

    constructor(image: UIImage?, target: Any?, action: String?) : this(target, action) {
        this.image = image
    }

    override fun getContentView(context: Context): UIView? {
        if (customView != null) {
            if (width > 0.0) {
                customView?.frame = CGRect(0.0, 0.0, width, Math.min(44.0, (customView?.frame?.height ?: 0.0)))
            }
            customView?.marginInsets = insets
            return customView
        }
        else if (mView == null) {
            val button = UIButton(context)
            title?.let {
                button.setFont(UIFont(17f))
                button.setTitle(it, UIControl.State.Normal)
            }
            image?.let {
                button.setImage(it, UIControl.State.Normal)
            }
            button.frame = CGRect(0.0, 0.0, button.intrinsicContentSize().width, 44.0)
            button.setImageEdgeInsets(imageInsets)
            if (target != null && action != null) {
                button.addTarget(target!!, action!!, UIControl.Event.TouchUpInside)
            }
            mView = button
            mView?.marginInsets = insets
        }
        return super.getContentView(context)
    }

}
