package com.yy.codex.uikit

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by it on 17/1/23.
 */

enum class UITableViewCellStyle {
    UITableViewCellStyleDefault
}

open class UITableViewCell : UIView {

    constructor(context: Context, view: View) : super(context, view) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    constructor(context: Context, style: UITableViewCellStyle, reuseIdentifier: String): super(context) {
        this.style = style
        this.reuseIdentifier = reuseIdentifier
    }

    var style: UITableViewCellStyle = UITableViewCellStyle.UITableViewCellStyleDefault
    var reuseIdentifier: String? = null

}