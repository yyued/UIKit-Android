package com.yy.codex.uikit

import android.content.Context

import java.util.ArrayList

/**
 * Created by cuiminghui on 2017/1/18.
 */

class UINavigationItem(val mContext: Context) {

    internal var navigationBar: UINavigationBar? = null

    /* Title */

    var title: String? = null
        set(title) {
            field = title
            updateTitle()
        }

    fun updateTitle() {
        (titleView as? UILabel)?.let {
            var titleView = it;
            val navigationBar = navigationBar as? UINavigationBar
            if (navigationBar != null && navigationBar.titleTextAttributes != null) {
                navigationBar.titleTextAttributes?.let {
                    if (title is String) {
                        titleView.attributedText = NSAttributedString(title as String, it)
                    }
                    else {
                        titleView.text = ""
                    }
                }
            }
            else {
                titleView.text = title
            }
            titleView.superview?.let(UIView::layoutSubviews)
        }
    }

    /* FrontView */

    internal var frontView: UINavigationItemView = UINavigationItemView(mContext);

    fun setNeedsUpdate() {
        for (subview in frontView.subviews) {
            subview.removeFromSuperview()
        }
        titleView?.let {
            updateTitle()
            frontView.addSubview(it)
            frontView.titleView = it
        }
        frontView.leftViews = leftBarButtonItems.mapNotNull { it.getContentView(mContext) }
        for (view in frontView.leftViews) {
            frontView.addSubview(view)
        }
        frontView.rightViews = rightBarButtonItems.mapNotNull { it.getContentView(mContext) }
        for (view in frontView.rightViews) {
            frontView.addSubview(view)
        }
    }

    /* TitleView */

    var titleView: UIView? = null
        get() {
            if (field == null) {
                val labelTitleView = UILabel(mContext)
                labelTitleView.font = UIFont(17f)
                labelTitleView.textColor = UIColor.blackColor
                field = labelTitleView
            }
            return field
        }
        set(titleView) {
            field?.let(UIView::removeFromSuperview)
            field = titleView
            setNeedsUpdate()
        }


    /* BarButtonItems */

    var leftBarButtonItems: List<UIBarButtonItem> = listOf()
        set(value) {
            field = value
            setNeedsUpdate()
        }

    fun setLeftBarButtonItem(leftBarButtonItem: UIBarButtonItem?) {
        if (leftBarButtonItem == null) {
            leftBarButtonItems = listOf()
        }
        else {
            leftBarButtonItems = listOf(leftBarButtonItem)
        }
    }

    var rightBarButtonItems: List<UIBarButtonItem> = listOf()
        set(value) {
            field = value
            setNeedsUpdate()
        }

    fun setRightBarButtonItem(rightBarButtonItem: UIBarButtonItem?) {
        if (rightBarButtonItem == null) {
            rightBarButtonItems = listOf()
        }
        else {
            rightBarButtonItems = listOf(rightBarButtonItem)
        }
    }

    var backBarButtonItemUsingMaterialDesign = false

    var backBarButtonItem: UIBarButtonItem? = null
        get() {
            val navigationBar = navigationBar ?: return null
            if (navigationBar.materialDesign && !backBarButtonItemUsingMaterialDesign && (field?.isSystemBackItem ?: false)) {
                field = null
            }
            if (field == null && navigationBar.materialDesign) {
                val arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAABsAAAAwCAYAAADgvwGgAAAAAXNSR0IArs4c6QAABDVJREFUWAm1mFuITVEYx791ZpCI3O+XcQmjcUu5FLmNSybkMuZIRBSlvCiFULzhUfIiCXs75ozcLzF4UBIlHjSePHgQymSMu/P5f+fMtte+rX3OmWPVPmut7/bb69trrb32UfQ/is3TiekoQk/E9ZoStIfWqtuq5Cybl1OGLiJuBy12hspodpkmaH/T5hqA0j6QxFUYac/SwWxegoANCNxRooeU1vIQYeEimxdiRALqZHB+2v5nZvF8AK5iVJ0NoI9UTlUJg0G8yuI5MLoSA/oEUDXVqnfFj8ziWYDcBKyL4a6aAVoA0DOxKW6CWDwTvrdwdZUgEeUzQAsBeuroC58gFk9rG5EJ1IKFvAigJw5I6sJgFk+Fz21c3cQ5tCj6AtAS7BiP/fr8J4jNUzCiO7i6+4No/VaAlgL0SJP9a+Y3QWyeCEgjrp7/PP0NRd/aQPf9KqcfP7IUV2HB3jWCiL4DtAwjigQJ0PzMUlxJf+ge7Ho7dxeoFf0AaAVAdwM6nyB6ZPU8FiBJXR+fj9tV9BOglQDJpIkt4SOr59EI0wjvfoYIvwBaDdANg41HFYTZPJJ+keR+gMdS7yj6je5agK7q4ri2N40prkDa7uMaFOmYAyUpqS5F2kQoXFiah+F+BTQkwlZegX+gWw9QfaSNQZFbZxYL4CFAFQbbDJ7RBqpT5ww2RlWCGrgXLGREcaBN7QHJXZRjOR5BPVI6oUVOD0RbAToTqi9AmMBzqDHaK9qGZ3TKaJOnUibIT6NthoYa9QUoE0jShRj7vWTxwRibvNQJ6kX7Yel5yQU8mQ4AuC8gL1CA96lqxbloEfyy54RIf6ZDAO6O1OehcN9n17gHtWCHZ5oc47eL1qljMTahancHqVGfcMSsxux8EWrpCo+SzTvdbv4td2SOT4r7YFOSRT7eEYXWinZgSRwP1UUIgzAxbOC+eCU+AHBchF/uU0HRdiz2k5E2PoWbRl2xUr3HiXIeRE262NPm7JfJCaR0i0du6ISPzHGweCCaskGPckSBOredbUZKTwd0PoEZJsZpHoyUPkRrhM9X72bQ2YhZelYX+tvhadStVqm32K7nImlvdLGvLXvsaTrPSZ/c040fmWNu8XA0JaXRe6W8XBUlMWnkMzdQ8oeJa5pHtKV0cCCSI5Bjg8L5pE7Jx6GnFAYT1xSPwv3LCGXyRBU5ea0B8LJuUDhMvFM8BkBZh/31YJ62nCkJZ8qkuu7I4yeIY6nXtaoJqZJ1+F4Xe9qc/ZBPY9IsduTFwcS7Tr3C7zxAPzjBAjVnP+gvAVgtuuLSqEc9zxPQbcQlB6fwougr/oKZ1H6YhE/xJMxBAfYIp0Gq6GzxadSj1qrnWPiSqmZd7GtXlgYmUeUfgfLsG/+zD+J0m0qTRiec1CmegZTKPwnud7d8w5XRlNLDcsAKrMPDaMrkeQ3QQYz85V9acAeH0YiWqgAAAABJRU5ErkJggg==", 3.0)
                field = UIBarButtonItem(arrowImage, this, "onBackButtonItemTouchUpInside")
                field?.insets = UIEdgeInsets.zero
                field?.isSystemBackItem = true
            }
            else if (field == null) {
                field = UIBarButtonItem(title ?: "Back", this, "onBackButtonItemTouchUpInside")
                field?.image = UIImage("iVBORw0KGgoAAAANSUhEUgAAACcAAABCCAYAAADUms/cAAAAAXNSR0IArs4c6QAAAbFJREFUaAXt2stNxDAQBuDfSNQBpWwVcORRBhHSXqALkOBCG1SCdkUfeD2CKC8ncex5+MAcR5b8aRxLmUmA/1iowKO/QuMvYivOYkm1XONv8IMPeHzGgE4NMt6IYMBrgLUFOsBhhyd3bJfa4Kaw1jMA6uPmYROgLm4dNgDq4dJhLfBLB7cV5sIdBu7lcZmwcGvfZHEFMDpbOVwhTA7HAJPBMcH4cYwwXhwzjA8nAOPBCcHKcYKwMpwwLB+nAMvDKcG24xRh23DKsHScASwNZwRbxxnClnHGsHlcBbA4rhLYFFcRbIirDNbhKoQR7nfC4zO6MB/mQ8LRtYY51fO4w7N7lzJ2ONqhMuAQlwsMcw0aH3BXcIrLBQoccRxXCXAeVwFwGWcMXMcZAtNwRsB0nAFwG04ZuB2nCMzDKQHzcQrAMpwwsBwnCOTBCQH5cAJAXhwzkB/HCJTB5QJHb9RyOAagLK4QKI8rAOrgCPjgb0Pr/tL7VYOyS6H0Gb0lpPbFDkecY6dXuVTgHwx7d9DHEXKugj0YLbPBxYAjmC2uDwS+6Rmjo6R0PdH4a+z9ZT2gRMkJbMiClVJCMU0AAAAASUVORK5CYII=", 3.0)
                field?.imageInsets = UIEdgeInsets(0.0, 0.0, 0.0, 4.0)
                field?.isSystemBackItem = true
            }
            return field
        }
        set(backBarButtonItem) {
            field = backBarButtonItem
        }

    fun onBackButtonItemTouchUpInside() {
        var nextResponder: UIResponder? = frontView
        while (nextResponder != null) {
            nextResponder = nextResponder.nextResponder
            if (nextResponder is UINavigationController) {
                nextResponder.popViewController(true)
                return
            }
        }
    }

}
