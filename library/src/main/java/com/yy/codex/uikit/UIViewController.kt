package com.yy.codex.uikit

import android.app.Activity
import android.content.Context

import java.lang.ref.WeakReference
import java.util.ArrayList

/**
 * Created by cuiminghui on 2017/1/19.
 */

open class UIViewController(val context: Context) : UIResponder {

    /* Public */

    fun postDelay(runnable: Runnable, millisecond: Long) {
        view?.let {
            it.postDelayed(runnable, millisecond)
        }
    }

    fun runOnUIThread(runnable: Runnable) {
        if (context is Activity) {
            context.runOnUiThread(runnable)
        }
    }

    /* UIResponder */

    override val nextResponder: UIResponder?
        get() {
            return parentViewController
        }

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        nextResponder?.let {
            it.touchesBegan(touches, event)
        }
    }

    override fun touchesMoved(touches: List<UITouch>, event: UIEvent) {
        nextResponder?.let {
            it.touchesMoved(touches, event)
        }
    }

    override fun touchesEnded(touches: List<UITouch>, event: UIEvent) {
        nextResponder?.let {
            it.touchesEnded(touches, event)
        }
    }

    /* Props */

    var title: String? = null
        set(title) {
            field = title
            navigationItem.title = title
        }

    /* View Manager */

    var isViewLoaded = false
        private set

    var view: UIView? = null
        get() {
            if (field == null) {
                loadView()
            }
            return field
        }
        set(view) {
            field?.let {
                it.viewController = null
            }
            field = view
            field?.let {
                it.viewController = this
            }
            if (!isViewLoaded) {
                isViewLoaded = true
                viewDidLoad()
            }
        }

    open fun loadView() {
        val aView = UIView(context)
        aView.setBackgroundColor(UIColor.whiteColor)
        view = aView
    }

    fun loadViewIfNeeded() {
        loadView()
    }

    open fun viewDidLoad() {
        for (childViewController in childViewControllers) {
            childViewController.loadViewIfNeeded()
        }
    }

    fun viewWillAppear(animated: Boolean) {
        for (childViewController in childViewControllers) {
            childViewController.viewWillAppear(animated)
        }
    }

    fun viewDidAppear(animated: Boolean) {
        for (childViewController in childViewControllers) {
            childViewController.viewDidAppear(animated)
        }
    }

    fun viewWillDisappear(animated: Boolean) {
        for (childViewController in childViewControllers) {
            childViewController.viewWillDisappear(animated)
        }
    }

    fun viewDidDisappear(animated: Boolean) {
        for (childViewController in childViewControllers) {
            childViewController.viewDidDisappear(animated)
        }
    }

    open fun viewWillLayoutSubviews() {
        for (childViewController in childViewControllers) {
            childViewController.viewWillLayoutSubviews()
        }
    }

    fun viewDidLayoutSubviews() {
        for (childViewController in childViewControllers) {
            childViewController.viewDidLayoutSubviews()
        }
    }

    /* ChildViewControllers */

    var childViewControllers: List<UIViewController> = listOf()
        private set

    var parentViewController: UIViewController? = null
        private set

    fun addChildViewController(childController: UIViewController) {
        childController.parentViewController = this
        if (!childViewControllers.contains(childController)) {
            val mutableList = childViewControllers.toMutableList()
            mutableList.add(childController)
            childViewControllers = mutableList.toList()
        }
    }

    fun removeFromParentViewController() {
        parentViewController?.let {
            it.childViewControllers = it.childViewControllers.filter {
                it !== this
            }
        }
    }

    /* NavigationController */

    fun navigationController(): UINavigationController? {
        var nextResponder = nextResponder
        while (nextResponder != null) {
            if (nextResponder is UINavigationController) {
                return nextResponder
            }
            nextResponder = nextResponder.nextResponder
        }
        return null
    }

    val navigationItem: UINavigationItem = UINavigationItem(context)

    /* Layout */

    fun topLayoutLength(): Double {
        var length = 0.0
        val navigationController = if (this is UINavigationController) this else navigationController()
        if (navigationController != null) {
            length += navigationController.navigationBar.length()
        }
        return length
    }

    fun bottomLayoutLength(): Double {
        return 0.0
    }

}
