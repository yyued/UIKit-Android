package com.yy.codex.uikit

/**
 * Created by it on 17/1/6.
 */

interface UIResponder {

    val nextResponder: UIResponder?

    fun touchesBegan(touches: Array<UITouch>, event: UIEvent)

    fun touchesMoved(touches: Array<UITouch>, event: UIEvent)

    fun touchesEnded(touches: Array<UITouch>, event: UIEvent)

}
