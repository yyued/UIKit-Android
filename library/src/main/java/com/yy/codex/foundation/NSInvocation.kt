package com.yy.codex.foundation

import java.lang.ref.WeakReference
import java.lang.reflect.Method

/**
 * Created by cuiminghui on 2017/1/10.
 */

class NSInvocation(val target: Any, val selector: String) {

    operator fun invoke(): Any? {
        return invoke(target, null)
    }

    operator fun invoke(arguments: Array<Any>): Any? {
        return invoke(target, arguments)
    }

    operator fun invoke(target: Any?, arguments: Array<Any>?): Any? {
        val target = target ?: this.target
        var clazz: Class<*>? = target.javaClass
        val components = if (selector.endsWith(":")) (selector + "_").split(":".toRegex()).dropLastWhile(String::isEmpty).toTypedArray() else selector.split(":".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
        var returnValue: Any? = null
        var found = false
        while (!found && clazz != null) {
            try {
                if (components.size == 1) {
                    val method = clazz.getDeclaredMethod(selector)
                    var accessible = true
                    if (!method.isAccessible) {
                        accessible = false
                        method.isAccessible = true
                    }
                    returnValue = method.invoke(target)
                    if (!accessible) {
                        method.isAccessible = false
                    }
                }
                else if (arguments != null) {
                    if (components.size == 2 && arguments.size >= 1) {
                        val method = clazz.getDeclaredMethod(components[0], arguments[0].javaClass)
                        var accessible = true
                        if (!method.isAccessible) {
                            accessible = false
                            method.isAccessible = true
                        }
                        returnValue = method.invoke(target, arguments[0])
                        if (!accessible) {
                            method.isAccessible = false
                        }
                    } else if (components.size == 3 && arguments.size >= 2) {
                        val method = clazz.getDeclaredMethod(components[0], arguments[0].javaClass, arguments[1].javaClass)
                        var accessible = true
                        if (!method.isAccessible) {
                            accessible = false
                            method.isAccessible = true
                        }
                        returnValue = method.invoke(target, arguments[0], arguments[1])
                        if (!accessible) {
                            method.isAccessible = false
                        }
                    } else if (components.size == 4 && arguments.size >= 3) {
                        val method = clazz.getDeclaredMethod(components[0], arguments[0].javaClass, arguments[1].javaClass, arguments[2].javaClass)
                        var accessible = true
                        if (!method.isAccessible) {
                            accessible = false
                            method.isAccessible = true
                        }
                        returnValue = method.invoke(target, arguments[0], arguments[1], arguments[2])
                        if (!accessible) {
                            method.isAccessible = false
                        }
                    } else if (components.size == 5 && arguments.size >= 4) {
                        val method = clazz.getDeclaredMethod(components[0], arguments[0].javaClass, arguments[1].javaClass, arguments[2].javaClass, arguments[3].javaClass)
                        var accessible = true
                        if (!method.isAccessible) {
                            accessible = false
                            method.isAccessible = true
                        }
                        returnValue = method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3])
                        if (!accessible) {
                            method.isAccessible = false
                        }
                    } else if (components.size == 6 && arguments.size >= 5) {
                        val method = clazz.getDeclaredMethod(components[0], arguments[0].javaClass, arguments[1].javaClass, arguments[2].javaClass, arguments[3].javaClass, arguments[4].javaClass)
                        var accessible = true
                        if (!method.isAccessible) {
                            accessible = false
                            method.isAccessible = true
                        }
                        returnValue = method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4])
                        if (!accessible) {
                            method.isAccessible = false
                        }
                    } else if (components.size == 7 && arguments.size >= 6) {
                        val method = clazz.getDeclaredMethod(components[0], arguments[0].javaClass, arguments[1].javaClass, arguments[2].javaClass, arguments[3].javaClass, arguments[4].javaClass, arguments[5].javaClass)
                        var accessible = true
                        if (!method.isAccessible) {
                            accessible = false
                            method.isAccessible = true
                        }
                        returnValue = method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5])
                        if (!accessible) {
                            method.isAccessible = false
                        }
                    }
                }
                found = true
            } catch (e: Exception) {
                if (e is NoSuchMethodException) {
                    clazz = clazz.superclass
                } else {
                    e.printStackTrace()
                    found = true
                }
            }

        }
        return returnValue
    }

}
