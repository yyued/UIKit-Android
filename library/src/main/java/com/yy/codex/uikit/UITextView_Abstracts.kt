package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/10.
 */

abstract class UITextViewDelegateObject: UIScrollViewDelegateObject(), UITextView.Delegate {

    override fun shouldBeginEditing(textView: UITextView): Boolean {
        return true
    }

    override fun shouldEndEditing(textView: UITextView): Boolean {
        return true
    }

    override fun didBeginEditing(textView: UITextView) {
        
    }

    override fun didEndEditing(textView: UITextView) {
        
    }

    override fun shouldChangeTextInRange(textView: UITextView, inRange: NSRange, replacementString: String): Boolean {
        return true
    }

    override fun didChange(textView: UITextView) {
        
    }

}