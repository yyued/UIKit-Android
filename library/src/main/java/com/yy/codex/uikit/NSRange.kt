package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/9.
 */

class NSRange(val location: Int, val length: Int) {

    override fun equals(other: Any?): Boolean {
        var other = other as? NSRange ?: return false
        return location == other.location && length == other.length
    }

}
