package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/10.
 */

abstract class UITextFieldDelegateObject: UITextField.Delegate {

    override fun shouldBeginEditing(textField: UITextField): Boolean {
        return true
    }

    override fun didBeginEditing(textField: UITextField) {
        
    }

    override fun shouldEndEditing(textField: UITextField): Boolean {
        return true
    }

    override fun didEndEditing(textField: UITextField) {
        
    }

    override fun shouldChangeCharactersInRange(textField: UITextField, inRange: NSRange, replacementString: String): Boolean {
        return true
    }

    override fun shouldClear(textField: UITextField): Boolean {
        return true
    }

    override fun shouldReturn(textField: UITextField): Boolean {
        return true
    }

}