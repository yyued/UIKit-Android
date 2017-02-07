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

    }

    var arrowDirection: ArrowDirection = ArrowDirection.Default

    var menuItems: List<UIMenuItem> = listOf()

    private var menuView: UIView? = null
    private var maskView: UIView? = null

    private var context: Context? = null

    private fun createMenuView() {
        context?.let {
            menuView = UIView(it)
            maskView = UIView(it)
            maskView?.frame = CGRect(0.0, 0.0, UIScreen.mainScreen.bounds().width, UIScreen.mainScreen.bounds().height)
        }
    }

    companion object {

        var sharedMenuController = UIMenuController()

    }

}