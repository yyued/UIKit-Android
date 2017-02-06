package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/2/6.
 */
interface UITextInputTraits {

    /**
     * default is NO
     */
    var keyboardType: UIKeyboardType

    /**
     * default is NO
     */
    var returnKeyType: UIReturnKeyType

    /**
     * default is NO
     */
    var secureTextEntry: Boolean

}

enum class UIKeyboardType {
    Default,                // Default type for the current input method.
    Password,               // Password keyboard.
    EmailAddress,           // A type optimized for multiple email address entry (shows space @ .
    URL,                    // A type optimized for URL entry (shows . / .com prominently).
    DecimalPad,             // A number pad with a decimal point.
    NumberPad,              // A number pad with locale-appropriate digits (0-9, ۰-۹, ०-९, etc.). Suitable for PIN entry.
    PhonePad,               // A phone pad (1-9, *, 0, #, with letters under the numbers).
}

enum class UIReturnKeyType {
    Default,
    Go,
    Next,
    Search,
    Send,
    Done,
}