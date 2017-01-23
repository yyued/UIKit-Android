package com.yy.codex.uikit

import android.graphics.Color

/**
 * Created by cuiminghui on 2017/1/9.
 */

class NSShadow {

    var shadowOffset = CGSize(1.0, 1.0)
    var shadowBlurRadius = 1.0
    var shadowColor = Color.BLACK

    constructor() {}

    constructor(shadowOffset: CGSize, shadowBlurRadius: Double, shadowColor: Int) {
        this.shadowOffset = shadowOffset
        this.shadowBlurRadius = shadowBlurRadius
        this.shadowColor = shadowColor
    }

}
