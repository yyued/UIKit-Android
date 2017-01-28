package com.yy.codex.uikit

import android.content.Context

/**
 * Created by saiakirahui on 2017/1/28.
 */
class UITabBarController(context: Context): UIViewController(context) {

    val tabBar: UITabBar = UITabBar(context)
    val wrapperView: UIView = UIView(context)

    override fun viewDidLoad() {
        super.viewDidLoad()
        view?.let {
            it.addSubview(wrapperView)
            it.addSubview(tabBar)
        }
    }

    fun setViewControllers(viewControllers: Array<UIViewController>) {
        for (childViewController in childViewControllers) {
            childViewController.removeFromParentViewController()
        }
        for (childViewController in viewControllers) {
            addChildViewController(childViewController)
        }
        resetTabItems()
        resetChildViews()
    }

    fun resetTabItems() {

    }

    fun resetChildViews() {
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

    protected fun resetContentViewsFrame() {
        val subviews = wrapperView.subviews
        for (subview in subviews) {
            subview.frame = CGRect(0.0, topLayoutLength(), wrapperView.frame.width, wrapperView.frame.height - topLayoutLength())
        }
    }

}