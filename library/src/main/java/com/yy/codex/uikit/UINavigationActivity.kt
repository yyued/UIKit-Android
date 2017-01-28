package com.yy.codex.uikit

import android.content.Intent
import android.os.Bundle

/**
 * Created by PonyCui_Home on 2017/1/23.
 */

open class UINavigationActivity: UIActivity() {

    var navigationController: UINavigationController? = null
        set(value) {
            value?.let {
                it.setRootViewController(nextViewController() ?: rootViewController())
                field = it
                viewController = it
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = createNavigationController()
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