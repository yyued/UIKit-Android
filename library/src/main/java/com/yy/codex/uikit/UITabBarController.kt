package com.yy.codex.uikit

import android.content.Context

/**
 * Created by saiakirahui on 2017/1/28.
 */
open class UITabBarController(context: Context): UIViewController(context) {

    val tabBar: UITabBar = UITabBar(context)
    val wrapperView: UIView = UIView(context)

    override fun viewDidLoad() {
        super.viewDidLoad()
        view?.let {
            it.addSubview(tabBar)
            it.addSubview(wrapperView)
        }
    }

    open fun setViewControllers(viewControllers: Array<UIViewController>) {
        for (childViewController in childViewControllers) {
            childViewController.removeFromParentViewController()
        }
        for (childViewController in viewControllers) {
            addChildViewController(childViewController)
        }
        resetTabItems()
        resetChildViews()
        selectedIndex = 0
    }

    var selectedIndex = 0
        set(value) {
            field = value
            tabBar.selectedItem = selectedViewController?.tabBarItem
            childViewControllers.forEach {
                if (selectedViewController === it) {
                    it.view?.alpha = 1.0f
                }
                else {
                    it.view?.alpha = 0.0f
                }
            }
        }

    var selectedViewController: UIViewController? = null
        get() = childViewControllers[selectedIndex]

    private fun resetTabItems() {
        tabBar.items = childViewControllers.map { it.tabBarItem }
    }

    private fun resetChildViews() {
        val subviews = wrapperView.subviews
        for (i in subviews.indices) {
            subviews[i].removeFromSuperview()
        }
        val childViewControllers = childViewControllers
        for (childViewController in childViewControllers) {
            childViewController.view?.let {
                wrapperView.addSubview(it)
            }
        }
        resetContentViewsFrame()
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        view?.let {
            wrapperView.frame = CGRect(0.0, 0.0, it.frame.width, it.frame.height)
        }
        resetContentViewsFrame()
    }

    internal fun resetContentViewsFrame() {
        val subviews = wrapperView.subviews
        for (subview in subviews) {
            subview.frame = CGRect(0.0, topLayoutLength(), wrapperView.frame.width, wrapperView.frame.height - topLayoutLength())
        }
    }

}