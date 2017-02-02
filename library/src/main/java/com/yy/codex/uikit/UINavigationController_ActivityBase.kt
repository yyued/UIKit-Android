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

        internal var linkingContexts: HashMap<Int, Int> = hashMapOf()
        internal var linkingReversingContexts: HashMap<Int, Int> = hashMapOf()
        internal var hashedViewControllers: HashMap<Int, UIViewController> = hashMapOf()

    }

    override fun pushViewController(viewController: UIViewController, animated: Boolean) {
        UIResponder.firstResponder?.let(UIResponder::resignFirstResponder)
        if (context is UINavigationActivity || context is UITabBarActivity) {
            val fromViewController = childViewControllers.last()
            val toViewController = viewController
            fromViewController.viewWillDisappear(animated)
            toViewController.viewWillAppear(animated)
            val intent = Intent(context, context.javaClass)
            configureLinking(fromViewController, toViewController)
            intent.putExtra("com.yy.codex.uikit.UINavigationController_ActivityBase.toViewController.hashCode", toViewController.hashCode())
            context.startActivity(intent)
            fromViewController.viewDidDisappear(animated)
            toViewController.viewDidAppear(animated)
        }
    }

    override fun popViewController(animated: Boolean) {
        UIResponder.firstResponder?.let(UIResponder::resignFirstResponder)
        val activeViewController = childViewControllers.last()
        linkingContexts[activeViewController.hashCode()]?.let {
            hashedViewControllers[it]?.let {
                it.navigationController()?.popViewController(animated)
                return
            }
        }
        val fromViewController = childViewControllers.last()
        val fromViewControllerHashCode = fromViewController.hashCode()
        val toViewControllerHashCode = linkingReversingContexts[fromViewControllerHashCode] ?: return
        val toViewController = hashedViewControllers[toViewControllerHashCode] ?: return
        fromViewController.viewWillDisappear(animated)
        toViewController.viewWillAppear(animated)
        val context = context as? Activity ?: return
        context.finish()
        fromViewController.viewDidDisappear(animated)
        toViewController.viewDidAppear(animated)
        removeLinking(fromViewController, toViewController)
    }

    fun configureLinking(fromViewController: UIViewController, toViewController: UIViewController) {
        linkingContexts.put(fromViewController.hashCode(), toViewController.hashCode())
        linkingReversingContexts.put(toViewController.hashCode(), fromViewController.hashCode())
        hashedViewControllers.put(fromViewController.hashCode(), fromViewController)
        hashedViewControllers.put(toViewController.hashCode(), toViewController)
    }

    fun removeLinking(fromViewController: UIViewController, toViewController: UIViewController) {
        linkingContexts.remove(toViewController.hashCode())
        linkingReversingContexts.remove(fromViewController.hashCode())
        hashedViewControllers.remove(fromViewController.hashCode())
    }

}
