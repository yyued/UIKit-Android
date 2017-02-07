package com.yy.codex.uikit

import android.content.Context

/**
 * Created by PonyCui_Home on 2017/2/7.
 */
class UIMenuController {

    enum class ArrowDirection {
        Default, // up or down based on screen location
        Up,
        Down,
        Left,
        Right,
    }

    private constructor()

    var menuVisible = false

    fun setMenuVisible(visible: Boolean, animated: Boolean) {
        menuVisible = visible
    }

    fun setTargetWithRect(targetRect: CGRect, targetView: UIView) {
        this.targetRect = targetRect
        this.targetView = targetView
        createMenuView()
    }

    var arrowDirection: ArrowDirection = ArrowDirection.Default
        set(value) {
            field = value
            createMenuView()
        }

    var menuItems: List<UIMenuItem> = listOf()
        set(value) {
            field = value
            createMenuView()
        }

    private var targetRect: CGRect? = null
    private var targetView: UIView? = null
    private var menuView: UIView? = null
    private var maskView: UIView? = null

    private fun createMenuView() {
        targetView?.let {
            menuView = UIView(it.context)
            maskView = UIView(it.context)
            maskView?.setBackgroundColor(UIColor.clearColor)
            maskView?.frame = CGRect(0.0, 0.0, UIScreen.mainScreen.bounds().width, UIScreen.mainScreen.bounds().height)
        }
    }

    companion object {

        var sharedMenuController = UIMenuController()

    }

}