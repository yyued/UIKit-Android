package com.yy.codex.uikit

import android.content.Context

import com.yy.codex.foundation.NSInvocation
import com.yy.codex.uikit.UIControl
import com.yy.codex.uikit.UIEdgeInsets
import com.yy.codex.uikit.UIImage
import com.yy.codex.uikit.UIView

import java.util.EnumSet
import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/18.
 */

open class UIBarItem {

    var enabled = true
    var title: String? = null
    var image: UIImage? = null
    var imageInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
    var tag = 0
    var titleTextAttributes = HashMap<EnumSet<UIControl.State>, HashMap<String, Any>>()
        private set
    fun titleTextAttributes(state: EnumSet<UIControl.State>): HashMap<String, Any>? {
        return titleTextAttributes[state]
    }
    protected var view: UIView? = null
    open fun getContentView(context: Context): UIView? {
        return view
    }

}
