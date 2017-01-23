package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIViewAnimation {

    var cancelled = false
        private set
    var finished = false
        private set

    internal var completion: Runnable? = null

    fun cancel() {
        if (!finished) {
            cancelled = true
            completion?.run()
        }
    }

    fun markFinished() {
        finished = true
        completion = null
    }

}
