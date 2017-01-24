package com.yy.codex.uikit

import android.content.Context

/**
 * Created by it on 17/1/24.
 */

class UITableViewHeaderFooterView(context: Context) : UIView(context) {

    var label = UILabel(context)
        public set(value) {
            field = value
        }

    init {
        addSubview(label)
    }
}