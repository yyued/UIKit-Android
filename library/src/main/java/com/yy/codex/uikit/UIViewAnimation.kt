package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIViewAnimation {

    var cancelled = false
        private set
    var finished = false
        private set

    fun cancel() {
        cancelled = true
    }

    fun markFinished() {
        finished = true
    }

}
