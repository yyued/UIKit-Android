package com.yy.codex.uikit

/**
 * Created by it on 17/1/5.
 */



open class UIEvent(val eventType: Type, val subType: SubType) {

    enum class Type {
        Unknown,
        Touches,
        Keyboard,
    }

    enum class SubType {
        Unknown,
    }

}

