package com.yy.codex.uikit

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import java.util.*

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

    override fun keyboardPressUp(event: UIKeyEvent) {
        super.keyboardPressUp(event)
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            popViewController(true)
        }
    }

    /* Animation Base */

    private var currentAnimation: UIViewAnimation? = null

    fun setViewControllers(viewControllers: Array<UIViewController>) {
        currentAnimation?.cancel()
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

    open fun pushViewController(viewController: UIViewController, animated: Boolean) {
        if (childViewControllers.count() > 0) {
            childViewControllers.last().viewWillDisappear(animated)
        }
        viewController.viewWillAppear(animated)
        currentAnimation?.cancel()
        beingPush = true
        addChildViewController(viewController)
        resetChildViews()
        beingPush = false
        navigationBar.pushNavigationItem(viewController.navigationItem, animated)
        if (animated) {
            doPushAnimation()
        }
        else {
            if (childViewControllers.count() > 1) {
                childViewControllers[childViewControllers.count() - 2].viewDidDisappear(animated)
            }
            viewController.viewDidAppear(animated)
        }
    }

    open fun doPushAnimation() {
        val subviews = wrapperView.subviews
        if (subviews.count() >= 2) {
            val frontView = subviews.last()
            val backView = subviews[subviews.count() - 2]
            frontView.frame = frontView.frame.setX(wrapperView.frame.width)
            backView.frame = backView.frame.setX(0.0)
            currentAnimation = UIViewAnimator.springWithBounciness(1.0, 75.0, Runnable {
                frontView.frame = frontView.frame.setX(0.0)
                backView.frame = backView.frame.setX(wrapperView.frame.width * -0.20)
            }, Runnable {
                if (childViewControllers.count() > 1) {
                    childViewControllers[childViewControllers.count() - 2].viewDidDisappear(true)
                    childViewControllers[childViewControllers.count() - 1].viewDidAppear(true)
                }
            })
        }
    }

    private val beingPop = false

    open fun popViewController(animated: Boolean) {
        currentAnimation?.cancel()
        navigationBar.popNavigationItem(animated)
        if (animated) {
            if (childViewControllers.count() >= 2) {
                val lastViewController = childViewControllers.last()
                val backViewController = childViewControllers[childViewControllers.count() - 2]
                lastViewController.viewWillDisappear(true)
                backViewController.viewWillAppear(true)
                doPopAnimation(Runnable {
                    lastViewController.removeFromParentViewController()
                    resetChildViews()
                    lastViewController.viewDidDisappear(false)
                    backViewController.viewDidAppear(false)
                })
            }
        }
        else {
            if (childViewControllers.count() >= 2) {
                val lastViewController = childViewControllers.last()
                val backViewController = childViewControllers[childViewControllers.count() - 2]
                lastViewController.viewWillDisappear(false)
                backViewController.viewWillAppear(false)
                lastViewController.removeFromParentViewController()
                resetChildViews()
                lastViewController.viewDidDisappear(false)
                backViewController.viewDidAppear(false)
            }
        }
    }

    open fun doPopAnimation(completion: Runnable) {
        val subviews = wrapperView.subviews
        if (subviews.count() >= 2) {
            val frontView = subviews.last()
            val backView = subviews[subviews.count() - 2]
            frontView.frame = frontView.frame.setX(0.0)
            backView.frame = backView.frame.setX(wrapperView.frame.width * -0.20)
            currentAnimation = UIViewAnimator.springWithBounciness(1.0, 75.0, Runnable {
                frontView.frame = frontView.frame.setX(wrapperView.frame.width)
                backView.frame = backView.frame.setX(0.0)
            }, completion)
        }
    }

    protected fun resetNavigationItems() {
        navigationBar.setItems(childViewControllers.map { it.navigationItem })
    }

    protected fun resetChildViews() {
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
        view?.let {
            wrapperView.frame = CGRect(0.0, 0.0, it.frame.width, it.frame.height)
        }
        resetContentViewsFrame()
    }

    protected fun resetContentViewsFrame() {
        val subviews = wrapperView.subviews
        for (subview in subviews) {
            subview.frame = CGRect(0.0, topLayoutLength(), wrapperView.frame.width, wrapperView.frame.height - topLayoutLength() - bottomLayoutLength())
        }
    }

}
