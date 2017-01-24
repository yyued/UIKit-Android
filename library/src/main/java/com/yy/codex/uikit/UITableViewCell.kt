package com.yy.codex.uikit

import android.content.Context

/**
 * Created by it on 17/1/23.
 */

enum class UITableViewCellStyle {
    UITableViewCellStyleDefault
}

open class UITableViewCell(context: Context, val style: UITableViewCellStyle, val reuseIdentifier: String) : UIView(context) {


}