package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIViewAnimation {

    var isCancelled = false
        private set
    var isFinished = false
        private set

    fun cancel() {
        isCancelled = true
    }

    fun markFinished() {
        isFinished = true
    }

}
