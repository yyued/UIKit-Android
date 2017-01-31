package com.yy.codex.uikit

import android.content.Context
import com.yy.codex.foundation.lets
import java.util.*

/**
 * Created by saiakirahui on 2017/1/28.
 */
open class UITabBarItem: UIBarItem() {

    var itemIndex: Int = 0

    fun attachTabBar(tabBar: UITabBar) {
        this.tabBar = tabBar
    }

    open fun setSelected(selected: Boolean) {
        if (selected) {
            lets(title, titleTextAttributes(EnumSet.of(UIControl.State.Selected))) { title, attrs ->
                titleLabel?.attributedText = NSAttributedString(title, attrs)
            }
        }
        else {
            lets(title, titleTextAttributes(EnumSet.of(UIControl.State.Normal))) { title, attrs ->
                titleLabel?.attributedText = NSAttributedString(title, attrs)
            }
        }
        resetImageView(selected)
    }

    protected var tabBar: UITabBar? = null
        set(value) {
            field = value
            resetImageView(false)
            resetLabel()
        }

    private var frontView: UIButton? = null
    private var iconImageView: UIImageView? = null
    private var titleLabel: UILabel? = null

    private fun resetImageView(selected: Boolean) {
        lets(tabBar, iconImageView) { tabBar, iconImageView ->
            if (iconImageView.image?.renderingMode != UIImage.RenderingMode.AlwaysOriginal) {
                if (selected) {
                    iconImageView.layer.bitmapColor = tabBar.tintColor
                }
                else {
                    iconImageView.layer.bitmapColor = UIColor.blackColor.colorWithAlpha(0.55)
                }
            }
        }
    }

    private fun resetLabel() {
        tabBar?.let {
            titleTextAttributes = hashMapOf(
                    Pair(EnumSet.of(UIControl.State.Normal), hashMapOf(
                            Pair(NSAttributedString.NSFontAttributeName, UIFont(10f) as Any),
                            Pair(NSAttributedString.NSForegroundColorAttributeName, UIColor.blackColor.colorWithAlpha(0.55) as Any)
                    )),
                    Pair(EnumSet.of(UIControl.State.Selected), hashMapOf(
                            Pair(NSAttributedString.NSFontAttributeName, UIFont(10f) as Any),
                            Pair(NSAttributedString.NSForegroundColorAttributeName, it.tintColor as Any)
                    ))
            )
        }
    }

    override fun getContentView(context: Context): UIView? {
        if (view == null) {
            frontView = UIButton(context)
            /* ImageView */
            var imageView = UIImageView(context)
            image?.let {
                imageView.image = it
                imageView.constraint = UIConstraint()
                imageView.constraint?.centerHorizontally = true
                imageView.constraint?.centerVertically = true
                imageView.constraint?.top = "" + (-6 - imageInsets.bottom + imageInsets.top)
            }
            frontView?.addSubview(imageView)
            iconImageView = imageView
            /* Label */
            val label = UILabel(context)
            label.constraint = UIConstraint()
            label.constraint?.bottom = "" + (3 + titleInsets.bottom - titleInsets.top)
            label.constraint?.centerHorizontally = true
            lets(title, titleTextAttributes(EnumSet.of(UIControl.State.Normal))) { title, attrs ->
                label.attributedText = NSAttributedString(title, attrs)
            }
            frontView?.addSubview(label)
            titleLabel = label
            /* View */
            frontView?.addTarget(this, "onTouchUpInside", UIControl.Event.TouchUpInside)
            view = frontView
        }
        return view
    }

    protected fun onTouchUpInside() {
        tabBar?.let {
            var nextResponder = it.nextResponder
            while (nextResponder != null) {
                if (nextResponder is UITabBarController) {
                    nextResponder.selectedIndex = itemIndex
                    break
                }
                nextResponder = nextResponder.nextResponder
            }
        }
    }

}