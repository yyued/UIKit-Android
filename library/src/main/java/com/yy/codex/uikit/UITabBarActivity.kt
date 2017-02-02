package com.yy.codex.uikit

import android.content.Intent
import android.os.Bundle
import java.util.*

/**
 * Created by PonyCui_Home on 2017/2/2.
 */

open class UITabBarActivity : UIActivity() {

    var tabBarController: UITabBarController? = null
        set(value) {
            value?.let {
                it.setViewControllers(createViewControllers())
                field = it
                viewController = it
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (nextViewController() != null) {
            navigationController = createNavigationController()
        }
        else {
            tabBarController = createTabBarController()
        }
    }

    open fun createTabBarController(): UITabBarController {
        return UITabBarController(this)
    }

    open fun createViewControllers(): Array<UIViewController> {
        return arrayOf()
    }

    /* For Activity_Base Needs */

    var navigationController: UINavigationController? = null
        set(value) {
            value?.let {
                it.setRootViewController(nextViewController() ?: rootViewController())
                field = it
                viewController = it
            }
        }

    open fun createNavigationController(): UINavigationController {
        return UINavigationController(this)
    }

    private fun nextViewController(): UIViewController? {
        if (intent is Intent) {
            val toViewControllerHashCode = intent.getIntExtra("com.yy.codex.uikit.UINavigationController_ActivityBase.toViewController.hashCode", 0)
            val fromViewControllerHashCode = UINavigationController_ActivityBase.linkingReversingContexts[toViewControllerHashCode]
            val toViewController = UINavigationController_ActivityBase.hashedViewControllers[toViewControllerHashCode] ?: return null
            val fromViewController = UINavigationController_ActivityBase.hashedViewControllers[fromViewControllerHashCode] ?: return null
            val backBarButtonItem = fromViewController.navigationItem.backBarButtonItem ?: return toViewController
            backBarButtonItem.isSystemBackItem = false
            toViewController.navigationItem.setLeftBarButtonItem(backBarButtonItem)
            return toViewController
        }
        else {
            return null
        }
    }

    open fun rootViewController(): UIViewController {
        return UIViewController(this)
    }

}
