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
                main = it
                field = it
                setContentView(it.view)
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
            val hashCode = intent.getIntExtra("com.yy.codex.uikit.UINavigationController_ActivityBase.nextViewController.hashCode", 0)
            return UINavigationController_ActivityBase.nextViewControllers[hashCode]
        }
        else {
            return null
        }
    }

    open fun rootViewController(): UIViewController {
        return UIViewController(this)
    }

}