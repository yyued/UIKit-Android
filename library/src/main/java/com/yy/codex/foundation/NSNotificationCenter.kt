package com.yy.codex.foundation

import java.util.*

/**
 * Created by cuiminghui on 2017/2/6.
 */
class NSNotificationCenter {

    companion object {

        var defaultCenter = NSNotificationCenter()

    }

    private constructor()

    private var listeners: MutableMap<String, MutableList<NSInvocation>> = mutableMapOf()

    fun addObserver(observer: Any, selector: String, name: String) {
        if (listeners[name] == null) {
            listeners.put(name, mutableListOf())
        }
        listeners[name]?.let {
            it.add(NSInvocation(observer, selector))
        }
    }

    fun postNotification(notification: NSNotification) {
        listeners.filter { it.key == notification.name }.forEach {
            it.value.forEach {
                it.invoke(arrayOf(notification))
            }
        }
    }

    fun postNotification(name: String, obj: Any? = null, userInfo: Map<String, Any>? = mapOf()) {
        postNotification(NSNotification(name, obj, userInfo))
    }

    fun removeObserver(observer: Any, name: String? = null) {
        if (name != null) {
            listeners[name]?.let {
                listeners.put(name, it.filter { it.target === observer }.toMutableList())
            }
        }
        else {
            listeners.forEach {
                listeners.put(it.key, it.value.filter { it.target === observer }.toMutableList())
            }
        }
    }

}