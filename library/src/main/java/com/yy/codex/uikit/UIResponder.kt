package com.yy.codex.uikit

/**
 * Created by it on 17/1/6.
 */

interface UIResponder {

    val nextResponder: UIResponder?

    fun canBecomeFirstResponder(): Boolean
    fun becomeFirstResponder()
    fun canResignFirstResponder(): Boolean
    fun resignFirstResponder()
    fun isFirstResponder(): Boolean

    fun touchesBegan(touches: List<UITouch>, event: UIEvent)
    fun touchesMoved(touches: List<UITouch>, event: UIEvent)
    fun touchesEnded(touches: List<UITouch>, event: UIEvent)

    fun keyboardPressDown(event: UIKeyEvent)
    fun keyboardPressUp(event: UIKeyEvent)

    companion object {

        var firstResponder: UIResponder? = null
        set(value) {
            field?.let {
                field = null
                it.resignFirstResponder()
            }
            field = value
        }

        fun findFirstResponder(currentResponder: UIResponder): UIResponder? {
            if (currentResponder.canBecomeFirstResponder()) {
                return currentResponder
            }
            if (currentResponder is UIViewController) {
                if (currentResponder.view?.canBecomeFirstResponder() ?: false) {
                    return currentResponder.view
                }
                for (childResponder in currentResponder.childViewControllers) {
                    val firstResponder = findFirstResponder(childResponder) ?: continue
                    return firstResponder
                }
            }
            else if (currentResponder is UIView) {
                for (childResponder in currentResponder.subviews) {
                    val firstResponder = findFirstResponder(childResponder) ?: continue
                    return firstResponder
                }
            }
            return null
        }

    }

}
