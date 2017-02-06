package com.yy.codex.uikit

import android.content.Context

/**
 * Created by it on 17/1/23.
 */

enum class UITableViewCellStyle {
    UITableViewCellStyleDefault
}

open class UITableViewCell(context: Context, val style: UITableViewCellStyle, val reuseIdentifier: String) : UIView(context) {

    var line = UIPixelLine(context)
    init {
        addSubview(line)
        line.setBackgroundColor(UIColor.blackColor)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        line.frame = CGRect(0.00, frame.size.height - 1, frame.size.width, 1.00)
    }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)

    }
}