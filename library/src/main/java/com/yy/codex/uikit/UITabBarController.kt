package com.yy.codex.uikit

import android.content.Context
import android.view.MotionEvent

/**
 * Created by saiakirahui on 2017/1/28.
 */
open class UITabBarController(context: Context): UIViewController(context) {

    val tabBar: UITabBar = UITabBar(context)
    val wrapperView: UIView = UIView(context)

    override fun loadView() {
        view = UITabBarControllerView(context)
    }

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
            UIResponder.firstResponder?.let(UIResponder::resignFirstResponder)
            childViewControllers.forEach {
                it.view?.hidden = selectedViewController !== it
            }
            tabBar.selectedItem = selectedViewController?.tabBarItem
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

    inner class UITabBarControllerView(context: Context) : UIView(context) {

        override fun hitTest(point: CGPoint, event: MotionEvent): UIView? {
            (selectedViewController as? UINavigationController)?.let {
                if (it.childViewControllers.last().hidesBottomBarWhenPushed) {
                    return super.hitTest(point, event)
                }
            }
            val views = subviews.reversed()
            val mutableViews: MutableList<UIView> = mutableListOf()
            mutableViews.addAll(views.filter { it is UITabBar })
            mutableViews.addAll(views.filter { it !is UITabBar })
            if (!userInteractionEnabled && alpha <= 0) {
                return null
            }
            if (UIViewHelpers.pointInside(this, point)) {
                for (subview in mutableViews) {
                    if (!subview.userInteractionEnabled || subview.alpha <= 0) {
                        continue
                    }
                    val convertedPoint = this.convertPoint(point, subview)
                    val hitTestView = subview.hitTest(convertedPoint, event)
                    if (hitTestView != null) {
                        return hitTestView
                    }
                }
                return this
            }
            return null
        }

    }

}