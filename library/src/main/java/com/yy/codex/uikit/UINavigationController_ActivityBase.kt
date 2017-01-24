package com.yy.codex.uikit

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.util.*

/**
 * Created by cuiminghui on 2017/1/24.
 */

class UINavigationController_ActivityBase(context: Context) : UINavigationController(context) {

    companion object {

        internal var prevViewControllers: HashMap<Int, UIViewController> = hashMapOf()
        internal var nextViewControllers: HashMap<Int, UIViewController> = hashMapOf()

    }

    override fun pushViewController(viewController: UIViewController, animated: Boolean) {
        if (context is UINavigationActivity) {
            this.viewWillDisappear(animated)
            viewController.viewWillAppear(animated)
            val intent = Intent(context, context.javaClass)
            prevViewControllers.put(viewController.hashCode(), this)
            nextViewControllers.put(viewController.hashCode(), viewController)
            intent.putExtra("com.yy.codex.uikit.UINavigationController_ActivityBase.nextViewController.hashCode", viewController.hashCode())
            context.startActivity(intent)
            this.viewDidDisappear(animated)
            viewController.viewDidAppear(animated)
        }
    }

    override fun popViewController(animated: Boolean) {
        this.viewWillDisappear(true)
        prevViewControllers[this.hashCode()]?.let {
            it.viewWillAppear(true)
        }
        val context = context as? Activity ?: return
        context.finish()
        prevViewControllers[this.hashCode()]?.let {
            it.viewDidAppear(true)
        }
        this.viewDidDisappear(true)
    }

}
