package com.yy.codex.uikit

import android.content.Context

/**
 * Created by PonyCui_Home on 2017/1/20.
 */

open class UINavigationController(context: Context) : UIViewController(context) {

    val navigationBar: UINavigationBar = UINavigationBar(context)
    val wrapperView: UIView = UIView(context)

    fun setRootViewController(rootViewController: UIViewController) {
        setViewControllers(arrayOf(rootViewController))
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        view?.let {
            it.addSubview(wrapperView)
            it.addSubview(navigationBar)
        }
    }

    fun setViewControllers(viewControllers: Array<UIViewController>) {
        for (childViewController in childViewControllers) {
            childViewController.removeFromParentViewController()
        }
        for (childViewController in viewControllers) {
            addChildViewController(childViewController)
        }
        resetNavigationItems()
        resetChildViews()
    }

    private var beingPush = false

    fun pushViewController(viewController: UIViewController, animated: Boolean) {
        beingPush = true
        addChildViewController(viewController)
        resetNavigationItems()
        resetChildViews()
        beingPush = false
        doPushAnimation()
    }

    private fun doPushAnimation() {

    }

    private val beingPop = false

    fun popViewController(animated: Boolean) {

    }

    fun resetNavigationItems() {
        navigationBar.setItems(childViewControllers.map { it.navigationItem }, false)
    }

    fun resetChildViews() {
        if (beingPush) {
            val childViewControllers = childViewControllers
            if (childViewControllers.size > 0) {
                val currentView = childViewControllers[childViewControllers.size - 1].view
                currentView?.let {
                    wrapperView.addSubview(it)
                }
            }
        } else if (beingPop) {
            val childViewControllers = childViewControllers
            if (childViewControllers.size > 0) {
                val currentView = childViewControllers[childViewControllers.size - 1].view
                currentView?.let(UIView::removeFromSuperview)
            }
        } else {
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
        }
        resetContentViewsFrame()
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        wrapperView.frame = CGRect(0.0, 0.0, view!!.frame.width, view!!.frame.height)
        resetContentViewsFrame()
    }

    private fun resetContentViewsFrame() {
        val subviews = wrapperView.subviews
        for (subview in subviews) {
            subview.frame = CGRect(0.0, topLayoutLength(), wrapperView.frame.width, wrapperView.frame.height - topLayoutLength())
        }
    }

}
